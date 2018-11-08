package com.sim.tunisierando;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Services.Implementation.ProductService;
import com.sim.tunisierando.Services.Interfaces.ProductInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;
import com.sim.tunisierando.adapters.ProduitListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MagasinActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    RecyclerView gridView;
    FloatingActionButton addprod;
    ArrayList<Product> listpro;
    Toolbar toolbar;

    ProductInterface productInterface;
    ProduitListAdapter produitListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magasin);
        addprod = (FloatingActionButton) findViewById(R.id.add_prod);
        gridView = (RecyclerView) findViewById(R.id.gridview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
              setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Produit à vendre");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MaterialSearchBar searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Search..");
        searchBar.addTextChangeListener(this);
        listpro = new ArrayList<Product>();
        GridLayoutManager lLayout = new GridLayoutManager(MagasinActivity.this, 2);
        gridView.setLayoutManager(lLayout);
        SocketListeners socketListeners = new SocketListeners(this);
         productInterface = new ProductService(this);
        produitListAdapter = new ProduitListAdapter(MagasinActivity.this,listpro,R.id.gridview);
        gridView.setAdapter(produitListAdapter);

        addprod.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        listpro.clear();
        productInterface.getAll(new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

            @Override
            public void onSuccessListProduct(List<Product> result) {

                listpro.addAll(result);

                produitListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onSuccessListComments(List<Comment> result) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_prod:
                Intent i = new Intent(MagasinActivity.this,AddProductActivity.class);
                startActivity(i);
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        produitListAdapter.getFilter().filter(s);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
