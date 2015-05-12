package com.herokuapp.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends ActionBarActivity implements View.OnClickListener{
    EditText etEmail, etPassword;
    Button btnRegister;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRegister = (Button) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                Credentials user = new Credentials(email, password);
                registerUser(user);
                break;
        }
    }

    private void registerUser(Credentials user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.createUser(user, new GetUserCallback() {
            @Override
            public void done(String returnedUID) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
