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
import com.sim.tunisierando.Entities.Product;
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

public class UpdateProductActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {
    @NotEmpty
    EditText upprod_title,upprod_desc,upprod_contact,upprod_prix;
    @NotEmpty
    MaterialBetterSpinner upprod_type;
    Button btn_uppro,btn_up_produploadimg;
    ArrayList<String> imgArray = new ArrayList<>();

    IResult mResultCallback = null;
    VolleyService mVolleyService;
    private String TAG = "UpdateProdAct";
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    Toolbar toolbar;
    Validator validator;
    String[] SPINNERLIST = {"Chaussures", "Sac à dos", "Vêtements", "Autres"};
    String title;
    int idprod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        overridePendingTransition(R.anim.slide_up,0);

        preferences =getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SocketListeners socketListeners = new SocketListeners(this);
        upprod_title = (EditText) findViewById(R.id.uppro_title);
        upprod_desc = (EditText) findViewById(R.id.uppro_description);
        upprod_contact = (EditText) findViewById(R.id.uppro_contact);
        upprod_prix = (EditText) findViewById(R.id.uppro_prix);
        upprod_type = (MaterialBetterSpinner) findViewById(R.id.uppro_type);
        btn_uppro = (Button) findViewById(R.id.btn_uppro);
        btn_up_produploadimg = (Button)findViewById(R.id.btn_up_produploadimg);
        btn_uppro.setOnClickListener(this);
        btn_up_produploadimg.setOnClickListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_selectable_list_item, SPINNERLIST);
        upprod_type.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        if(intent.hasExtra("obj")){
            Bundle b = getIntent().getExtras();
            Product e  = (Product) b.get("obj");
            idprod = e.getId();
            upprod_title.setText(e.getTitle());
            upprod_prix.setText(String.valueOf(e.getPrix()));
            title = e.getTitle();
            upprod_contact.setText(e.getContact());
            upprod_desc.setText(e.getDescription());
            if(e.getType().equals("Chaussures")){
                upprod_type.setText(arrayAdapter.getItem(0));

            }else if(e.getType().equals("Sac à dos")){
                upprod_type.setText(arrayAdapter.getItem(1));

            }else if(e.getType().equals("Vêtements")){
                upprod_type.setText(arrayAdapter.getItem(2));
            }else if(e.getType().equals("Autres")){
                upprod_type.setText(arrayAdapter.getItem(3));
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dialog_close_dark);


        validator = new Validator(this);
        validator.setValidationListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_up_produploadimg:
                Intent intent = new Intent(this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 50); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                break;
            case R.id.btn_uppro:
                validator.validate();
                break;
        }


    }

    @Override
    public void onValidationSucceeded() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject product = new JSONObject();
        try {
            product.put("id",""+idprod);
            product.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)));
            product.put("title",upprod_title.getText().toString());
            product.put("description", upprod_desc.getText().toString());
            product.put("contact", upprod_contact.getText().toString());
            product.put("date", getCurrentTimeStamp());
            product.put("prix",upprod_prix.getText().toString());
            product.put("type", upprod_type.getText().toString());
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
                product.put("images", array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("********************update"+product);
        mVolleyService.postDataVolley("POSTCALL", ServerConfig.UrlForServer+"/updateProduct",product);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down,R.anim.slide_down);
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

    void initVolleyCallback() {
        mResultCallback = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {


            }

            @Override
            public void notifySuccessJsonobject(String requestType, JSONObject response) {
                try {
                    Log.d(TAG, "Volley JSON post" + response.get("response"));
                    Toast.makeText(UpdateProductActivity.this, "Produit modifié", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley JSON erreur" + error.toString());
            }
        };

    }


    public  String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }
}
