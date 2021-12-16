package com.example.IlhamJmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.model.Store;
import com.example.IlhamJmartMH.request.RegisterStoreRequest;
import com.example.IlhamJmartMH.request.RequestFactory;
import com.example.IlhamJmartMH.request.TopUpRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * AboutMeActivity sebagai Activity yang menampilkan informasi account
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textName, textEmail, textBalance, textStorename, textStoreAddress, textStorePhonenumber;
    private EditText editName, editAddress, editPhonenumber, editTopup;
    private Button registerstoreButton, cancelButton, buttonRegister, topupButton;
    private LinearLayout registerstoreLayout, storeLayout;
    private Account account = LoginActivity.getLoggedAccount();
    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        refreshData();

        textName = findViewById(R.id.accountName);
        textEmail = findViewById(R.id.accountEmail);
        textBalance = findViewById(R.id.accountBalance);
        textStorename = findViewById(R.id.storeName);
        textStoreAddress = findViewById(R.id.storeAddress);
        textStorePhonenumber = findViewById(R.id.storePhonenumber);
        editName = findViewById(R.id.registerName);
        editAddress = findViewById(R.id.registerAddress);
        editPhonenumber = findViewById(R.id.registerPhonenumber);
        editTopup = findViewById(R.id.toupAmount);

        registerstoreButton = findViewById(R.id.registerstoreButton);
        registerstoreLayout = findViewById(R.id.registerstoreLayout);
        storeLayout = findViewById(R.id.storeLayout);
        cancelButton = findViewById(R.id.cancelButton);
        buttonRegister = findViewById(R.id.registerButton);
        topupButton = findViewById(R.id.toupButton);
        registerstoreButton.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        topupButton.setOnClickListener(this);

        textName.setText(account.name);
        textEmail.setText(account.email);
        textBalance.setText(String.valueOf(account.balance));

        if (account.store == null) {
            TransitionManager.beginDelayedTransition(registerstoreLayout);
            registerstoreLayout.setVisibility(View.GONE);
            registerstoreButton.setVisibility(View.VISIBLE);
        } else {
            registerstoreButton.setVisibility(View.GONE);
            registerstoreLayout.setVisibility(View.GONE);
            storeLayout.setVisibility(View.VISIBLE);
            textStorename.setText(account.store.name);
            textStoreAddress.setText(account.store.address);
            textStorePhonenumber.setText(account.store.phoneNumber);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerstoreButton) {
            registerstoreButton.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(registerstoreLayout);
            registerstoreLayout.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.cancelButton) {
            TransitionManager.beginDelayedTransition(registerstoreLayout);
            registerstoreLayout.setVisibility(View.GONE);
            registerstoreButton.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.toupButton) {
            double topupAmount = Double.valueOf(editTopup.getText().toString().trim());
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Boolean object = Boolean.valueOf(response);
                    if (object) {
                        Toast.makeText(AboutMeActivity.this, "TopUp Successful, Balance has been updated", Toast.LENGTH_SHORT).show();
                        refreshData();
                        editTopup.getText().clear();
                    } else {
                        Toast.makeText(AboutMeActivity.this, "TopUp Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AboutMeActivity.this, "Top Up Failed Connection", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", error.toString());
                }
            };

            TopUpRequest topUpRequest = new TopUpRequest(topupAmount, account.id, listener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
            queue.add(topUpRequest);
        } else if (v.getId() == R.id.registerButton) {
            String dataName = editName.getText().toString();
            String dataAddress = editAddress.getText().toString();
            String dataPhoneNumber = editPhonenumber.getText().toString();

            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("AboutMeActivity(Resp)", response);
                    JSONObject object = null;
                    try {
                        Log.d("AboutMeActivity(try)", response);
                        object = new JSONObject(response);
                        if (object != null) {
                            Toast.makeText(AboutMeActivity.this, "Store registered successfully", Toast.LENGTH_SHORT).show();
                        }
                        account.store = gson.fromJson(object.toString(), Store.class);
                        textStorename.setText(account.store.name);
                        textStoreAddress.setText(account.store.address);
                        textStorePhonenumber.setText(account.store.phoneNumber);
                        registerstoreLayout.setVisibility(View.GONE);
                        TransitionManager.beginDelayedTransition(storeLayout);
                        storeLayout.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        Toast.makeText(AboutMeActivity.this, "Register Store Failed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d("AboutMeActivity(catch)", response);
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AboutMeActivity.this, "Register Store Failed Connection", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", error.toString());
                }
            };

            RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(account.id, dataName, dataAddress, dataPhoneNumber, listener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
            queue.add(registerStoreRequest);
        }
    }

    public void refreshData() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    textBalance.setText(String.valueOf(account.balance));
                    Log.d("AboutMeActivity(Rfrsh)", "Data: " + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorResponse", "Error: " + error);
                Toast.makeText(AboutMeActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        setActivityMode(menuItem.getItemId());
        return super.onOptionsItemSelected(menuItem);
    }

    public void setActivityMode(int modeSelected) {
        switch (modeSelected) {
            case R.id.invoice:
                Intent moveIntent = new Intent(AboutMeActivity.this, InvoiceActivity.class);
                startActivity(moveIntent);
                break;
            case R.id.refresh:
                refreshData();
            default:
                break;
        }
    }
}