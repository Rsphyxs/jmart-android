package com.example.IlhamJmartMH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
    private ArrayList<Payment> paymentList = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        getPaymentList();

        rvInvoice = findViewById(R.id.invoiceCardview);
        showRecyclerList();


    }

    private void showRecyclerList() {

    }

    private void getPaymentList() {
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener
            @Override
            public void onResponse(String response) {
                try {
                    paymentList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();     //mengambil tipe list Payment
                    paymentList = gson.fromJson(response, paymentListType);
                    rvInvoice.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                    InvoiceCardViewAdapter invoiceCardViewAdapter = new InvoiceCardViewAdapter(paymentList);
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
}
