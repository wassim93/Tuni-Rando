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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.HomeActivity;
import com.sim.tunisierando.Services.Interfaces.TipsAndTricksInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TipsAndTricksService implements TipsAndTricksInterface {
    ProgressDialog progressDialog;
    String url = ServerConfig.UrlForServer;
    String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
    SharedPreferences preferences;
    public Activity activity;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String OAUTH_TOKEN= "token";
    private ArrayList<TipsAndTricks> mEntries;
    public TipsAndTricksService(Activity activity){
        this.activity=activity;
        preferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void Add(TipsAndTricks tipsAndTricks) {
        final JSONObject params = new JSONObject();
        try {
            // user_id, comment_id,status

            JSONObject param = new JSONObject();

            params.put("title", tipsAndTricks.getTitle());
            params.put("description", tipsAndTricks.getContent());
            params.put("imagepath", tipsAndTricks.getImagePath());
            params.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/addArticleTips", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");
                            Toast.makeText(activity,
                                    "Article publié  ", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(activity, HomeActivity.class);
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

        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);

    }

    @Override
    public void Update(TipsAndTricks tipsAndTricks) {
        final JSONObject params = new JSONObject();
        try {

            params.put("title", tipsAndTricks.getTitle());
            params.put("description", tipsAndTricks.getContent());
            params.put("imagepath", tipsAndTricks.getImagePath());
            params.put("idtips",tipsAndTricks.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/UpdateArticleTips", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");
                            Toast.makeText(activity,
                                    "Article mis à jour  ", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(activity, HomeActivity.class);
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

        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(getRequest,REQUEST_TAG);

    }

    @Override
    public void Delete(TipsAndTricks tipsAndTricks) {

    }

    @Override
    public void getAll(VolleyCallback callback) {

    }




    @Override
    public void GetAll(final VolleyCallback callback) {
        mEntries = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(url+"/TipsTricksGetAll",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("TAG", jsonArray.toString());
                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                TipsAndTricks t = new TipsAndTricks(Integer.parseInt(jsonObject.getString("id")),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("content") ,
                                        jsonObject.getString("image_url"),
                                        new User(
                                                jsonObject.getJSONObject("user").getString("_fisrt_name"),

                                                jsonObject.getJSONObject("user").getString("_last_name"),
                                        jsonObject.getJSONObject("user").getString("_profile_pic_url"))


                                );

                                mEntries.add(t);

                            }
                            catch(JSONException e) {

                            }
                        }
                        callback.onSuccessListTips(mEntries);
                        for (TipsAndTricks t :mEntries){
                            Log.d("TAG",  t.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(activity, "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ;
        request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(activity).addToRequestQueue(request,REQUEST_TAG);




    }

    @Override
    public TipsAndTricks GetById(Integer integer) {
        return null;
    }
}
