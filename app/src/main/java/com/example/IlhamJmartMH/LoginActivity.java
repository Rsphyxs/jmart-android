package com.example.IlhamJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.request.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener {

    private static final Gson gson = new Gson();
    private static Account loggedAccount;
    private EditText editEmail;
    private EditText editPassword;
    private Button buttonLogin;
    private TextView buttonRegister;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.iptEmail);
        editPassword = findViewById(R.id.iptPass);
        buttonLogin = findViewById(R.id.butLogin);
        buttonRegister = findViewById(R.id.butRegister);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.butLogin){
            String dataEmail = editEmail.getText().toString().trim();
            String dataPassword = editPassword.getText().toString().trim();
            LoginRequest loginRequest = new LoginRequest(dataEmail, dataPassword, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(loginRequest);
        }
        else if(v.getId()==R.id.butRegister){
            Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(moveIntent);
        }

    }

    @Override
    public void onResponse(String response) {
        Intent moveIntent = new Intent(LoginActivity.this, MainActivity.class);
        try{
            JSONObject jsonObject = new JSONObject(response);
            moveIntent.putExtra("id", jsonObject.getInt("id"));
        }catch (Exception e){
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
        startActivity(moveIntent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    }
}