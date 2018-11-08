package com.sim.tunisierando;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Implementation.UserService;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button goToLogin,Register;
    EditText username,password,password1,email;
    TextView message;
    boolean status = false;
    private TextWatcher textWatcher= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        goToLogin = (Button) findViewById(R.id.btnGoToLogin);
        username = (EditText)findViewById(R.id.ed_login);
        password= (EditText)findViewById(R.id.ed_password);
        password1= (EditText)findViewById(R.id.ed_password1);
        message = (TextView) findViewById(R.id.message) ;
        email = (EditText)findViewById(R.id.ed_email);
        Register = (Button)findViewById(R.id.btnRegister) ;
        goToLogin.setOnClickListener(this);
        Register.setOnClickListener(this);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().toString().equals(password1.getText().toString())){
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
        password1.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }
    public boolean validate(){
        boolean valid = true;
        if(username.getText().toString().isEmpty()){
            username.setError("veuillez saisir un pseudo  ");
            valid = false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("veuillez saisir un mot de passe  ");
            valid = false;
        }
        if(email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("veuillez saisir un email valide  ");
            valid = false;
        }
        return valid;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGoToLogin:
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,0);
                break;
            case R.id.btnRegister:
            if(status && validate()) {
                User.passwords ps = new User.passwords();
                ps.first = password.getText().toString();
                ps.second = password.getText().toString();

                User u = new User(username.getText().toString(), email.getText().toString(), ps);
                UserService userService = new UserService(this);
                userService.Add(u);

            }

                break;
        }
    }
}
