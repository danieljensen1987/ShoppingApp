package com.herokuapp.loginandregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    Button btnLogin;
    TextView registerLink;
    EditText etEmail, etPassword;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        registerLink = (TextView) findViewById(R.id.tvRegisterLink);

        btnLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                //String email = "Frederik.o@mailme.dk";
                //String password = "Test12345678";

                Credentials user = new Credentials(email, password);

                authenticate(user);
                break;
            case R.id.tvRegisterLink:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    private void authenticate(Credentials user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.login(user, new GetUserCallback() {
            @Override
            public void done(String uid) {
                if (uid == null) {
                    showErrorMessage();
                } else {
                    logUserIn(uid);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect credentials");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(String returnedUID) {
        userLocalStore.storeUserID(returnedUID);
        startActivity(new Intent(this, MainActivity.class));
    }
}
