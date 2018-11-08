package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;

import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Interfaces.IParsing;
import com.sim.tunisierando.adapters.SportifEventAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wassim on 23/12/2017.
 */

public class ParsingSportifEvent implements IParsing<Events,SportifEventAdapter> {
    @Override
    public ArrayList<Events> ArrayParse(JSONArray array, SportifEventAdapter adapter) {
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
                u.setUsername(jsonObject.getString("username"));
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
        return null;
    }

    @Override
    public void participer(String token, int idevent, Activity activity) {

    }
}
