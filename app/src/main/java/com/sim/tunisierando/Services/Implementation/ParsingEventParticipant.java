package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;

import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Interfaces.IParsing;
import com.sim.tunisierando.adapters.EventParticipantAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wassim on 22/12/2017.
 */

public class ParsingEventParticipant implements IParsing<User,EventParticipantAdapter> {
    public static final String OAUTH_TOKEN= "token";

    @Override
    public ArrayList<User> ArrayParse(JSONArray array, EventParticipantAdapter adapter) {
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = array.getJSONObject(i);
                User u = new User();
                u.setId(Integer.parseInt(object.getString("id")));
                u.setUsername(object.getString("_fisrt_name")+" "+object.getString("_last_name"));
                u.setEmail(object.getString("email"));
                u.setProfilePicUrl(object.getString("_profile_pic_url"));
                list.add(u);
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                adapter.notifyDataSetChanged();
            }

        }
        return  list;
    }



    @Override
    public User ObjectParse(JSONObject object) {
        return null;
    }

    @Override
    public void participer(String token, int idevent, Activity activity) {

    }
}
