package com.sim.tunisierando;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.UserInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    ProgressDialog progressDialog;

    EditText nom,prenom,phonenumber,address;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        preferences =getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        nom = (EditText)findViewById(R.id.nom);
        prenom = (EditText)findViewById(R.id.prenom);
        phonenumber = (EditText)findViewById(R.id.phonenumber);
        address = (EditText)findViewById(R.id.address);
        send = (Button)findViewById(R.id.terminer);
        UserInterface userService = new UserService(this);
        SocketListeners socketListeners = new SocketListeners(this);
        userService.getUserByAccessToken(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)), new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {
                try {

                    nom.setText(result.getString("_fisrt_name"));
                    prenom.setText(result.getString("_last_name"));
                    address.setText(result.getString("_address"));
                    phonenumber.setText(result.getString("_phone_number"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

            @Override

            public void onSuccessListProduct(List<Product> result) {}

            public void onSuccessListComments(List<Comment> result) {


            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    JSONObject param = new JSONObject();
                    try {
                        // user_id, comment_id,status


                        param.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
                        param.put("phoneNumber", phonenumber.getText().toString());
                        param.put("adress", address.getText().toString());
                        param.put("firstname", nom.getText().toString());
                        param.put("lastname", prenom.getText().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDialog = new ProgressDialog(UpdateProfileActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.PUT, ServerConfig.UrlForServer + "/CompleteProfile", param,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        Log.d("response", response.getString("response") + "");
                                        if (response.getString("response").equals("updated")) {
                                            Intent i = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                        }
                                        progressDialog.dismiss();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error.Response", error.toString());

                                }
                            }
                    );

                    getRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    AppSingleton.getInstance(UpdateProfileActivity.this).addToRequestQueue(getRequest, "rr");
                }
            }
        });

    }
    public boolean validate(){
        boolean valid = true;
        if(nom.getText().toString().isEmpty()){
            nom.setError("veuillez saisir un nom ");
            valid = false;
        }
        if(prenom.getText().toString().isEmpty()){
            prenom.setError("veuillez saisir un prenom  ");
            valid = false;
        }
        if(address.getText().toString().isEmpty()){
            address.setError("veuillez saisir une adresse ");
            valid = false;
        }
        if(phonenumber.getText().toString().isEmpty() || !Patterns.PHONE.matcher(phonenumber.getText().toString()).matches()|| phonenumber.length()>8){
            phonenumber.setError("veuillez saisir un numéro de téléphone valide  ");
            valid = false;
        }
        return valid;
    }
    }

