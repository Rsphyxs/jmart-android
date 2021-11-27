package com.example.IlhamJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editName;
    private EditText editEmail;
    private EditText editPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.iptName);
        editEmail = findViewById(R.id.iptEmail);
        editPassword = findViewById(R.id.iptPass);
        buttonLogin = findViewById(R.id.butLogin);

        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject!=null){
                        Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
            }
        };
        String dataName = editName.getText().toString();
        String dataEmail = editEmail.getText().toString();
        String dataPassword = editPassword.getText().toString();
        RegisterRequest registerRequest = new RegisterRequest(dataName, dataEmail, dataPassword, stringListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }
}

