package com.sim.tunisierando.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sim.tunisierando.R;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.Network.VolleyService;
import com.sim.tunisierando.Services.Implementation.ParsingEvent;
import com.sim.tunisierando.Services.Implementation.ParsingSportifEvent;
import com.sim.tunisierando.Services.Implementation.ParsingTodayEvent;
import com.sim.tunisierando.Services.Interfaces.IResult;
import com.sim.tunisierando.adapters.EventListAdapter;
import com.sim.tunisierando.adapters.SportifEventAdapter;
import com.sim.tunisierando.adapters.TodayEventAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListEventFragment extends Fragment {

    RecyclerView recyclerView,today_eventlist,sportif_eventlist;
    ArrayList<Events> listev,listevtoday,listsportifevent;

    TextView no_events,no_eventstoday,no_sportif_events;
    IResult mResultCallback1 = null;
    VolleyService mVolleyService1;
    IResult mResultCallback2 = null;
    VolleyService mVolleyService2;
    IResult mResultCallback3 = null;
    VolleyService mVolleyService3;
    private String TAG = "EventsFragment";
    EventListAdapter eventListAdapter;
    TodayEventAdapter todayEventAdapter;
    SportifEventAdapter sportifEventAdapter;
    ProgressDialog pDialog;





    public ListEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_event, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.eventlist);
        today_eventlist =(RecyclerView)v.findViewById(R.id.today_eventlist);
        sportif_eventlist =(RecyclerView)v.findViewById(R.id.sportif_eventlist);
        no_eventstoday = (TextView)v.findViewById(R.id.no_eventstoday);
        no_events = (TextView) v.findViewById(R.id.no_events);
        no_sportif_events = (TextView) v.findViewById(R.id.no_sportif_events);

        return v;
    }



    /* public int getDate(){
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String formatted = format1.format(Calendar.getInstance().getTime());
        String[] out = formatted.split("/");

        return Integer.parseInt(out[0]);


    }*/

   /* public ArrayList<Events> getEventsByDate(int date){

        ArrayList<Events> ev_list = new ArrayList<>();
        for (Events e :listev){
            String[] out = e.getDate().split(" ");
            String[] out1 = out[1].split("/");
            if(Integer.parseInt(out1[0]) == date ){
                ev_list.add(e);
            }
        }
        return ev_list;
    }*/

    void initVolleyCallback1(){
        mResultCallback1 = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                if(response.length()>0 && response != null) {
                    no_eventstoday.setVisibility(View.INVISIBLE);
                    ParsingTodayEvent p = new ParsingTodayEvent();
                    listevtoday = p.ArrayParse(response, todayEventAdapter);
                    todayEventAdapter = new TodayEventAdapter(getActivity(), listevtoday, R.id.today_eventlist);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    today_eventlist.setLayoutManager(mLayoutManager);
                    today_eventlist.setAdapter(todayEventAdapter);
                }else{
                    no_eventstoday.setVisibility(View.VISIBLE);

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

    void initVolleyCallback3(){
        mResultCallback3 = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                if(response.length()>0 && response != null) {
                    no_sportif_events.setVisibility(View.INVISIBLE);
                    ParsingSportifEvent p = new ParsingSportifEvent();
                    listsportifevent = p.ArrayParse(response, sportifEventAdapter);
                    sportifEventAdapter = new SportifEventAdapter(getActivity(), listsportifevent, R.id.sportif_eventlist);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    sportif_eventlist.setLayoutManager(mLayoutManager);
                    sportif_eventlist.setAdapter(sportifEventAdapter);
                }else{
                    no_sportif_events.setVisibility(View.VISIBLE);

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

    void initVolleyCallback2(){
        mResultCallback2 = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                if(response.length()>0 && response != null) {
                    no_events.setVisibility(View.INVISIBLE);
                    ParsingEvent p = new ParsingEvent();
                    listev = p.ArrayParse(response, eventListAdapter);
                    eventListAdapter = new EventListAdapter(getActivity(), listev, R.id.eventlist);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(eventListAdapter);
                }else{
                    no_events.setVisibility(View.VISIBLE);
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




    @Override
    public void onResume() {
        super.onResume();

        initVolleyCallback1();
    /// today liist
        mVolleyService1 = new VolleyService(mResultCallback1, getContext());
        listevtoday = mVolleyService1.getTodayEventDataVolley("GETCALL", ServerConfig.UrlForServer+"/GetTodayEvent");

        todayEventAdapter = new TodayEventAdapter(getActivity(),listevtoday,R.id.today_eventlist);

        todayEventAdapter.notifyDataSetChanged();

        today_eventlist.setAdapter(todayEventAdapter);



        initVolleyCallback3();
        /// suggestion liist
        mVolleyService3 = new VolleyService(mResultCallback3, getContext());
        listsportifevent = mVolleyService3.getSportifEventDataVolley("GETCALL", ServerConfig.UrlForServer+"/GetSportifEvent");

        sportifEventAdapter = new SportifEventAdapter(getActivity(),listsportifevent,R.id.sportif_eventlist);

        sportifEventAdapter.notifyDataSetChanged();

        sportif_eventlist.setAdapter(sportifEventAdapter);

        // all events
        initVolleyCallback2();
        mVolleyService2 = new VolleyService(mResultCallback2, getContext());
        listev = mVolleyService2.getDataVolley("GETCALL", ServerConfig.UrlForServer+"/GetAllEvent");
        eventListAdapter = new EventListAdapter(getActivity(),listev,R.id.eventlist);

        eventListAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(eventListAdapter);





    }
}







