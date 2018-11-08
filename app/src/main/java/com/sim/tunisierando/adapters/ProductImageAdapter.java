package com.sim.tunisierando.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sim.tunisierando.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.ServerConfig;

import java.util.ArrayList;

/**
 * Created by wassim on 12/12/2017.
 */

public class ProductImageAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> bitmapArray;

    public ProductImageAdapter(Context mContext, ArrayList<String> bitmapArray) {
        this.mContext = mContext;
        this.bitmapArray = bitmapArray;
    }

    @Override
    public int getCount() {
        return bitmapArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView img = new ImageView(mContext);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(mContext).load(ServerConfig.UrlForImageLocation+bitmapArray.get(position))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.mipmap.ic_launcher_round).into(img);


        container.addView(img,0);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }
}
