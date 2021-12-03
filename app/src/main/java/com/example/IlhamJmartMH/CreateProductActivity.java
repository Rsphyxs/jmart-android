package com.example.IlhamJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateProductActivity extends AppCompatActivity {

    private String[] categoryList = FilterFragment.productCategory;
    private String[] shipmentplanList = {"INSTANT", "SAME DAY", "NEXT DAY", "REGULER", "KARGO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        Spinner categoryspinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(CreateProductActivity.this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(categoryAdapter);
        Spinner shipmentplanspinner = (Spinner) findViewById(R.id.shipmentplanSpinner);
        ArrayAdapter<String> shipmentplanAdapter = new ArrayAdapter<String>(CreateProductActivity.this, android.R.layout.simple_spinner_item, shipmentplanList);
        shipmentplanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipmentplanspinner.setAdapter(shipmentplanAdapter);

    }
}