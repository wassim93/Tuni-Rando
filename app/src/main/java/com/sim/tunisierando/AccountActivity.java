package com.sim.tunisierando;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
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

public class AccountActivity extends AppCompatActivity {
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    private static int PICK_IMAGE_REQUEST = 1;
    public  int Status ;
    UserInterface userService;
    SharedPreferences preferences;
    ImageView profileimage,imageback,coverimage;
    String urlImages= ServerConfig.UrlForImageLocation;
    TextView email,username ,address,numtel,abonnee;
Button subscribe;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        userService = new UserService(this);
        preferences =getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        profileimage = (ImageView)findViewById(R.id.user_profile_photo);
        coverimage = (ImageView)findViewById(R.id.header_cover_image);
        imageback = (ImageView)findViewById(R.id.imageback);
        email = (TextView) findViewById(R.id.user_profile_short_bio);
        username = (TextView)findViewById(R.id.user_profile_name);
        subscribe  = (Button)findViewById(R.id.subscribe);
        address =  (TextView)findViewById(R.id.address);
        numtel =  (TextView)findViewById(R.id.numtel);
        abonnee =  (TextView)findViewById(R.id.abonnee);
        SocketListeners socketListeners = new SocketListeners(this);
        final int iduser = getIntent().getExtras().getInt("iduser");
        userService.getUserByAccessToken(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)), new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {


                try {
                    android.view.ViewGroup.LayoutParams layoutParams = subscribe.getLayoutParams();
                    if(Integer.parseInt(result.getString("id"))== iduser){
                        layoutParams.height = 0;
                        subscribe.setLayoutParams(layoutParams);

                    }
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

            public void onSuccessListProduct(List<Product> result) { }

            public void onSuccessListComments(List<Comment> result) {


            }
        });

        final JSONObject params1 = new JSONObject();
        try {

            params1.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
            params1.put("iduser",iduser);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checksubscribe", params1,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if(response.getString("response").equals("true")){
                                subscribe.setBackground(getResources().getDrawable( R.drawable.ic_subscribedone));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());

                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(this).addToRequestQueue(getRequest,"tag");
        RequestQueue rq = Volley.newRequestQueue(AccountActivity.this);
        rq.add(getRequest);
        final JSONObject params = new JSONObject();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,ServerConfig.UrlForServer+"/GetUserById/"+iduser,params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.print(response);
                        Log.d("response", response+"");
                        try{
                            Picasso.with(AccountActivity.this).load(urlImages+ response.getString("_profile_pic_url")).placeholder(R.drawable.loader).into(profileimage);

                            Picasso.with(AccountActivity.this).load(urlImages+ response.getString("_background_pic_url")).placeholder(R.drawable.loader).into(coverimage);

                            email.setText(response.getString("email"));

                            username.setText(response.getString("_fisrt_name")+" "+response.getString("_last_name"));
                            address.setText("Adresse : "+response.getString("_address"));

                            numtel.setText("Numéro de télephone :"+ response.getString("_phone_number"));
                            abonnee.setText("Abonnée : "+response.getString("subscribenumber") +" Abonnées ");

                        }catch (JSONException e){

                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());

                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rq1 = Volley.newRequestQueue(AccountActivity.this);
        rq1.add(request);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final JSONObject params = new JSONObject();
                try {

                    params.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
                    params.put("iduser",iduser);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog = new ProgressDialog(AccountActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/subscribe", params,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {


                                try {
                                    if(response.getString("response").equals("subscribed")){
                                        Toast.makeText(AccountActivity.this,
                                                "Abonné", Toast.LENGTH_LONG).show();
                                        subscribe.setBackground(getResources().getDrawable( R.drawable.ic_subscribedone));
                                        abonnee.setText("Abonnée : "+response.getString("ab")+" Abonnées ");
                                    }else {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);


                                        builder.setTitle(" ");

                                        //Setting message manually and performing action on button click
                                        builder.setMessage("Voulez vous annuler votre abonnement ?");
                                        //This will not allow to close dialogbox until user selects an option
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                final JSONObject params1 = new JSONObject();
                                                try {

                                                    params1.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
                                                    params1.put("iduser",iduser);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/deletesubscribe", params1,
                                                        new Response.Listener<JSONObject>()
                                                        {
                                                            @Override
                                                            public void onResponse(JSONObject response) {


                                                                try {
                                                                    if(response.getString("response").equals("deleted")){
                                                                        subscribe.setBackground(getResources().getDrawable( R.drawable.ic_subs));

                                                                        abonnee.setText("Abonnée : "+response.getString("ab")+" Abonnées ");

                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }


                                                            }
                                                        },
                                                        new Response.ErrorListener()
                                                        {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.d("Error.Response", error.toString());

                                                            }
                                                        }
                                                );

                                                getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                                AppSingleton.getInstance(AccountActivity.this).addToRequestQueue(getRequest,"tag");
                                                RequestQueue rq = Volley.newRequestQueue(AccountActivity.this);
                                                rq.add(getRequest);

                                            }
                                        });
                                        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button

                                                dialog.cancel();
                                            }
                                        });

                                        //Creating dialog box
                                        AlertDialog alert = builder.create();
                                        //Setting the title manually
                                        //alert.setTitle("AlertDialogExample");
                                        alert.show();


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();

                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                progressDialog.hide();
                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppSingleton.getInstance(AccountActivity.this).addToRequestQueue(getRequest,"tag");

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AccountActivity.this,HomeActivity.class);
        startActivity(i);
    }
}
