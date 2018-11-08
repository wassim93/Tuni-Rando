package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sim.tunisierando.Configuration.FilterShop;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.DetailProduct;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Aymen on 21/12/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>  {

    private Context context;
    private List<Product> list,filterlist;
    private int resourceId;
    FilterShop myfilter;

    public void setList(List<Product> list) {
        this.list = list;
    }

    public ProductAdapter(Context context, List<Product> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.filterlist = list;
        this.resourceId = resourceId;
    }



    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_item2,parent,false);
        ProductAdapter.ViewHolder viewHolder = new ProductAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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



    }



    public void setProducts(ArrayList<Product> filteredProducts)
    {
        this.list=filteredProducts;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout pro_linear;
        private ImageView pro_img;


        private TextView pro_title;

        private TextView pro_prix;
        private CardView item;


        public ViewHolder(View itemView) {
            super(itemView);
            item = (CardView) itemView.findViewById(R.id.pro_item);
            pro_linear = (LinearLayout) itemView.findViewById(R.id.pro_linear);
            pro_img = (ImageView) itemView.findViewById(R.id.pro_img);
            pro_title = (TextView) itemView.findViewById(R.id.prod_title);
            pro_prix = (TextView) itemView.findViewById(R.id.prod_prix);


        }
    }
}
