package com.sim.tunisierando.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Network.VolleyService;
import com.sim.tunisierando.Services.Implementation.ParsingThisWeekEvent;
import com.sim.tunisierando.Services.Implementation.ProductService;
import com.sim.tunisierando.Services.Implementation.TipsAndTricksService;
import com.sim.tunisierando.Services.Interfaces.IResult;
import com.sim.tunisierando.Services.Interfaces.ProductInterface;
import com.sim.tunisierando.Services.Interfaces.TipsAndTricksInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;
import com.sim.tunisierando.adapters.EventThisWeekAdapter;
import com.sim.tunisierando.adapters.ProductAdapter;
import com.sim.tunisierando.adapters.TipsAndTricksAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends Fragment {

    ProductInterface productInterface;
    ProductAdapter produitListAdapter;
    RecyclerView myrecycler1;
    ArrayList<Product> listpro;
    //tips & tricks
    private RecyclerView myRecylerView ;
    private List<TipsAndTricks> TipsAndTricksList ;
    TipsAndTricksInterface tipsAndTricksInterface ;
    private com.sim.tunisierando.adapters.TipsAndTricksAdapter TipsAndTricksAdapter;
// event
       RecyclerView recyclerView;
    ArrayList<Events> listev;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    private String TAG = "EventsFragment";
    EventThisWeekAdapter eventListAdapter;
    TextView txt1 ;
    public ArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return  inflater.inflate(R.layout.fragment_articles, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myrecycler1 = (RecyclerView) view.findViewById(R.id.recycler_view1);
        listpro = new ArrayList<Product>();
        productInterface = new ProductService(getActivity());
        produitListAdapter = new ProductAdapter(getContext(),listpro,R.id.recycler_view1);
        myrecycler1.setAdapter(produitListAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        myrecycler1.setLayoutManager(layoutManager);
        myrecycler1.setMinimumWidth(200);
        /********************************************************************/
        TipsAndTricksList =  new ArrayList<>();
        myRecylerView = (RecyclerView) view.findViewById(R.id.recycler_view2);
        TipsAndTricksAdapter = new TipsAndTricksAdapter(TipsAndTricksList,getContext());
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecylerView.setLayoutManager(layoutManager2);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        myRecylerView.setAdapter(TipsAndTricksAdapter);
        TipsAndTricksList.clear();
         tipsAndTricksInterface = new TipsAndTricksService(getActivity());
       /**********************************************/
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view3);
        txt1 = (TextView)view.findViewById(R.id.text2);

    }

    @Override
   public void onResume() {
        super.onResume();
        listpro.clear();
        productInterface.getAll(new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

            @Override
            public void onSuccessListProduct(List<Product> result) {

                listpro.addAll(result);

                produitListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onSuccessListComments(List<Comment> result) {

            }
        });




          TipsAndTricksList.clear();

        tipsAndTricksInterface.GetAll(new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {
                TipsAndTricksList.addAll(result);
                TipsAndTricksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

            @Override

            public void onSuccessListProduct(List<Product> result) {
            }
            public void onSuccessListComments(List<Comment> result) {


            }
        });
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        listev = mVolleyService.getDataVolley("GETCALL", ServerConfig.UrlForServer+"/geteventofthisweek");
        // Inflate the layout for this fragment

        eventListAdapter = new EventThisWeekAdapter(getActivity(),listev,R.id.recycler_view3);

        eventListAdapter.notifyDataSetChanged();


        recyclerView.setAdapter(eventListAdapter);
    }
    void initVolleyCallback(){
        mResultCallback = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                ParsingThisWeekEvent p = new ParsingThisWeekEvent();
                listev = p.ArrayParse(response,eventListAdapter);


                eventListAdapter = new EventThisWeekAdapter(getActivity(),listev,R.id.eventlist);

                if(listev.size()==0){
                    ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
                    params.height=0;
                    recyclerView.setLayoutParams(params);
                    txt1.setText("pas d'événements pour cette semaine");
                }else {
                    LinearLayoutManager layoutManager3
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager3);
                    Log.d("lllllllll",response+"");
                }
                recyclerView.setAdapter(eventListAdapter);
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
