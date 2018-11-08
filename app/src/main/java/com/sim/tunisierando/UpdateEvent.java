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
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.Network.VolleyService;
import com.sim.tunisierando.Services.Interfaces.IResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

public class UpdateEvent extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener,Validator.ValidationListener {
    Toolbar toolbar;
    @NotEmpty
    EditText edup_title,edup_date,edup_prix,edup_nbplace,edup_depart,edup_arrive,edup_description,edup_contact;
    @Select
    Spinner spup_type, spup_difficulty;
    Button btnup_save,btn_upload;
    Validator validator;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    private String TAG = "UpdateEventAct";
    SharedPreferences preferences;
    public static final String OAUTH_TOKEN = "token";
    public static final String MyPREFERENCES = "MyPrefs";
    private int idevent ;
    ArrayList<String> imgArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        overridePendingTransition(R.anim.slide_up,0);
        SocketListeners socketListeners = new SocketListeners(this);

        edup_title = (EditText) findViewById(R.id.edup_evtitle);
        edup_depart = (EditText) findViewById(R.id.edup_evdepart);
        edup_arrive = (EditText) findViewById(R.id.edup_evarrive);
        edup_date = (EditText) findViewById(R.id.edup_evdate);
        edup_description = (EditText) findViewById(R.id.edup_evdescription);
        edup_contact = (EditText) findViewById(R.id.edup_evcontact);
        edup_prix = (EditText) findViewById(R.id.edup_evprix);
        edup_nbplace = (EditText) findViewById(R.id.edup_evnbplaces);
        spup_type = (Spinner) findViewById(R.id.spup_evtype);
        spup_difficulty = (Spinner) findViewById(R.id.spup_evniveau);
        btnup_save = (Button) findViewById(R.id.btnup_save);
        btn_upload = (Button) findViewById(R.id.btnup_evuploadimg);
        preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);


        // validating form

        validator = new Validator(this);
        validator.setValidationListener(this);

        edup_depart.setOnFocusChangeListener(this);
        edup_arrive.setOnFocusChangeListener(this);
        edup_date.setOnClickListener(this);
        btnup_save.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra("obj")) {
            Bundle b = getIntent().getExtras();
            Events e = (Events) b.get("obj");
            idevent = e.getId();
            edup_title.setText(e.getTitle());
            edup_date.setText(e.getDate());
            edup_prix.setText(String.valueOf(e.getPrix()));
            edup_nbplace.setText(String.valueOf(e.getNbrPlace()));
            edup_depart.setText(e.getDepart());
            edup_arrive.setText(e.getArrive());
            if(e.getType().equals("Sportifs")){
                spup_type.setSelection(1);

            }else if(e.getType().equals("Séniors")){
                spup_type.setSelection(2);

            }else if(e.getType().equals("Adultes")){
                spup_type.setSelection(3);
            }else if(e.getType().equals("Famille")){
                spup_type.setSelection(4);
            }else if(e.getType().equals("Jeunes")){
                spup_type.setSelection(5);
            }

            if(e.getNiveau().equals("Niveau A")){
                spup_difficulty.setSelection(1);

            }else if(e.getNiveau().equals("Niveau B")){
                spup_difficulty.setSelection(2);

            }else if(e.getNiveau().equals("Niveau C")){
                spup_difficulty.setSelection(3);
            }
            edup_contact.setText(e.getContact());
            edup_description.setText(e.getDescription());
        }
        toolbar = (Toolbar) findViewById(R.id.evup_toolbar);
        toolbar.setTitle(edup_title.getText());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dialog_close_dark);





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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.edup_evdate:
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(UpdateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if(monthOfYear < 10 ){
                            if(dayOfMonth < 10){
                                String date = String.valueOf(year) + "-" + "0"+String.valueOf(monthOfYear + 1)
                                        + "-" + "0"+String.valueOf(dayOfMonth);
                                edup_date.setText(date);
                            }else {
                                String date = String.valueOf(year) + "-" + "0"+String.valueOf(monthOfYear + 1)
                                        + "-" + String.valueOf(dayOfMonth);
                                edup_date.setText(date);
                            }

                        }else {
                            if(dayOfMonth >= 10){
                                String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                        + "-" +String.valueOf(dayOfMonth);
                                edup_date.setText(date);
                            }else {
                                String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                        + "-" +"0"+String.valueOf(dayOfMonth);
                                edup_date.setText(date);
                            }

                        }

                    }

                }, yy, mm, dd);
                datePicker.show();
                break;

            case R.id.btnup_save:
                validator.validate();
                break;


            case R.id.btnup_evuploadimg:
                Intent intent = new Intent(this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 50); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                break;


        }
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edup_evdepart:
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
                                    edup_depart.setText(locationName);
                                }
                            })
                            .build();
                    placeSearchDialog.show();
                }

                break;

            case R.id.edup_evarrive:
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
                                    edup_arrive.setText(locationName);
                                }
                            })
                            .build();
                    placeSearchDialog1.show();
                }
                break;
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
                    Toast.makeText(UpdateEvent.this, "Evènement modifié", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onValidationSucceeded() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, this);
        JSONObject event = new JSONObject();
        try {
            event.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)));
            event.put("id",idevent);
            event.put("title", edup_title.getText().toString());
            event.put("description", edup_description.getText().toString());
            event.put("contact", edup_contact.getText().toString());
            event.put("date", edup_date.getText().toString());
            event.put("prix", edup_prix.getText().toString());
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
                event.put("image", array);

            }
            event.put("pointDepart", edup_depart.getText().toString());
            event.put("pointArrive", edup_arrive.getText().toString());
            event.put("type", spup_type.getSelectedItem().toString());
            event.put("niveau", spup_difficulty.getSelectedItem().toString());
            event.put("nbrPlace", edup_nbplace.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("*******"+event.toString());
        mVolleyService.postDataVolley("POSTCALL", ServerConfig.UrlForServer + "/updateEvent", event);
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
