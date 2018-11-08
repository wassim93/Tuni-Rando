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
import com.sim.tunisierando.DetailProduct;
import com.sim.tunisierando.Entities.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wassim on 24/12/2017.
 */

public class ProductSuggestionAdapter extends RecyclerView.Adapter<ProductSuggestionAdapter.ViewHolder> {

    private Context context;
    private List<Product> list;
    private int resourceId;


    public ProductSuggestionAdapter(Context context, List<Product> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.suggestion_product_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.sugg_prod_title.setText(list.get(position).getTitle());

        ArrayList<String> arrayImg = list.get(position).getImages();
        Picasso.with(context).load(ServerConfig.UrlForImageLocation+arrayImg.get(0))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.loader).into(holder.sugg_pro_img);

        holder.sugg_prod_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailProduct.class);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout sugg_prod_lin;
        private ImageView sugg_pro_img;
        private TextView sugg_prod_title;


        public ViewHolder(View itemView) {
            super(itemView);
            sugg_prod_lin = (LinearLayout) itemView.findViewById(R.id.sugg_prod_lin);
            sugg_pro_img = (ImageView) itemView.findViewById(R.id.sugg_pro_img);
            sugg_prod_title = (TextView) itemView.findViewById(R.id.sugg_prod_title);


        }
    }
}
