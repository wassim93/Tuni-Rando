package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sim.tunisierando.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.DetailEventActivity;
import com.sim.tunisierando.Entities.Events;

import java.util.List;

/**
 * Created by wassim on 23/12/2017.
 */

public class SportifEventAdapter extends RecyclerView.Adapter<SportifEventAdapter.ViewHolder> {

    private Context context;
    private List<Events> list;
    private int resourceId;


    public SportifEventAdapter(Context context, List<Events> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sportif_event_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.sportif_ev_title.setText(list.get(position).getTitle());
        holder.sportif_ev_date.setText(list.get(position).getDate());

        Picasso.with(context).load(ServerConfig.UrlForImageLocation+list.get(position).getImages().get(0))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.loader).into(holder.sportif_ev_img);

        holder.sportif_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailEventActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("obj",list.get(position));
                i.putExtras(bundle);

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView sportif_ev_img;
        private TextView sportif_ev_title;
        private  TextView sportif_ev_date;
        private LinearLayout sportif_linear;


        public ViewHolder(View itemView) {
            super(itemView);
            sportif_linear = (LinearLayout) itemView.findViewById(R.id.sportif_linear);
            sportif_ev_title = (TextView) itemView.findViewById(R.id.sportif_ev_title);
            sportif_ev_date = (TextView) itemView.findViewById(R.id.sportif_ev_date);
            sportif_ev_img =(ImageView)itemView.findViewById(R.id.sportif_ev_img);


        }
    }
}
