package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;

import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Services.Interfaces.IParsing;
import com.sim.tunisierando.adapters.ProductSuggestionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wassim on 24/12/2017.
 */

public class ParsingProductSuggestion implements IParsing<Product,ProductSuggestionAdapter> {
    @Override
    public ArrayList<Product> ArrayParse(JSONArray array, ProductSuggestionAdapter adapter) {
        ArrayList<Product> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                //int id, String content, String date, User user, int like, int comment, String imagePath
                Product e = new Product();

                e.setId(object.getInt("id"));
                e.setTitle(object.getString("titre"));
                e.setDescription(object.getString("description"));
                e.setContact(object.getString("contact"));
                e.setDate(object.getString("date"));
                e.setPrix(Float.parseFloat(object.getString("prix")));

                ArrayList<String> arrayImg= new ArrayList<>();
                JSONArray arr = object.getJSONArray("images");
                JSONObject ob = null;

                for(int j =0 ; j<arr.length();j++){
                    ob = arr.getJSONObject(j);
                    arrayImg.add(ob.getString("image"));

                }
                e.setImages(arrayImg);
                e.setType(object.getString("type"));
                list.add(e);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }finally {
                adapter.notifyDataSetChanged();
            }

        }
        return  list;
    }

    @Override
    public Product ObjectParse(JSONObject object) {
        return null;
    }

    @Override
    public void participer(String token, int idevent, Activity activity) {

    }
}
