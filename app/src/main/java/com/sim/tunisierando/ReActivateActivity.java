package com.sim.tunisierando;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sim.tunisierando.Configuration.ServerConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class ReActivateActivity extends AppCompatActivity {
    public Button send ;
    public EditText Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_activate);
        send = (Button)findViewById(R.id.send);
        Email = (EditText) findViewById(R.id.email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(ReActivateActivity.this);
                progressDialog.setMessage("Veuillez patienter ...");
                progressDialog.show();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/ResetPassword/"+Email.getText().toString(),null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {

                                try{


                                    if(response.getString("response").equals("true")){
                                        Intent intent = new Intent(ReActivateActivity.this,ActivateAccountActivity.class);
                                        intent.putExtra("email",Email.getText().toString());
                                        startActivity(intent);
                                        progressDialog.dismiss();

                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ReActivateActivity.this,
                                                ""+ response.getString("response"), Toast.LENGTH_LONG).show();
                                    }

                                }catch (JSONException e){

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
                request.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                RequestQueue rq1 = Volley.newRequestQueue(ReActivateActivity.this);
                rq1.add(request);
            }
        });
    }
}
