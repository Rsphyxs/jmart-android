package com.example.IlhamJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.model.Product;
import com.example.IlhamJmartMH.model.ProductCategory;
import com.example.IlhamJmartMH.request.CreateProductRequest;
import com.example.IlhamJmartMH.request.RequestFactory;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateProductActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] categoryList = MainActivity.productCategory;
    private String[] shipmentplanList = {"INSTANT", "SAME DAY", "NEXT DAY", "REGULER", "KARGO"};
    private EditText editName, editWeight, editPrice, editDiscount;
    private RadioButton radioNew, radioUsed;
    private Spinner spinnerCategory, spinnerShipmentplan;
    private Button buttonCreate, buttonCancel;
    private static final Gson gson = new Gson();
    private Account account = LoginActivity.getLoggedAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        editName = findViewById(R.id.createproductName);
        editWeight = findViewById(R.id.createproductWeight);
        editPrice = findViewById(R.id.createproductPrice);
        editDiscount = findViewById(R.id.createproductDiscount);
        radioNew = findViewById(R.id.NewRadio);
        radioUsed = findViewById(R.id.UsedRadio);

        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(this);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(this);

        spinnerCategory = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(CreateProductActivity.this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerShipmentplan = findViewById(R.id.shipmentplanSpinner);
        ArrayAdapter<String> shipmentplanAdapter = new ArrayAdapter<String>(CreateProductActivity.this, android.R.layout.simple_spinner_item, shipmentplanList);
        shipmentplanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShipmentplan.setAdapter(shipmentplanAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreate) {
            refreshData();
            if(account.store != null){
                String name = editName.getText().toString();
                String weight = editWeight.getText().toString();
                String price = editPrice.getText().toString();
                String discount = editDiscount.getText().toString();

                int dataWeight = Integer.valueOf(weight);
                double dataPrice = Double.valueOf(price);
                double dataDiscount = Double.valueOf(discount);
                boolean dataRadiobutton = false;
                if (radioNew.isChecked()) {
                    dataRadiobutton = true;
                }
                byte dataShipmentplan = spinnerShipmentPlan(spinnerShipmentplan);
                ProductCategory dataProductcategory = spinnerProductCategory(spinnerCategory);

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CreateProductActivity", response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(CreateProductActivity.this, "Product created successfully", Toast.LENGTH_SHORT).show();
                            }
                            Product product = gson.fromJson(response, Product.class);
                            Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateProductActivity.this, "Create Product Failed Connection", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                };

                CreateProductRequest createProductRequest = new CreateProductRequest(account.id, name, dataWeight, dataRadiobutton, dataPrice, dataDiscount, dataProductcategory, dataShipmentplan, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(CreateProductActivity.this);
                queue.add(createProductRequest);
            }
            else{
                Toast.makeText(CreateProductActivity.this, "You don't have a store", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId() == R.id.buttonCancel) {
            Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    public byte spinnerShipmentPlan(Spinner spinnerShipmentplan) {
        String dataShipmentplan = spinnerShipmentplan.getSelectedItem().toString();
        if (dataShipmentplan.equals("INSTANT")) {
            return 1;
        } else if (dataShipmentplan.equals("SAME DAY")) {
            return 2;
        } else if (dataShipmentplan.equals("NEXT DAY")) {
            return 4;
        } else if (dataShipmentplan.equals("REGULER")) {
            return 8;
        } else if (dataShipmentplan.equals("KARGO")) {
            return 16;
        } else {
            return 0;
        }
    }


    public ProductCategory spinnerProductCategory(Spinner spinnerProductcategory) {
        ProductCategory category = ProductCategory.BOOK;
        String productCategory = spinnerProductcategory.getSelectedItem().toString();
        for (ProductCategory productcategory : ProductCategory.values()) {
            if (productcategory.toString().equals(productCategory)) {
                category = productcategory;
            }
        }
        return category;
    }

    public void refreshData() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    Log.d("MainActivity(Rfrsh)", "Data: " + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorResponse", "Error: " + error);
                Toast.makeText(CreateProductActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(CreateProductActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }
}