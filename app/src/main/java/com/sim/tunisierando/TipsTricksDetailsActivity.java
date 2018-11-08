package com.sim.tunisierando;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.TipsAndTricks;

public class TipsTricksDetailsActivity extends AppCompatActivity {
   TextView title , desc , username;
    ImageView img , profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_tricks_details);
        SocketListeners socketListeners = new SocketListeners(this);
        TipsAndTricks tipsAndTricks = (TipsAndTricks) getIntent().getExtras().getSerializable("object");
        title = (TextView)findViewById(R.id.txt_ev_title);
        desc = (TextView)findViewById(R.id.txt_ev_des);
        username = (TextView)findViewById(R.id.username);
        img = (ImageView)findViewById(R.id.imgi);
        profilepic = (ImageView)findViewById(R.id.profilepic);
        title.setText(tipsAndTricks.getTitle());
        desc.setText(tipsAndTricks.getContent());
        username.setText(tipsAndTricks.getUser().getFisrtName()+" "+tipsAndTricks.getUser().getLastName());
        Picasso.with(this).load(ServerConfig.UrlForImageLocation+tipsAndTricks.getUser().getProfilePicUrl()) .placeholder(R.drawable.loader).into(profilepic);
       if(tipsAndTricks.getImagePath().equals("null")){
           Picasso.with(this).load(ServerConfig.UrlForImageLocation+ tipsAndTricks.getImagePath()) .placeholder(R.drawable.rando).into(img);

       }else {
           Picasso.with(this).load(ServerConfig.UrlForImageLocation+ tipsAndTricks.getImagePath()) .placeholder(R.drawable.loader).into(img);

       }

    }
}
