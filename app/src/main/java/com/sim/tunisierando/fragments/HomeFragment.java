package com.sim.tunisierando.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sim.tunisierando.R;
import com.sim.tunisierando.AddPostActivity;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Services.Implementation.PostsService;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;
import com.sim.tunisierando.adapters.PostsAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView myRecylerView ;
    private List<Posts> PostsList ;
    private PostsAdapter postsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout ;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout) ;
        FloatingActionButton addPost = (FloatingActionButton)view.findViewById(R.id.addPost);
       PostsList =  new ArrayList<>();
        myRecylerView = (RecyclerView) view.findViewById(R.id.postlist);
        postsAdapter = new PostsAdapter(PostsList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());


        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        myRecylerView.setAdapter(postsAdapter);

        return  view ;
    }

    void refreshItems() {
        // Load items
        // ...
        PostsList.clear();
        PostsService postsService = new PostsService(getActivity());

        postsService.getAll(new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {
                PostsList.addAll(result);
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccessListProduct(List<Product> result) {

            }

            @Override
            public void onSuccessListComments(List<Comment> result) {

            }
        });
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void onResume() {

        super.onResume();



        PostsList.clear();
        PostsService postsService = new PostsService(getActivity());


        postsService.getAll(new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {
                PostsList.addAll(result);
                myRecylerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onSuccessListProduct(List<Product> result) {

            }

            @Override
            public void onSuccessListComments(List<Comment> result) {

            }
        });

    }


}
