package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Interfaces.IParsing;
import com.sim.tunisierando.adapters.EventListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParsingEvent implements IParsing<Events,EventListAdapter> {
    ProgressDialog progressDialog;
    String url = ServerConfig.UrlForServer;
    public static final String OAUTH_TOKEN= "token";
    String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
    @Override
    public ArrayList<Events> ArrayParse(JSONArray array, EventListAdapter adapter) {
        ArrayList<Events> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = array.getJSONObject(i);
                Events e  = new Events();
                e.setId(object.getInt("id"));
                e.setTitle(object.getString("title"));
                e.setDescription(object.getString("description"));
                e.setContact(object.getString("contact"));
                e.setDate(object.getString("date"));
                e.setPrix(Float.parseFloat(object.getString("prix")));

                ArrayList<String> arrayImg= new ArrayList<>();
                 JSONArray arr = object.getJSONArray("image");
                JSONObject ob = null;

                for(int j =0 ; j<arr.length();j++){
                        ob = arr.getJSONObject(j);
                        arrayImg.add(ob.getString("image"));

                    }
                e.setImages(arrayImg);

                User u = new User();
                JSONObject jsonObject = object.getJSONObject("user");
                u.setId(Integer.parseInt(jsonObject.getString("id")));
                u.setUsername(jsonObject.getString("_fisrt_name")+" "+jsonObject.getString("_last_name"));
                u.setEmail(jsonObject.getString("email"));
                u.setProfilePicUrl(jsonObject.getString("_profile_pic_url"));
                e.setUser(u);
                e.setDepart(object.getString("point_depart"));
                e.setArrive(object.getString("point_arrive"));
                e.setType(object.getString("type"));
                e.setNiveau(object.getString("niveau"));
                e.setNbrPlace(object.getInt("_nbr_places"));
                list.add(e);
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                adapter.notifyDataSetChanged();
            }

        }
        return  list;
    }


    @Override
    public Events ObjectParse(JSONObject object) {
        Events e  = new Events();
        try {
            e.setId(object.getInt("id"));
            e.setTitle(object.getString("title"));
            e.setDescription(object.getString("description"));
            e.setContact(object.getString("contact"));
            e.setDate(object.getString("date"));
            e.setPrix(Float.parseFloat(object.getString("prix")));

            ArrayList<String> arrayImg= new ArrayList<>();
            JSONArray arr = object.getJSONArray("image");
            JSONObject ob = null;

            for(int j =0 ; j<arr.length();j++){
                ob = arr.getJSONObject(j);
                arrayImg.add(ob.getString("image"));

            }
            e.setImages(arrayImg);
            e.setDepart(object.getString("point_depart"));
            e.setArrive(object.getString("point_arrive"));
            e.setType(object.getString("type"));
            e.setNiveau(object.getString("niveau"));
            e.setNbrPlace(object.getInt("_nbr_places"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return e;

    }

    @Override
    public void participer(String token, int idevent, Activity activity) {
        final JSONObject params = new JSONObject();
        try {
            // user_id, comment_id,status
            params.put("token",token);
            params.put("idevent", idevent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Veuiller patienter...");
        progressDialog.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url+"/participate", params,
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


}
