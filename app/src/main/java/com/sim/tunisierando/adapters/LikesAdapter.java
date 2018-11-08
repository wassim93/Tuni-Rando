package com.sim.tunisierando.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sim.tunisierando.R;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.likes;

import java.util.List;

/**
 * Created by Aymen on 26/12/2017.
 */

public class LikesAdapter  extends RecyclerView.Adapter<LikesAdapter.MyViewHolder> {
    private List<likes> likesList;
    private AppCompatActivity context;



    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profilepic;


        public MyViewHolder(View view) {
            super(view);

            username = (TextView) view.findViewById(R.id.username);
            profilepic = (ImageView) view.findViewById(R.id.profilepic);





        }
    }
    public LikesAdapter(List<likes>CommentsList,@NonNull AppCompatActivity context) {
        this.context = context;
        this.likesList = CommentsList;


    }

    @Override
    public int getItemCount() {
        return likesList.size();
    }
    @Override
    public LikesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_item, parent, false);



        return new LikesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LikesAdapter.MyViewHolder holder, final int position) {
        final likes l = likesList.get(position);

        holder.username.setText(l.getUsername() );


        Picasso.with(context).load(ServerConfig.UrlForImageLocation+l.getImagePath()) .placeholder(R.mipmap.ic_launcher).into(holder.profilepic);


    }



}