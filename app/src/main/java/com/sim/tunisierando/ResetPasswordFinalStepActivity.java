package com.sim.tunisierando;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ResetPasswordFinalStepActivity extends AppCompatActivity {
    public Button send ;
    public EditText code1,code2;
    TextView message;
    String Email;
    private TextWatcher textWatcher= null;
    boolean status = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_final_step);
        send = (Button)findViewById(R.id.send);
        code1 = (EditText) findViewById(R.id.code);
        code2 = (EditText) findViewById(R.id.code2);
        Email = (String) getIntent().getExtras().getString("email");
        message = (TextView)findViewById(R.id.message);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code1.getText().toString().equals(code2.getText().toString())){
                    message.setText("les deux mot de passes  sont  identiques ");
                    message.setTextColor(Color.GREEN);
                    status = true;
                }else {
                    message.setText("les deux mot de passes ne sont pas identiques ");
                    message.setTextColor(Color.RED);
                    status = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        code2.addTextChangedListener(textWatcher);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(status==false){
                   Toast.makeText(ResetPasswordFinalStepActivity.this,
                       "les deux mot de passes doivent étre identiques", Toast.LENGTH_LONG).show();
               }else{

                   final JSONObject params = new JSONObject();
                   try {

                       params.put("email",Email);
                       params.put("password",code1.getText().toString());

                   } catch (Exception e) {
                       e.printStackTrace();
                   }

                   final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordFinalStepActivity.this);
                   progressDialog.setMessage("Veuillez patienter...");
                   progressDialog.show();
                   JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/ResetPasswordComplete", params,
                           new Response.Listener<JSONObject>()
                           {
                               @Override
                               public void onResponse(JSONObject response) {

                                   try {
                                       Log.d("response", response.getString("response")+"");

                                       if(response.getString("response").equals("true")){
                                           Toast.makeText(ResetPasswordFinalStepActivity.this,
                                                   "opération éffectué avec succès", Toast.LENGTH_LONG).show();
                                           Intent i = new Intent(ResetPasswordFinalStepActivity.this,LoginActivity.class);

                                           startActivity(i);


                                       }else {
                                           Toast.makeText(ResetPasswordFinalStepActivity.this,
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

                   AppSingleton.getInstance(ResetPasswordFinalStepActivity.this).addToRequestQueue(getRequest,"fg");

               }
            }
        });

    }
    @Override
    public void onBackPressed() {

    }
}
