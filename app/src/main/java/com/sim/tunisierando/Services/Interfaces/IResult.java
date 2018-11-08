package com.sim.tunisierando.Services.Interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wassim on 15/11/2017.
 */

public interface IResult {

    public void notifySuccessJsonArray(String requestType,JSONArray response);
    public void notifySuccessJsonobject(String requestType,JSONObject response);
    public void notifyError(String requestType,VolleyError error);
}
