package com.sim.tunisierando;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.CustomViewPager;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.UserInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;
import com.sim.tunisierando.adapters.TabsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CustomViewPager viewPager;
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    UserInterface userService;
    SharedPreferences preferences;
    JSONObject userObj ;
    View hView;
    String message;
    String urlImages= ServerConfig.UrlForImageLocation;
    private Socket socket;
    private int[] tabIcons = {
            R.drawable.ic_homeicwhite,
            R.drawable.ic_event,
            R.drawable.ic_star,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        userService = new UserService(this);
        preferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        SocketListeners socketListeners = new SocketListeners(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tuni-Rando");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        viewPager.setPagingEnabled(false);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
         hView =  navigationView.getHeaderView(0);
        userService.getUserByAccessToken(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)), new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {

                userObj = result;
                TextView email = (TextView) hView.findViewById(R.id.email);
                TextView username  = (TextView) hView.findViewById(R.id.name);
                ImageView img = (ImageView)hView.findViewById(R.id.profilepic);
                try {
                    if(result.getString("_fisrt_name").equals("default")){
                        Intent i = new Intent(HomeActivity.this,CompleteProfileActivity.class);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    email.setText(result.getString("email"));
                    username.setText(result.getString("username"));
                    Picasso.with(HomeActivity.this).load(urlImages+result.getString("_profile_pic_url"))
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .placeholder(R.drawable.ic_avatar).into(img);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

            @Override

            public void onSuccessListProduct(List<Product> result) { }

            public void onSuccessListComments(List<Comment> result) {


            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        finish();
        startActivity(getIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_astuces) {
            Intent i = new Intent(HomeActivity.this,TipsandTricksActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_profil) {
            Intent i = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_gallerie) {
            Intent i = new Intent(HomeActivity.this,GallerieActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_magasin) {
            Intent i = new Intent(HomeActivity.this,MagasinActivity.class);
            startActivity(i);
        } else if (id == R.id.logout) {
           userService.Logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
