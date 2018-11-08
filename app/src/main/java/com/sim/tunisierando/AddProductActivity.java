package com.sim.tunisierando;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Network.VolleyService;
import com.sim.tunisierando.Services.Interfaces.IResult;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {
    @NotEmpty
    EditText prod_title,prod_desc,prod_contact,prod_prix;
    @NotEmpty
    MaterialBetterSpinner prod_type;
    Button btn_enregistrer,btn_produploadimg;
    ArrayList<String> imgArray = new ArrayList<>();

    IResult mResultCallback = null;
    VolleyService mVolleyService;
    private String TAG = "NewProdAct";
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    Toolbar toolbar;
    Validator validator;

    String[] SPINNERLIST = {"Chaussures", "Sac à dos", "Vêtements", "Autres"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        preferences =getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SocketListeners socketListeners = new SocketListeners(this);
        prod_title = (EditText) findViewById(R.id.pro_title);
        prod_desc = (EditText) findViewById(R.id.pro_description);
        prod_contact = (EditText) findViewById(R.id.pro_contact);
        prod_prix = (EditText) findViewById(R.id.pro_prix);
        prod_type = (MaterialBetterSpinner) findViewById(R.id.pro_type);
        btn_enregistrer = (Button) findViewById(R.id.btn_addpro);
        btn_produploadimg = (Button)findViewById(R.id.btn_produploadimg);
        btn_enregistrer.setOnClickListener(this);
        btn_produploadimg.setOnClickListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_selectable_list_item, SPINNERLIST);
        prod_type.setAdapter(arrayAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ajouter un produit");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // validating form

        validator = new Validator(this);
        validator.setValidationListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_produploadimg:
                Intent intent = new Intent(this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 50); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                break;
            case R.id.btn_addpro:
                if(imgArray.size() != 0){
                    validator.validate();
                }else{
                    Toast.makeText(this, "Veuillez choisir au moin une photo", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (Image i : images) {
                imgArray.add(i.path);
            }


        }
    }


    void initVolleyCallback(){
        mResultCallback = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {


            }

            @Override
            public void notifySuccessJsonobject(String requestType, JSONObject response) {
                try {
                    Log.d(TAG, "Volley JSON post" + response.get("response"));
                    Toast.makeText(AddProductActivity.this,"Produit ajouté ",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d(TAG, "Volley JSON erreur" +error.toString());
            }
        };

    }


    @Override
    public void onValidationSucceeded() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject product = new JSONObject();
        try {
            product.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)));
            product.put("title",prod_title.getText().toString());
            product.put("description", prod_desc.getText().toString());
            product.put("contact", prod_contact.getText().toString());
            product.put("date",getCurrentTimeStamp());
            product.put("prix",prod_prix.getText().toString());
            product.put("type", prod_type.getText().toString());
            JSONArray array = new JSONArray();
            if (!imgArray.isEmpty()) {
                for (String s : imgArray) {
                    try {
                        Bitmap bitmap = PhotoLoader.init().from(s).getBitmap();
                        String encodedImg = ImageBase64.encode(bitmap);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("image", encodedImg);
                        array.put(jsonObject);


                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Erreur de chargement des images", Toast.LENGTH_SHORT).show();
                    }
                }


                product.put("images", array);
            }else{
                array.put(null);
                product.put("images", array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVolleyService.postDataVolley("POSTCALL", ServerConfig.UrlForServer+"/AddProduct",product);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError("Champ vide");
            }else if (view instanceof MaterialBetterSpinner) {
                ((TextView) ((Spinner) view).getSelectedView()).setError(message);
            }
            else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }


        }
    }


    public  String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }
}
