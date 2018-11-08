package com.sim.tunisierando.Services.Implementation;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Services.Interfaces.ProductInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wassim on 04/12/2017.
 */

public class ProductService implements ProductInterface {
    private ArrayList<Product> mEntries;
 public Activity activity;

    public ProductService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void Add(Product product) {

    }

    @Override
    public void Update(Product product) {

    }

    @Override
    public void Delete(Product product) {

    }

    @Override
    public void getAll(final VolleyCallback callback) {
        mEntries = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(ServerConfig.UrlForServer+"/GetAllProduct",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
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
                                mEntries.add(e);


                            }
                            catch(JSONException e) {

                            }
                        }
                        callback.onSuccessListProduct(mEntries);

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

    @Override
    public Product GetById(Integer integer) {
        return null;
    }
}
