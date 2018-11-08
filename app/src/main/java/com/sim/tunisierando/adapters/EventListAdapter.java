package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.robohorse.pagerbullet.PagerBullet;
import com.sim.tunisierando.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.DetailEventActivity;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.UpdateEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wassim on 13/11/2017.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>{

    private Context context;
    private List<Events> list;
    private int resourceId;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    public static final String OAUTH_TOKEN= "token";

    public EventListAdapter(Context context, List<Events> list, int resourceId){
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
        preferences =context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.event_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.ev_title.setText(list.get(position).getTitle());
        holder.ev_date.setText(list.get(position).getDate());

        ArrayList<String> arrayImg = list.get(position).getImages();

        EventImagesAdapter evad = new EventImagesAdapter(context,arrayImg) ;
        holder.viewPager.setAdapter(evad);

// when item from list is clicked
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailEventActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("obj",list.get(position));
                i.putExtras(bundle);

                context.startActivity(i);


            }
        });


        holder.username.setText(list.get(position).getUser().getUsername());
        Picasso.with(context).load(ServerConfig.UrlForImageLocation+list.get(position).getUser().getProfilePicUrl())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.loader).into(holder.profilPic);


        final JSONObject paramss = new JSONObject();
        try {

            paramss.put("idevent",list.get(position).getId());
            paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkeventuser", paramss,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if(response.getString("response").equals("true")){

                                holder.btn_more.setVisibility(View.VISIBLE);

                            }else {

                                holder.btn_more.setVisibility(View.INVISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());

                    }
                }
        );

        getRequestt.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(context).addToRequestQueue(getRequestt,"rtt");



        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
                // popup.setOnMenuItemClickListener(context);// to implement on click event on items of menu
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modifier :
                                Intent i = new Intent(context, UpdateEvent.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("obj",list.get(position));
                                i.putExtras(bundle);
                                context.startActivity(i);

                                break;
                            case R.id.supprimer :
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(" ");
                                //Setting message manually and performing action on button click
                                builder.setMessage("Voulez vous supprimé cet évènement ?");
                                //This will not allow to close dialogbox until user selects an option
                                builder.setCancelable(false);
                                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/deleteEvent/"+list.get(position).getId(), null,
                                                new Response.Listener<JSONObject>()
                                                {
                                                    @Override
                                                    public void onResponse(JSONObject response) {


                                                        try {
                                                            if(response.getString("response").equals("true")){
                                                                list.remove(position);
                                                                notifyItemRemoved(position);
                                                                notifyItemRangeChanged(position, list.size());


                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener()
                                                {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("Error.Response", error.toString());

                                                    }
                                                }
                                        );

                                        getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                        AppSingleton.getInstance(context).addToRequestQueue(getRequest,"tag");
                                        RequestQueue rq = Volley.newRequestQueue(context);
                                        rq.add(getRequest);

                                    }
                                });
                                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                //alert.setTitle("AlertDialogExample");
                                alert.show();
                                break;
                        }
                        return false;
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_events, popup.getMenu());
                popup.show();
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linear;
        private PagerBullet viewPager;
        private ImageView profilPic;
        private TextView username;
        private Button btn_more;


        private  TextView ev_title;

        private TextView ev_date;


        public ViewHolder(View itemView) {
            super(itemView);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            viewPager = (PagerBullet) itemView.findViewById(R.id.viewPager);
            ev_title = (TextView) itemView.findViewById(R.id.ev_title);
            ev_date = (TextView) itemView.findViewById(R.id.ev_date);
            profilPic =(ImageView)itemView.findViewById(R.id.ev_userpic);
            username = (TextView) itemView.findViewById(R.id.ev_username);
            btn_more = (Button) itemView.findViewById(R.id.ev_btn_more);


        }
    }

}
