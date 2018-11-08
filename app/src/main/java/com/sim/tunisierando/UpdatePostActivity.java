package com.sim.tunisierando;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ImageManagment;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Services.Implementation.PostsService;
import com.sim.tunisierando.Services.Interfaces.PostsInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UpdatePostActivity extends AppCompatActivity {
    private static int PICK_IMAGE_REQUEST = 1;
    Button addimage,poster;
    EditText content ;
    String image="null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);
        addimage = (Button)findViewById(R.id.addimage);
        poster = (Button)findViewById(R.id.poster);
        content = (EditText) findViewById(R.id.message);
        SocketListeners socketListeners = new SocketListeners(this);
        final int idpost = (Integer)getIntent().getExtras().getInt("idpost");
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/GetPostById/"+idpost, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                    content.setText(response.getString("content"));


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

        AppSingleton.getInstance(this).addToRequestQueue(getRequest,"rr");
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageManagment.showFileChooser(UpdatePostActivity.this);

            }
        });
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts posts = new Posts(idpost,content.getText().toString(),image);
                PostsInterface postsInterface = new PostsService(UpdatePostActivity.this);
                postsInterface.Update(posts);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                image = ImageManagment.getStringImage(lastBitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
