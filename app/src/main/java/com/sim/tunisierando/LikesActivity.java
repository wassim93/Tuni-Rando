package com.sim.tunisierando;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.likes;
import com.sim.tunisierando.adapters.LikesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {
    public RecyclerView myRecylerView ;
    public List<likes> likesList ;
    public LikesAdapter likesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        String postid= (String)getIntent().getExtras().getString("eventid");
        likesList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.likeslist);
        likesAdapter = new LikesAdapter(likesList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        myRecylerView.setAdapter(likesAdapter);
        getData(postid);
    }





    public void getData(String id){
        final List<likes> mEntries = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(ServerConfig.UrlForServer+"/getlikes/"+id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                //int id, String content, User user, Posts posts, String date
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String username =  jsonObject.getJSONObject("user").getString("_fisrt_name")+" "+ jsonObject.getJSONObject("user").getString("_last_name") ;
                                likes t = new likes(username,
                                                jsonObject.getJSONObject("user").getString("_profile_pic_url")
 );
                                mEntries.add(t);

                            }
                            catch(JSONException e) {

                            }
                        }


                     likesList.addAll(mEntries);

                        likesAdapter.notifyDataSetChanged();
                        for (likes t:likesList){
                            Log.d("TAG",  t.getUsername());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LikesActivity.this, "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ;
        request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(this).addToRequestQueue(request,"pp");

    }

}
