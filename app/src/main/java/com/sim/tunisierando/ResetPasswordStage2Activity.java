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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordStage2Activity extends AppCompatActivity {
    public Button send ;
    public EditText code;
    String Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_stage2);
        send = (Button)findViewById(R.id.send);
        code = (EditText) findViewById(R.id.code);
        Email = (String) getIntent().getExtras().getString("email");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject params = new JSONObject();
                try {

                    params.put("email",Email);
                    params.put("token",code.getText().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("response--------------",params+"");
               final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordStage2Activity.this);
                progressDialog.setMessage("Veuillez patienter...");
                progressDialog.show();
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/CheckResetPasswordCode", params,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Log.d("response", response.getString("response")+"");

                                    if(response.getString("response").equals("true")){
                                        Toast.makeText(ResetPasswordStage2Activity.this,
                                                "code correct ", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(ResetPasswordStage2Activity.this,ResetPasswordFinalStepActivity.class);
                                        i.putExtra("email",Email);
                                        startActivity(i);

                                    }else {
                                        Toast.makeText(ResetPasswordStage2Activity.this,
                                                response.getString("response"), Toast.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.hide();

                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                progressDialog.hide();
                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppSingleton.getInstance(ResetPasswordStage2Activity.this).addToRequestQueue(getRequest,"fg");

            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}
