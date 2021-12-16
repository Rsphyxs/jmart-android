package com.example.IlhamJmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.model.Payment;
import com.example.IlhamJmartMH.request.InvoiceRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InvoiceActivity extends AppCompatActivity {
    private RecyclerView rvInvoice;
    private static final Gson gson = new Gson();
    private ArrayList<Payment> userInvoiceList = new ArrayList<>();
    private ArrayList<Payment> storeInvoiceList = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();
    private MenuItem itemPerson, itemStore;
    private TextView invoiceTitle;
    public static boolean isUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        rvInvoice = findViewById(R.id.invoiceCardview);
        invoiceTitle = findViewById(R.id.invoiceTitle);

        getUserInvoiceList();
    }


    private void getUserInvoiceList() {
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener
            @Override
            public void onResponse(String response) {
                try {
                    userInvoiceList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();     //mengambil tipe list Payment
                    userInvoiceList = gson.fromJson(response, paymentListType);
                    rvInvoice.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                    InvoiceCardViewAdapter invoiceCardViewAdapter = new InvoiceCardViewAdapter(userInvoiceList);
                    rvInvoice.setAdapter(invoiceCardViewAdapter);
                } catch (JSONException e) {     //jika response null
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceActivity.this, "Get List Failed due to Connection", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest invoiceRequest = new InvoiceRequest(account.id, true, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
        queue.add(invoiceRequest);
    }

    private void getStoreInvoiceList() {
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener
            @Override
            public void onResponse(String response) {
                try {
                    storeInvoiceList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();     //mengambil tipe list Payment
                    storeInvoiceList = gson.fromJson(response, paymentListType);
                    rvInvoice.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                    InvoiceCardViewAdapter invoiceCardViewAdapter = new InvoiceCardViewAdapter(storeInvoiceList);
                    rvInvoice.setAdapter(invoiceCardViewAdapter);
                } catch (JSONException e) {     //jika response null
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceActivity.this, "Get List Failed due to Connection", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest invoiceRequest = new InvoiceRequest(account.id, false, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
        queue.add(invoiceRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invoice_menu, menu);
        itemPerson = menu.findItem(R.id.user);
        itemStore = menu.findItem(R.id.store);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        setActivityMode(menuItem.getItemId());
        return super.onOptionsItemSelected(menuItem);
    }

    public void setActivityMode(int modeSelected) {
        switch (modeSelected) {
            case R.id.store:
                itemPerson.setVisible(true);
                itemStore.setVisible(false);
                invoiceTitle.setText("Invoice Store");
                isUser = false;
                getStoreInvoiceList();
                break;
            case R.id.user:
                itemStore.setVisible(true);
                itemPerson.setVisible(false);
                invoiceTitle.setText("Invoice User");
                isUser = true;
                getUserInvoiceList();
                break;
            default:
                break;
        }
    }
}
