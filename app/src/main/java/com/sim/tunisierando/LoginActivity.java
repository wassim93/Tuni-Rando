package com.sim.tunisierando;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.UserInterface;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnGoToRegister,btnSignIn;
    EditText login,password;
    TextView resretpassw;
    public static  String CURRENT_USER ;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences preferences;
    public static final String OAUTH_TOKEN= "token";
    UserInterface userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userService = new UserService(this);
        btnGoToRegister = (Button) findViewById(R.id.btnGoToRegister);
        resretpassw = (TextView) findViewById(R.id.tt1);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        login = (EditText) findViewById(R.id.ed_login);
        password = (EditText) findViewById(R.id.ed_password);
        preferences =getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if(preferences.getString(OAUTH_TOKEN, null) != null){
            Intent i  = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(i);
        }else {

        }  Log.d("jhhjhjhjhjh","prefrences fare8");
        SocketListeners socketListeners = new SocketListeners(this);
        btnGoToRegister.setOnClickListener(this);

        btnSignIn.setOnClickListener(this);
        resretpassw.setOnClickListener(this);


    }

    public boolean validate(){
        boolean valid = true;
        if(login.getText().toString().isEmpty()){
            login.setError("veuillez saisir votre pseudo ou votre email  ");
             valid = false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("veuillez saisir votre mot de passe  ");
            valid = false;
        }
       return valid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGoToRegister:
                Intent i = new Intent(this,RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,0);
                break;
            case R.id.btnSignIn:
                if(validate()){
                    userService.Login(login.getText().toString(),password.getText().toString());
                }else {
                    Toast.makeText(this,
                            " Vérifier vos informations .. ", Toast.LENGTH_LONG).show();
        }

                break;
            case R.id.tt1:
                Intent i1 = new Intent(this,ResetPasswordActivity.class);
                startActivity(i1);

                break;


        }

    }



  @Override
  public void onBackPressed() { }
}
