package com.sim.tunisierando.Network;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Interfaces.IResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wassim on 15/11/2017.
 */

public class VolleyService {
    IResult mResultCallback = null;
    Context mContext;
    ArrayList<Events> listev;
    ArrayList<User> ls_event_participant;

    ArrayList<Product> listprod,list_pro_sugg;
    ProgressDialog pDialog;

    public VolleyService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void postDataVolley(final String requestType, String url,JSONObject sendObj){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            pDialog = new ProgressDialog(mContext);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();

            JsonObjectRequest jsonObj = new JsonObjectRequest (Request.Method.POST, url, sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null) {
                        mResultCallback.notifySuccessJsonobject(requestType, response);
                        pDialog.dismiss();



                }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                    { mResultCallback.notifyError(requestType, error);
                    pDialog.dismiss();}

                }
            }
            );

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }

    public ArrayList<Events> getDataVolley(final String requestType, String url){
        try {
            listev = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            //pDialog = new ProgressDialog(mContext);
            // Showing progress dialog before making http request
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            JsonArrayRequest j = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccessJsonArray(requestType,response);
                   // pDialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                    //pDialog.dismiss();

                }
            });
            j.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(j);

        }catch(Exception e){

        }
        return listev;
    }


    public ArrayList<Events> getTodayEventDataVolley(final String requestType, String url){
        try {
            listev = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            //pDialog = new ProgressDialog(mContext);
             //Showing progress dialog before making http request
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            JsonArrayRequest j = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccessJsonArray(requestType,response);
                   // pDialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                   // pDialog.dismiss();

                }
            });
            j.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(j);

        }catch(Exception e){

        }
        return listev;
    }





    public ArrayList<Events> getSportifEventDataVolley(final String requestType, String url){
        try {
            listev = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            //pDialog = new ProgressDialog(mContext);
            //Showing progress dialog before making http request
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            JsonArrayRequest j = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccessJsonArray(requestType,response);
                    // pDialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                    // pDialog.dismiss();

                }
            });
            j.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(j);

        }catch(Exception e){

        }
        return listev;
    }


    public ArrayList<User> getParticipantListVolley(final String requestType, String url){
        try {
            ls_event_participant = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            //pDialog = new ProgressDialog(mContext);
            // Showing progress dialog before making http request
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            JsonArrayRequest j = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccessJsonArray(requestType,response);
                    //pDialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                   // pDialog.dismiss();

                }
            });
            j.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(j);

        }catch(Exception e){

        }
        return ls_event_participant;
    }


    public ArrayList<Product> getProdDataVolley(final String requestType, String url){
        try {
            listprod = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            pDialog = new ProgressDialog(mContext);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();

            JsonArrayRequest j = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccessJsonArray(requestType,response);
                    pDialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                    pDialog.dismiss();

                }
            });

            queue.add(j);

        }catch(Exception e){

        }
        return listprod;
    }


    public ArrayList<Product> getProductSuggestionDataVolley(final String requestType, String url){
        try {
            list_pro_sugg = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            //pDialog = new ProgressDialog(mContext);
            //Showing progress dialog before making http request
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            JsonArrayRequest j = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccessJsonArray(requestType,response);
                    // pDialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                    // pDialog.dismiss();

                }
            });
            j.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(j);

        }catch(Exception e){

        }
        return list_pro_sugg;
    }


}
