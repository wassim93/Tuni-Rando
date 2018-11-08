package com.sim.tunisierando;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Network.VolleyService;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.IResult;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;


public class NewEventActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener,Validator.ValidationListener {

    @NotEmpty
    EditText ed_title, ed_depart, ed_arrive, ed_date, ed_description, ed_prix, ed_nbplace;
    @NotEmpty
    @Length(max=8,min = 8)
    EditText ed_contact;
    Button btn_upload, btn_create;
    @Select

    Spinner sp_type,sp_difficulty;

    UserService userService;

 

    Toolbar toolbar;

    ArrayList<String> imgArray = new ArrayList<>();
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    private String TAG = "NewEventAct";
    private Socket socket;
    String id;
    String username;
    public static final String OAUTH_TOKEN = "token";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        try {
            socket = IO.socket(ServerConfig.UrlForNotificationServer);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        socket.connect();
        preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SocketListeners socketListeners = new SocketListeners(this);
        ed_title = (EditText) findViewById(R.id.ed_evtitle);
        ed_depart = (EditText) findViewById(R.id.ed_evdepart);
        ed_arrive = (EditText) findViewById(R.id.ed_evarrive);
        ed_date = (EditText) findViewById(R.id.ed_evdate);
        ed_description = (EditText) findViewById(R.id.ed_evdescription);
        ed_contact = (EditText) findViewById(R.id.ed_evcontact);
        ed_prix = (EditText) findViewById(R.id.ed_evprix);
        ed_nbplace = (EditText) findViewById(R.id.ed_evnbplaces);
        sp_type = (Spinner) findViewById(R.id.sp_evtype);
        sp_difficulty = (Spinner) findViewById(R.id.sp_evniveau);
        btn_upload = (Button) findViewById(R.id.btn_uploadimg);
        btn_create = (Button) findViewById(R.id.btn_create);

        // validating form

        validator = new Validator(this);
        validator.setValidationListener(this);


        ed_depart.setOnFocusChangeListener(this);
        ed_arrive.setOnFocusChangeListener(this);
        ed_date.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ajouter un évènement");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_create.setOnClickListener(this);
        btn_upload.setOnClickListener(this);


        userService = new UserService(this);
        userService.getUserByAccessToken(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)), new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {


                try {
                    id = result.getString("id");


                    username = result.getString("_fisrt_name");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }



            @Override
            public void onSuccessListProduct(List<Product> result) {

            }

            @Override
            public void onSuccessListComments(List<Comment> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

        });


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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:

                if(imgArray.size() != 0){
                    validator.validate();
                }else{
                    Toast.makeText(this, "Veuillez choisir au moin une photo", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btn_uploadimg:
                Intent intent = new Intent(this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 50); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                break;

            case R.id.ed_evdate:
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(NewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if(monthOfYear < 10 ){
                            if(dayOfMonth < 10){
                                String date = String.valueOf(year) + "-" + "0"+String.valueOf(monthOfYear + 1)
                                        + "-" + "0"+String.valueOf(dayOfMonth);
                                ed_date.setText(date);
                            }else {
                                String date = String.valueOf(year) + "-" + "0"+String.valueOf(monthOfYear + 1)
                                        + "-" + String.valueOf(dayOfMonth);
                                ed_date.setText(date);
                            }

                        }else {
                            if(dayOfMonth >= 10){
                                String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                        + "-" +String.valueOf(dayOfMonth);
                                ed_date.setText(date);
                            }else {
                                String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                        + "-" +"0"+String.valueOf(dayOfMonth);
                                ed_date.setText(date);
                            }

                        }

                    }

                }, yy, mm, dd);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();

                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
                    Log.d(TAG, "Volley JSON post****************" + response.get("response"));
                    Toast.makeText(NewEventActivity.this, "Evenement crée", Toast.LENGTH_SHORT).show();
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

    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.ed_evdepart:
                if (hasFocus) {
                    PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(this)
                            .setHintText("Entrer le point de départ")
                            .setHintTextColor(R.color.lightgray)
                            .setNegativeText("CANCEL")
                            .setNegativeTextColor(R.color.gray)
                            .setPositiveText("SUBMIT")
                            .setPositiveTextColor(R.color.crimson)
                            .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                                @Override
                                public void locationName(String locationName) {
                                    ed_depart.setText(locationName);
                                }
                            })
                            .build();
                    placeSearchDialog.show();
                }

                break;

            case R.id.ed_evarrive:
                if (hasFocus) {
                    PlaceSearchDialog placeSearchDialog1 = new PlaceSearchDialog.Builder(this)
                            .setHintText("Entrer le point d'arrive")
                            .setHintTextColor(R.color.lightgray)
                            .setNegativeText("CANCEL")
                            .setNegativeTextColor(R.color.gray)
                            .setPositiveText("SUBMIT")
                            .setPositiveTextColor(R.color.crimson)
                            .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                                @Override
                                public void locationName(String locationName) {
                                    ed_arrive.setText(locationName);
                                }
                            })
                            .build();
                    placeSearchDialog1.show();
                }
                break;
        }

    }


    @Override
    public void onValidationSucceeded() {
        socket.emit("setnotification", username+" a crée un événement",id);
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject event = new JSONObject();
        try {
            event.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)));
            event.put("title", ed_title.getText().toString());
            event.put("description", ed_description.getText().toString());
            event.put("contact", ed_contact.getText().toString());
            event.put("date", ed_date.getText().toString());
            event.put("prix", ed_prix.getText().toString());
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


                event.put("image", array);
            } else {
                array.put(null);
                event.put("image", array);

            }
            event.put("pointDepart", ed_depart.getText().toString());
            event.put("pointArrive", ed_arrive.getText().toString());
            event.put("type", sp_type.getSelectedItem().toString());
            event.put("niveau", sp_difficulty.getSelectedItem().toString());
            event.put("nbrPlace", ed_nbplace.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVolleyService.postDataVolley("POSTCALL", ServerConfig.UrlForServer + "/AddEvent", event);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError("Champ vide");
            }else if (view instanceof Spinner) {
                ((TextView) ((Spinner) view).getSelectedView()).setError(message);
            }
            else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }


        }
    }



}


