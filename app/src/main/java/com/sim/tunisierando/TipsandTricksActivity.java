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

import com.sim.tunisierando.Configuration.ImageManagment;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Services.Implementation.TipsAndTricksService;
import com.sim.tunisierando.Services.Interfaces.TipsAndTricksInterface;

import java.io.IOException;

public class TipsandTricksActivity extends AppCompatActivity {
     EditText title,content;
     Button addimage,addArticle ;
    public static String imgpath="";
    private static int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tipsand_tricks);
        SocketListeners socketListeners = new SocketListeners(this);
        title = (EditText)findViewById(R.id.titre);
        content= (EditText)findViewById(R.id.content);
        addimage = (Button)findViewById(R.id.addimage);
        addArticle = (Button)findViewById(R.id.addarticle) ;
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageManagment.showFileChooser(TipsandTricksActivity.this);
            }
        });
        addArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    TipsAndTricks tipsAndTricks = new TipsAndTricks();
                    tipsAndTricks.setContent(content.getText().toString());
                    tipsAndTricks.setTitle(title.getText().toString());
                    tipsAndTricks.setImagePath(imgpath);
                    TipsAndTricksInterface tipsAndTricksService = new TipsAndTricksService(TipsandTricksActivity.this);
                    tipsAndTricksService.Add(tipsAndTricks);
                }


            }
        });
    }
    public boolean validate(){
        boolean valid = true;
        if(title.getText().toString().isEmpty()){
            title.setError("veuillez saisir votre text ");
            valid = false;
        }
        if(content.getText().toString().isEmpty()){
            content.setError("veuillez saisir un text ");
            valid = false;
        }
        return valid;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        Bitmap lastBitmap = null;
                        lastBitmap = bitmap;
                        //encoding image to string
                        String image = ImageManagment.getStringImage(lastBitmap);
                        Log.d("image", image);
                        //passing the image to volley
                        ;
                        imgpath=image;




                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        }
}
