package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Interfaces.CommentInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CommentService implements CommentInterface {
    private ArrayList<Comment> mEntries;
    String url = ServerConfig.UrlForServer;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String OAUTH_TOKEN= "token";
    SharedPreferences preferences;
    private Activity activity;
    public CommentService(Activity activity){
        this.activity =activity;
        preferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    @Override
    public void Add(Comment comment) {

        final JSONObject params = new JSONObject();
        try {

            params.put("message",comment.getContent());
            params.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
            params.put("postid",comment.getPosts().getId());

        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/AddComment", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");
                        


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
    public void Update(Comment comment) {
        final JSONObject params = new JSONObject();
        try {
            params.put("idcomment",comment.getId());
            params.put("message",comment.getContent());


        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/UpdateComment", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");



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
    public void Delete(Comment comment) {

    }

    @Override
    public void getAll(final  VolleyCallback callback) {



    }

    @Override
    public Comment GetById(Integer integer) {
        return null;
    }

    @Override
    public void GetCommentByEventID(final  VolleyCallback callback, int eventid) {
       final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        mEntries = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(url+"/getcomments/"+eventid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                //int id, String content, User user, Posts posts, String date
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Comment t = new Comment(
                                        Integer.parseInt(jsonObject.getString("id")),
                                        jsonObject.getString("content"),
                                        new User(Integer.parseInt( jsonObject.getJSONObject("user").getString("id")),
                                                jsonObject.getJSONObject("user").getString("username"),
                                                jsonObject.getJSONObject("user").getString("_profile_pic_url"),
                                        jsonObject.getJSONObject("user").getString("_fisrt_name"),
                                        jsonObject.getJSONObject("user").getString("_last_name")),
                                        new Posts(),
                                        jsonObject.getString("date")

                                );
                                mEntries.add(t);

                            }
                            catch(JSONException e) {

                            }
                        }
                        progressDialog.dismiss();
                        callback.onSuccessListComments(mEntries);



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

        AppSingleton.getInstance(activity).addToRequestQueue(request,"pp");
    }

}
