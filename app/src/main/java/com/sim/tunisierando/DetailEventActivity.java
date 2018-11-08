package com.sim.tunisierando;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.robohorse.pagerbullet.PagerBullet;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Network.VolleyService;
import com.sim.tunisierando.Services.Implementation.ParsingEvent;
import com.sim.tunisierando.Services.Implementation.ParsingEventParticipant;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.IResult;
import com.sim.tunisierando.Services.Interfaces.UserInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;
import com.sim.tunisierando.adapters.EventImagesAdapter;
import com.sim.tunisierando.adapters.EventParticipantAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DetailEventActivity extends AppCompatActivity implements View.OnClickListener {
    String depart,arrive;
    ProgressDialog progressDialog;
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    TextView txt_title,txt_date,txt_prix,txt_nbplace,txt_type,txt_difficulte,txt_description,txt_contact,txt_no_participant;
    Button btn_particper, btn_annuler ;
    public Double longitude,latitude,longitudeArr,latitudeArr;
    public int evnetid = 0;
    private Socket socket;
    PagerBullet viewpager;
    Toolbar toolbar;
    RecyclerView participant_list;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    private String TAG = "EventsDetail";
    EventParticipantAdapter eventParticipantAdapter;
    ArrayList<User> listep;
    String titre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SocketListeners socketListeners = new SocketListeners(this);
        txt_title = (TextView) findViewById(R.id.txt_ev_title);
        txt_date = (TextView) findViewById(R.id.txt_ev_date);
        txt_prix = (TextView) findViewById(R.id.txt_ev_prix);
        txt_nbplace = (TextView) findViewById(R.id.txt_ev_nbplace);
        txt_type = (TextView) findViewById(R.id.txt_ev_type);
        txt_difficulte = (TextView) findViewById(R.id.txt_ev_difficulte);
        txt_contact = (TextView) findViewById(R.id.txt_ev_contact);
        txt_description = (TextView) findViewById(R.id.txt_ev_description);
        btn_particper = (Button) findViewById(R.id.particper);
        btn_annuler = (Button) findViewById(R.id.Annuler);
        viewpager = (PagerBullet) findViewById(R.id.viewPager);
        participant_list = (RecyclerView) findViewById(R.id.participantlist);
        toolbar = (Toolbar) findViewById(R.id.ev_toolbar);
        txt_no_participant = (TextView) findViewById(R.id.txt_no_participant);



        btn_particper.setOnClickListener(this);
        btn_annuler.setOnClickListener(this);
        try {
            socket = IO.socket(ServerConfig.UrlForNotificationServer);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        socket.connect();

        Intent intent = getIntent();


        if (intent.hasExtra("obj")) {
            Bundle b = getIntent().getExtras();
            Events e = (Events) b.get("obj");
            evnetid = e.getId();
            txt_title.setText(e.getTitle());
            txt_date.setText("Date :" + e.getDate());
            txt_prix.setText("Prix :" + e.getPrix());
            titre = e.getTitle();
            txt_nbplace.setText("Place Disponible : " + e.getNbrPlace());
            txt_type.setText("Type : " + e.getType());
            txt_difficulte.setText("Difficulté : " + e.getNiveau());
            txt_contact.setText("Contact : " + e.getContact());
            txt_description.setText(e.getDescription());
            ArrayList<String> arrayImg = e.getImages();
            EventImagesAdapter pad = new EventImagesAdapter(this, arrayImg);
            viewpager.setAdapter(pad);
            depart = e.getDepart();
            arrive = e.getArrive();


            Geocoder geoCoder = new Geocoder(this);
            try {
                List<Address> res = geoCoder.getFromLocationName(e.getDepart(), 1);
                longitude = res.get(0).getLongitude();
                latitude = res.get(0).getLatitude();


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                List<Address> res1 = geoCoder.getFromLocationName(e.getArrive(), 1);
                longitudeArr = res1.get(0).getLongitude();
                latitudeArr = res1.get(0).getLatitude();


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng origin = new LatLng(latitude, longitude);
                    LatLng destination = new LatLng(latitudeArr, longitudeArr);
                    DrawRouteMaps.getInstance(getApplicationContext())
                            .draw(origin, destination, googleMap);
                    DrawMarker.getInstance(getApplicationContext()).draw(googleMap, origin, R.drawable.icons8_marker_a, "Départ");
                    DrawMarker.getInstance(getApplicationContext()).draw(googleMap, destination, R.drawable.icons8_marker_b, "Arrivé");

                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(origin)
                            .include(destination).build();
                    Point displaySize = new Point();
                    getWindowManager().getDefaultDisplay().getSize(displaySize);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, displaySize.y, 30));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                }
            });



        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(titre);

        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, this);
        listep = mVolleyService.getParticipantListVolley("GETCALL", ServerConfig.UrlForServer+"/GetEventParticipant/"+evnetid);
        eventParticipantAdapter = new EventParticipantAdapter(this,listep,R.id.participantlist);

        eventParticipantAdapter.notifyDataSetChanged();

        participant_list.setAdapter(eventParticipantAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.particper:
                final JSONObject params = new JSONObject();
                try {
                    // user_id, comment_id,status
                    params.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)));
                    params.put("idevent", evnetid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkparticipate", params,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getString("response").equals("true")){

                                        Toast.makeText(DetailEventActivity.this, "vous avez déja participé à cet evenement",
                                                Toast.LENGTH_LONG).show();
                                    }else {
                                        ParsingEvent parsingEvent = new ParsingEvent();
                                        parsingEvent.participer(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)),evnetid,DetailEventActivity.this);
                                        UserInterface userService = new UserService(DetailEventActivity.this);
                                        userService.getUserByAccessToken(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)), new VolleyCallback() {
                                            @Override
                                            public void onSuccessUser(JSONObject result) {
                                                try {
                                                    socket.emit("setnotificationtosepifiedperson", result.getString("username"), evnetid);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onSuccessListTips(List<TipsAndTricks> result) {

                                            }

                                            @Override
                                            public void onSuccessListPosts(List<Posts> result) {

                                            }

                                            @Override

                                            public void onSuccessListProduct(List<Product> result) {

                                            }

                                            @Override
                                            public void onSuccessListComments(List<Comment> result) {


                                            }

                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

                AppSingleton.getInstance(this).addToRequestQueue(getRequest,"req");


                break;
            case R.id.Annuler:
                final JSONObject paramss = new JSONObject();
                try {
                    // user_id, comment_id,status
                    paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)));
                    paramss.put("idevent", evnetid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/deletepart", paramss,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getString("response").equals("true")){

                                        Toast.makeText(DetailEventActivity.this, "participation annulé",
                                                Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(DetailEventActivity.this, "vous n'avez pas encore participer à cet evenement",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

                getRequestt.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppSingleton.getInstance(this).addToRequestQueue(getRequestt,"req");

                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void initVolleyCallback(){
        mResultCallback = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                if(response.length()>0 && response != null) {
                txt_no_participant.setVisibility(View.INVISIBLE);
                ParsingEventParticipant p = new ParsingEventParticipant();
                listep = p.ArrayParse(response, eventParticipantAdapter);
                eventParticipantAdapter = new EventParticipantAdapter(DetailEventActivity.this,listep,R.id.participantlist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
                participant_list.setLayoutManager(mLayoutManager);
                participant_list.setAdapter(eventParticipantAdapter);
                }else{
                    txt_no_participant.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void notifySuccessJsonobject(String requestType, JSONObject response) {
                try {
                    Log.d(TAG, "Volley JSON get" + response.get("response"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d(TAG, "Volley JSON erreur" +error.toString());
            }
        };

    }

}
