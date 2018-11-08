package com.sim.tunisierando.Services.Interfaces;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wassim on 15/11/2017.
 */

public interface IParsing<T,V> {

    public ArrayList<T> ArrayParse(JSONArray array, V adapter);
    public T ObjectParse(JSONObject object);
    public void participer(String token, int idevent, Activity activity);
}
