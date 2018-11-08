package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.R;
import com.sim.tunisierando.TipsTricksDetailsActivity;
import com.sim.tunisierando.UpdateTipsTricksActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class TipsAndTricksAdapter extends RecyclerView.Adapter<TipsAndTricksAdapter.MyViewHolder> {
    private List<TipsAndTricks> TipsAndTricksList;
    private Context context;
    SharedPreferences preferences;
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public RelativeLayout container;
        public Button settings;
        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);

            imageView= (ImageView) view.findViewById(R.id.imagetips);

            container = (RelativeLayout) view.findViewById(R.id.contain);
           settings = (Button) view.findViewById(R.id.stt);

        }
    }
    public TipsAndTricksAdapter(List<TipsAndTricks>TipsAndTricksList,@NonNull Context context) {
        this.context = context;
        this.TipsAndTricksList = TipsAndTricksList;
        preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    @Override
    public int getItemCount() {
        return TipsAndTricksList.size();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tipstricks_item, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TipsAndTricks tipsAndTricks = TipsAndTricksList.get(position);
        holder.title.setText(tipsAndTricks.getTitle());
if(tipsAndTricks.getImagePath().equals("null")){
    Picasso.with(context).load(R.drawable.jum) .placeholder(R.drawable.loader).into(holder.imageView);
   /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
    layoutParams.gravity= Gravity.CENTER;
    layoutParams.setMargins(0,23,0,0);
    holder.imageView.setLayoutParams(layoutParams);*/
}else{
    Picasso.with(context).load(ServerConfig.UrlForImageLocation+tipsAndTricks.getImagePath()) .placeholder(R.drawable.loader).into(holder.imageView);

}
        final JSONObject params = new JSONObject();
        try {

            params.put("idarticle",tipsAndTricks.getId());
            params.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/Checktips", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");

                            if(response.getString("response").equals("true")){

                                holder.settings.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pick, 0, 0);

                            }else {
                                holder.settings.setEnabled(false);
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

        AppSingleton.getInstance(context).addToRequestQueue(getRequest,"rtt");

        holder.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
                // popup.setOnMenuItemClickListener(context);// to implement on click event on items of menu
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modifier :
                                Intent i = new Intent(context, UpdateTipsTricksActivity.class);
                                i.putExtra("idtips",tipsAndTricks.getId());
                                context.startActivity(i);
                                break;
                            case R.id.supprimer :
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);


                                builder.setTitle(" ");

                                //Setting message manually and performing action on button click
                                builder.setMessage("Voulez vous supprimé cet article ?");
                                //This will not allow to close dialogbox until user selects an option
                                builder.setCancelable(false);
                                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/Removetips/"+tipsAndTricks.getId(), null,
                                                new Response.Listener<JSONObject>()
                                                {
                                                    @Override
                                                    public void onResponse(JSONObject response) {


                                                        try {
                                                            if(response.getString("response").equals("true")){
                                                                notifyItemRemoved(position);
                                                                notifyItemChanged(position,TipsAndTricksList.size());
                                                                notifyDataSetChanged();
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
                                        //  Action for 'NO' Button

                                        dialog.cancel();
                                    }
                                });

                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                //alert.setTitle("AlertDialogExample");
                                alert.show();
                                break;
                        }
                        return false;
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_posts, popup.getMenu());
                popup.show();
            }
        });




          holder.container.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i = new Intent(context, TipsTricksDetailsActivity.class);
                  i.putExtra("object",tipsAndTricks);
                  context.startActivity(i);
              }
          });
    }



}