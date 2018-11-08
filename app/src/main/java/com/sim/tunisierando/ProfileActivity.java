package com.sim.tunisierando;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.ImageManagment;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.UserInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener  {
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    private static int PICK_IMAGE_REQUEST = 1;
    public  int Status ;
    UserInterface userService;
    SharedPreferences preferences;
  ImageView profileimage,imageback,coverimage;
    String urlImages= ServerConfig.UrlForImageLocation;
    TextView email,username,address,pseudo,numtel;
    Button Settings ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Status = 0 ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userService = new UserService(this);
        SocketListeners socketListeners = new SocketListeners(this);
        preferences =getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        profileimage = (ImageView)findViewById(R.id.user_profile_photo);
        coverimage = (ImageView)findViewById(R.id.header_cover_image);
        imageback = (ImageView)findViewById(R.id.imageback);
        address =  (TextView)findViewById(R.id.address);
        pseudo =(TextView)findViewById(R.id.pseudo);
        numtel =  (TextView)findViewById(R.id.numtel);
        Settings = (Button)findViewById(R.id.settings) ;
        email = (TextView) findViewById(
                R.id.user_profile_short_bio);
        username = (TextView)findViewById(R.id.user_profile_name);
        userService.getUserByAccessToken(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)), new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {
                try {
                    Picasso.with(ProfileActivity.this).load(urlImages+result.getString("_profile_pic_url")).placeholder(R.drawable.ic_avatar).into(profileimage);

                    Picasso.with(ProfileActivity.this).load(urlImages+result.getString("_background_pic_url")).placeholder(R.drawable.placeholder).into(coverimage);

                    email.setText(result.getString("email"));
                    username.setText(result.getString("_fisrt_name")+" "+result.getString("_last_name"));
                    address.setText("Adresse : "+result.getString("_address"));
                    pseudo.setText("Pseudo : "+result.getString("username"));
                    numtel.setText("Numéro de télephone :"+ result.getString("_phone_number"));

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

            public void onSuccessListProduct(List<Product> result) {}

            public void onSuccessListComments(List<Comment> result) {


            }
        });
        profileimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Status=1;
                ImageManagment.showFileChooser(ProfileActivity.this);
                return false;
            }
        });
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status=2;
                ImageManagment.showFileChooser(ProfileActivity.this);

            }
        });
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,UpdateProfileActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (Status){
            case 1:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        Bitmap lastBitmap = null;
                        lastBitmap = bitmap;
                        //encoding image to string
                        String image = ImageManagment.getStringImage(lastBitmap);
                        Log.d("image",image);
                        //passing the image to volley
                        User u = new User();
                        u.setProfilePicUrl(image);

                        UserInterface userInterface = new UserService(this);
                        userInterface.Update(u);



                        profileimage.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        Bitmap lastBitmap = null;
                        lastBitmap = bitmap;
                        //encoding image to string
                        String image = ImageManagment.getStringImage(lastBitmap);
                        Log.d("image",image);
                        //passing the image to volley
                        User u = new User();
                        u.setBackgroundPicUrl(image);

                        UserInterface userInterface = new UserService(this);
                        userInterface.SetBackgroundImage(u);

                      coverimage.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

}
