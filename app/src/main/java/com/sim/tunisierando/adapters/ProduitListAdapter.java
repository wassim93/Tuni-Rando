package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.sim.tunisierando.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.FilterShop;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.DetailProduct;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.UpdateProductActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wassim on 04/12/2017.
 */

public class ProduitListAdapter extends RecyclerView.Adapter<ProduitListAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Product> list,filterlist;
    private int resourceId;
    FilterShop myfilter;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    public static final String OAUTH_TOKEN= "token";

    public void setList(List<Product> list) {
        this.list = list;
    }

    public ProduitListAdapter(Context context, List<Product> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.filterlist = list;
        this.resourceId = resourceId;
        preferences =context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.pro_title.setText(list.get(position).getTitle());
        holder.pro_prix.setText(String.valueOf(list.get(position).getPrix())+" DT");

        ArrayList<String> arrayImg = list.get(position).getImages();
        Picasso.with(context).load(ServerConfig.UrlForImageLocation+arrayImg.get(0))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.loader).into(holder.pro_img);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailProduct.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("obj",list.get(position));
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });



        final JSONObject paramss = new JSONObject();
        try {

            paramss.put("idprod",list.get(position).getId());
            paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkproductuser", paramss,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if(response.getString("response").equals("true")){

                                holder.prod_btn_more.setVisibility(View.VISIBLE);

                            }else {

                                holder.prod_btn_more.setVisibility(View.INVISIBLE);
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



        holder.prod_btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
                // popup.setOnMenuItemClickListener(context);// to implement on click event on items of menu
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modifier :
                                Intent i = new Intent(context, UpdateProductActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("obj",list.get(position));
                                i.putExtras(bundle);
                                context.startActivity(i);

                                break;
                            case R.id.supprimer :
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(" ");
                                //Setting message manually and performing action on button click
                                builder.setMessage("Voulez vous supprimé ce produit ?");
                                //This will not allow to close dialogbox until user selects an option
                                builder.setCancelable(false);
                                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/deleteProduct/"+list.get(position).getId(), null,
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
                inflater.inflate(R.menu.menu_prod, popup.getMenu());
                popup.show();
            }
        });



    }

    public void setProducts(ArrayList<Product> filteredProducts)
    {
        this.list=filteredProducts;
    }
    @Override
    public Filter getFilter() {
        if(myfilter==null)
        {
            myfilter=new FilterShop(filterlist,this);
        }
        return myfilter;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout pro_linear;
        private ImageView pro_img;
        private TextView pro_title;
        private TextView pro_prix;
        private CardView item;
        private Button prod_btn_more;



        public ViewHolder(View itemView) {
            super(itemView);
            item = (CardView) itemView.findViewById(R.id.pro_item);
            pro_linear = (RelativeLayout) itemView.findViewById(R.id.pro_linear);
            pro_img = (ImageView) itemView.findViewById(R.id.pro_img);
            pro_title = (TextView) itemView.findViewById(R.id.prod_title);
            pro_prix = (TextView) itemView.findViewById(R.id.prod_prix);
            prod_btn_more = (Button) itemView.findViewById(R.id.prod_btn_more);



        }
    }
}
