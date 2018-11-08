package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sim.tunisierando.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.AccountActivity;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.User;

import java.util.List;

/**
 * Created by wassim on 22/12/2017.
 */

public class EventParticipantAdapter extends RecyclerView.Adapter<EventParticipantAdapter.ViewHolder> {

    private Context context;
    private List<User> list;
    private int resourceId;

    public EventParticipantAdapter(Context context, List<User> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.participant_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.participant_name.setText(list.get(position).getUsername());
        Picasso.with(context).load(ServerConfig.UrlForImageLocation+list.get(position).getProfilePicUrl())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.loader).into(holder.participant_profile_pic);
        holder.btn_show_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(context, AccountActivity.class);
                i.putExtra("iduser",list.get(position).getId());
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView participant_profile_pic;
        private TextView participant_name;
        private ImageButton btn_show_profile;



        public ViewHolder(View itemView) {
            super(itemView);
            participant_profile_pic =(ImageView)itemView.findViewById(R.id.participant_profile_pic);
            participant_name = (TextView) itemView.findViewById(R.id.participant_name);
            btn_show_profile = (ImageButton) itemView.findViewById(R.id.btn_show_profile);


        }
    }
}
