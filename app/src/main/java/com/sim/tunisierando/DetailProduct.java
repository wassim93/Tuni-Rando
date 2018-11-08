package com.sim.tunisierando;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.robohorse.pagerbullet.PagerBullet;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Network.VolleyService;
import com.sim.tunisierando.Services.Implementation.ParsingProductSuggestion;
import com.sim.tunisierando.Services.Interfaces.IResult;
import com.sim.tunisierando.adapters.ProductImageAdapter;
import com.sim.tunisierando.adapters.ProductSuggestionAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailProduct extends AppCompatActivity {
    Toolbar toolbar;
    TextView txt_title,txt_prix,txt_type,txt_description,txt_contact;
    PagerBullet viewpager;
    RecyclerView prod_suggestionlist;
    ProductSuggestionAdapter productSuggestionAdapter;
    ArrayList<Product> list_sugg_pro;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    String type,title;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        SocketListeners socketListeners = new SocketListeners(this);

        txt_title = (TextView) findViewById(R.id.pro_title);
        txt_prix = (TextView) findViewById(R.id.pro_prix);
        txt_type = (TextView) findViewById(R.id.pro_type);
        txt_contact = (TextView) findViewById(R.id.pro_contact);
        txt_description = (TextView) findViewById(R.id.pro_description);
        viewpager = (PagerBullet)findViewById(R.id.viewPager);
        prod_suggestionlist = (RecyclerView)findViewById(R.id.prod_suggestionlist);


        Intent intent = getIntent();
        if(intent.hasExtra("obj")){
            Bundle b = getIntent().getExtras();
            Product e  = (Product) b.get("obj");
            txt_title.setText(e.getTitle());
            txt_prix.setText("Prix :"+e.getPrix());
            txt_type.setText("Type : "+e.getType());
            type = e.getType();
            title = e.getTitle();
            txt_contact.setText("Contact : "+e.getContact());
            txt_description.setText(e.getDescription());
            ArrayList<String> arrayImg = e.getImages();
            ProductImageAdapter pad = new ProductImageAdapter(this,arrayImg) ;
            viewpager.setAdapter(pad);

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        initVolleyCallback();
        /// today liist
        mVolleyService = new VolleyService(mResultCallback,this);
        list_sugg_pro = mVolleyService.getProductSuggestionDataVolley("GETCALL", ServerConfig.UrlForServer+"/GetSuggestionProduct/"+type);
        productSuggestionAdapter = new ProductSuggestionAdapter(this,list_sugg_pro, R.id.prod_suggestionlist);

        productSuggestionAdapter.notifyDataSetChanged();

        prod_suggestionlist.setAdapter(productSuggestionAdapter);
    }


    void initVolleyCallback(){
        mResultCallback = new IResult() {

            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                if(response.length()>0 && response != null) {

                    //no_sportif_events.setVisibility(View.INVISIBLE);
                    ParsingProductSuggestion p = new ParsingProductSuggestion();
                    list_sugg_pro = p.ArrayParse(response, productSuggestionAdapter);
                    productSuggestionAdapter = new ProductSuggestionAdapter(DetailProduct.this, list_sugg_pro, R.id.prod_suggestionlist);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailProduct.this, LinearLayoutManager.HORIZONTAL, false);
                    prod_suggestionlist.setLayoutManager(mLayoutManager);
                    prod_suggestionlist.setAdapter(productSuggestionAdapter);
                }else{
                    //no_sportif_events.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void notifySuccessJsonobject(String requestType, JSONObject response) {

            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
            }
        };

    }
}
