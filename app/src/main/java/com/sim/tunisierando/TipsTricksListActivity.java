package com.sim.tunisierando;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Services.Implementation.TipsAndTricksService;
import com.sim.tunisierando.Services.Interfaces.TipsAndTricksInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;
import com.sim.tunisierando.adapters.TipsAndTricksAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TipsTricksListActivity extends AppCompatActivity {

    private RecyclerView myRecylerView ;
    private List<TipsAndTricks> TipsAndTricksList ;
    private TipsAndTricksAdapter TipsAndTricksAdapter;
    FloatingActionButton addtips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_tricks_list);
        addtips = (FloatingActionButton)findViewById(R.id.addtips);
        TipsAndTricksList =  new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.recycler_view);
        TipsAndTricksAdapter = new TipsAndTricksAdapter(TipsAndTricksList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        myRecylerView.setAdapter(TipsAndTricksAdapter);
        TipsAndTricksList.clear();
        TipsAndTricksInterface tipsAndTricksInterface = new TipsAndTricksService(TipsTricksListActivity.this);
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

      addtips.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent(TipsTricksListActivity.this,TipsandTricksActivity.class);
              startActivity(i);
          }
      });
    }

}
