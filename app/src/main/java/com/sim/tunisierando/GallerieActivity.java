package com.sim.tunisierando;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kosalgeek.android.photoutil.MainActivity;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.GallerieImg;
import com.sim.tunisierando.adapters.GallerieAdapter;
import com.sim.tunisierando.fragments.SlideshowDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GallerieActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = ServerConfig.UrlForServer+"/GetPhotos";
    private ArrayList<GallerieImg> images;
    private ProgressDialog pDialog;
    private GallerieAdapter mAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallerie);
        SocketListeners socketListeners = new SocketListeners(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.gallerie_toolbar);
        toolbar.setTitle("Gallerie des Photos");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.gallerie_list);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GallerieAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

         recyclerView.addOnItemTouchListener(new GallerieAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GallerieAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
    }

    private void fetchImages() {

        pDialog.setMessage("Chargement en cours...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                GallerieImg image = new GallerieImg();
                                image.setNom(object.getString("name"));
                                image.setUrl(object.getString("url"));
                                images.add(image);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppSingleton.getInstance(this).addToRequestQueue(req,TAG);
    }
}


