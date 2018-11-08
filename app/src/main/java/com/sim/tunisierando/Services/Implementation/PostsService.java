package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.HomeActivity;
import com.sim.tunisierando.Services.Interfaces.PostsInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aymen on 03/12/2017.
 */

public class PostsService implements PostsInterface {
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    ProgressDialog progressDialog;
    private ArrayList<Posts> mEntries;
    String url = ServerConfig.UrlForServer;
    SharedPreferences preferences;
    private Activity activity;
    public PostsService(Activity activity){
        this.activity= activity;
        preferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    @Override
    public void Add(Posts posts) {
        final JSONObject params = new JSONObject();
        try {

            params.put("content",posts.getContent());
            params.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
            params.put("image",posts.getImagePath());


        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/addPost", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");
                             progressDialog.dismiss();
                            Toast.makeText(activity,
                                    "publié !! ", Toast.LENGTH_LONG).show();


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

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,"rr");

    }

    @Override
    public void Update(Posts posts) {
        final JSONObject params = new JSONObject();
        try {
            params.put("postid",posts.getId());
            params.put("content",posts.getContent());
            params.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
            params.put("image",posts.getImagePath());


        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/UpdatePost", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");
                            progressDialog.dismiss();
                            Intent i = new Intent(activity, HomeActivity.class);
                            activity.startActivity(i);
                            Toast.makeText(activity,
                                    "opération effectué avec succés  !! ", Toast.LENGTH_LONG).show();


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

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,"rr");

    }

    @Override
    public void Delete(Posts posts) {

    }


    @Override
    public void getAll(final  VolleyCallback callback) {

        mEntries = new ArrayList<>();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonArrayRequest request = new JsonArrayRequest(url+"/getAllPosts",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                //int id, String content, String date, User user, int like, int comment, String imagePath
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Posts t = new Posts(
                                        Integer.parseInt(jsonObject.getString("id")),
                                        jsonObject.getString("content"),
                                        jsonObject.getString("date") ,
                                        new User(
                                         Integer.parseInt(jsonObject.getJSONObject("user").getString("id")),
                                        jsonObject.getJSONObject("user").getString("username"),
                                        jsonObject.getJSONObject("user").getString("_profile_pic_url"),
                                        jsonObject.getJSONObject("user").getString("_fisrt_name"),
                                        jsonObject.getJSONObject("user").getString("_last_name")),
                                        Integer.parseInt( jsonObject.getString("likes")),
                                        Integer.parseInt(jsonObject.getString("comments")),
                                        jsonObject.getString("image_path")

                                );
                                mEntries.add(t);

                            }
                            catch(JSONException e) {

                            }
                        }
                        progressDialog.dismiss();
                       callback.onSuccessListPosts(mEntries);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError instanceof NetworkError) {
                         String   message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof ServerError) {
                            String  message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            String   message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ParseError) {
                            String   message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof NoConnectionError) {
                            String   message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof TimeoutError) {
                            String  message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();

                    }

                }){
          /*  @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer "+preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)) );


                return params;
            }*/
        };
        ;
        try {
            Log.d("eeeee",request.getHeaders()+"");
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(request,"pp");



    }

    @Override
    public Posts GetById(Integer integer) {
        return null;
    }
}
