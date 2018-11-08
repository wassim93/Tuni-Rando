package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.HomeActivity;
import com.sim.tunisierando.LoginActivity;
import com.sim.tunisierando.ReActivateActivity;
import com.sim.tunisierando.Services.Interfaces.UserInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class UserService  implements UserInterface {
    ProgressDialog progressDialog;
    String url = ServerConfig.UrlForServer;
    String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
    SharedPreferences preferences;
    public  Activity activity;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String OAUTH_TOKEN= "token";
    User u ;

    public UserService(Activity activity){
        this.activity=activity;
        preferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);



    }

    public void SavePreferences(String value,String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void Add(final User u) {

        final JSONObject params = new JSONObject();
        try {
            // user_id, comment_id,status

            JSONObject param = new JSONObject();
            param.put("first",u.getPlainPassword().first);
            param.put("second",u.getPlainPassword().second);
            params.put("username", u.username);
            params.put("email", u.email);
            params.put("plainPassword", param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/register", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.getString("status").equals("User created")){
                                Toast.makeText(activity,
                                        "opération effectué avec succés ", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(activity,LoginActivity.class);

                                activity.startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();

                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);

    }

    @Override
    public void Update(User u) {
        final JSONObject params = new JSONObject();
        try {
            // user_id, comment_id,status
            params.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
            params.put("image", u.getProfilePicUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/UpdateUser", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        progressDialog.hide();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);

    }

    @Override
    public void Delete(User T) {

    }

    @Override
    public void getAll(VolleyCallback callback) {

    }




    @Override
    public User GetById(Integer integer) {
        final JSONObject params = new JSONObject();
        try {

            params.put("id",integer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/GetUserById", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                 try{
                            u.setEmail(response.getString("email"));
                        }catch (JSONException e){

                        }
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

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);

        return u;
    }

    @Override
    public void ActivateAccount(String email, String code) {
        final JSONObject params = new JSONObject();
        try {

            params.put("email",email);
            params.put("token",code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veulliez patienter..");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.PUT, url+"/ActivateAccount", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                                Log.d("response", response.getString("response")+"");

                                if(response.getString("response").equals("Activated")){
                                    Toast.makeText(activity,
                                            "compte activé ", Toast.LENGTH_LONG).show();
                                   Intent i = new Intent(activity, LoginActivity.class);
                                    activity.startActivity(i);
                                }else {
                                    Toast.makeText(activity,
                                            "Code incorrect ", Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(activity,
                                ""+  error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.hide();
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);

    }

    @Override
    public void Login(final String email, final String password) {
        Log.d("logpass",email+""+password);
        final JSONObject params = new JSONObject();
        try {

            params.put("username",email);
            params.put("password",password);

        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Connexion en cours...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/auth", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");

                            if(response.getString("response").equals("User Authenticated")){
                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"/oauth/v2/token?" +
                                        "grant_type=password&" +
                                        "client_id=1_3bcbxd9e24g0gk4swg0kwgcwg4o8k8g4g888kwc44gcc0gwwk4&" +
                                        "client_secret=4ok2x70rlfokc8g0wws8c8kwcokw80k44sg48goc0ok4w0so0k&" +
                                        "username=" +email+
                                        "&password="+password, params,
                                        new Response.Listener<JSONObject>()
                                        {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    SavePreferences(response.getString("access_token"),OAUTH_TOKEN);

                                                    Intent i = new Intent(activity,HomeActivity.class);
                                                  
                                                    activity.startActivity(i);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                progressDialog.hide();

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

                                request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                AppSingleton.getInstance(activity).addToRequestQueue(request,REQUEST_TAG);
                            }else if (response.getString("response").equals("Account disabled")){
                                Toast.makeText(activity,
                                        "votre compte est désactivé  ", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(activity, ReActivateActivity.class);
                                activity.startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(activity,
                                "Login ou mot de passe incorrect !   ", Toast.LENGTH_LONG).show();
                        progressDialog.hide();
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);
    }

    @Override
    public void SetBackgroundImage(User u) {
        final JSONObject params = new JSONObject();
        try {
            // user_id, comment_id,status
            params.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
            params.put("image", u.getBackgroundPicUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/UpdateUserBackgroundImage", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        progressDialog.hide();

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

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);
    }

    @Override
    public void getUserByAccessToken(String Token,final VolleyCallback callback) {

        final JSONObject params = new JSONObject();
        try {

            params.put("token",Token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/getUserByToken", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                           /* Toast.makeText(activity,
                                    ""+ response.getString("_fisrt_name"), Toast.LENGTH_LONG).show();*/


                             callback.onSuccessUser(response);



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

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);



    }

    @Override
    public void Logout() {

                     preferences.edit().remove(OAUTH_TOKEN).commit();
                           Intent intent = new Intent(activity,LoginActivity.class);
                           activity.startActivity(intent);



    }


}

