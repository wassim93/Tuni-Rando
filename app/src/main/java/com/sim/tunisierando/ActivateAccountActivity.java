package com.sim.tunisierando;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.UserInterface;

public class ActivateAccountActivity extends AppCompatActivity implements View.OnClickListener{

    EditText code;
    Button validate;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);
        email = (String )getIntent().getExtras().getString("email");
        SocketListeners socketListeners = new SocketListeners(this);
        validate= (Button) findViewById(R.id.btnActivate);
        code = (EditText)findViewById(R.id.ed_activation_code);
        validate.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActivate:
         if(validate()) {
             UserInterface userservice = new UserService(this);
             userservice.ActivateAccount(email, code.getText().toString());
         }

        }
    }
    public boolean validate(){
        boolean valid = true;
        if(code.getText().toString().isEmpty()){
            code.setError("veuillez entrer un code  ");
            valid = false;
        }

        return valid;
    }
    @Override
    public void onBackPressed() {

    }
}
