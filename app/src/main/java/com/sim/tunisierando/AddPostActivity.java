package com.sim.tunisierando;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sim.tunisierando.Configuration.ImageManagment;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Services.Implementation.PostsService;
import com.sim.tunisierando.Services.Interfaces.PostsInterface;

import java.io.IOException;

public class AddPostActivity extends AppCompatActivity {
    private static int PICK_IMAGE_REQUEST = 1;
    Button addimage,poster;
    EditText content ;
    String image="null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        SocketListeners socketListeners = new SocketListeners(this);
        addimage = (Button)findViewById(R.id.addimage);
        poster = (Button)findViewById(R.id.poster);
        content = (EditText) findViewById(R.id.message);



        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageManagment.showFileChooser(AddPostActivity.this);

            }
        });
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts posts = new Posts(content.getText().toString(),image);
                PostsInterface postsInterface = new PostsService(AddPostActivity.this);
                postsInterface.Add(posts);
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
