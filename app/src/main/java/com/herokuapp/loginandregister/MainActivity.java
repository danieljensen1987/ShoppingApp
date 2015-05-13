package com.herokuapp.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    UserLocalStore userLocalStore;
    EditText etEmail, etPassword;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogout = (Button) findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_logout:
                userLocalStore.clearUserData();
                //userLocalStore.setUserLoggedIn(false);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userLocalStore.isLoggedIn()) {
            displayUserLists();
        } else{
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    private void displayUserLists() {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.findLists(userLocalStore.getUID(), new ListCallback() {
            @Override
            public void done(List<ShoppingList> lists) {
                etEmail.setText(lists.get(0).items.toString());
            }
        });

    }
}
