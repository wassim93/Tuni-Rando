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
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Services.Implementation.TipsAndTricksService;
import com.sim.tunisierando.Services.Interfaces.TipsAndTricksInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UpdateTipsTricksActivity extends AppCompatActivity {
    EditText title,content;
    Button addimage,UpdateArticle ;
    public static String imgpath="";
    private static int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tips_tricks);
        title = (EditText)findViewById(R.id.titre);
        content= (EditText)findViewById(R.id.content);
        addimage = (Button)findViewById(R.id.addimage);
        UpdateArticle  = (Button)findViewById(R.id.addarticle) ;
        SocketListeners socketListeners = new SocketListeners(this);
        final int idArtcicle = (Integer)getIntent().getExtras().getInt("idtips");
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/GetTipsTricksById/"+idArtcicle, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            content.setText(response.getString("content"));
                            title.setText(response.getString("title"));


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
                ImageManagment.showFileChooser(UpdateTipsTricksActivity.this);
            }
        });
        UpdateArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    TipsAndTricks tipsAndTricks = new TipsAndTricks();
                    tipsAndTricks.setContent(content.getText().toString());
                    tipsAndTricks.setTitle(title.getText().toString());
                    tipsAndTricks.setImagePath(imgpath);
                    tipsAndTricks.setId(idArtcicle);
                    TipsAndTricksInterface tipsAndTricksService = new TipsAndTricksService(UpdateTipsTricksActivity.this);
                    tipsAndTricksService.Update(tipsAndTricks);
                }
            }
        });
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
    public boolean validate(){
        boolean valid = true;
        if(content.getText().toString().isEmpty()){
            content.setError("veuillez saisir un text ");
            valid = false;
        }
        if(title.getText().toString().isEmpty()){
            title.setError("veuillez saisir un titre  ");
            valid = false;
        }

        return valid;
    }
}
