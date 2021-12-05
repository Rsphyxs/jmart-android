package com.example.IlhamJmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.model.Product;
import com.example.IlhamJmartMH.model.ProductCategory;
import com.example.IlhamJmartMH.request.FilterRequest;
import com.example.IlhamJmartMH.request.RequestFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static List<Product> productList = new ArrayList<>();
    public static List<String> productnameList = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();
    private TextView textView;
    private EditText editPage, editName, editLowestprice, editHighestprice;
    private ListView listView;
    private Button buttonGo, buttonNext, buttonPrev, buttonApply, buttonClear;;
    private CheckBox checkBoxNew, checkBoxUsed;
    private Spinner spinnerCategory;
    private TabLayout tabLayout;
    private CardView productCardview, filterCardview;
    private static final Gson gson = new Gson();
    private int page, pageSize;
    private boolean filterApllied = false;

    public static String[] productCategory = {"BOOK", "KITCHEN", "ELECTRONIC", "FASHION", "GAMING", "GADGET", "MOTHERCARE", "COSMETICS",
            "HEALTHCARE", "FURNITURE", "JEWELRY", "TOYS", "FNB", "STATIONERY", "SPORTS", "AUTOMOTIVE",
            "PETCARE", "ART_CRAFT", "CARPENTRY", "MISCELLANEOUS", "PROPERTY", "TRAVEL", "WEDDING"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.productListview);
        editPage = findViewById(R.id.pageNumber);
        buttonGo = findViewById(R.id.buttonGo);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrev = findViewById(R.id.buttonPrev);
        editName = findViewById(R.id.filterName);
        editLowestprice = findViewById(R.id.filterLowerprice);
        editHighestprice = findViewById(R.id.filterHighestprice);
        checkBoxNew = findViewById(R.id.checkboxNew);
        checkBoxUsed = findViewById(R.id.checkboxUsed);
        buttonApply = findViewById(R.id.buttonApply);
        buttonClear = findViewById(R.id.buttonClear);
        spinnerCategory = (Spinner) findViewById(R.id.categorySpinner);
        tabLayout = findViewById(R.id.tabLayout);
        productCardview = findViewById(R.id.productCardview);
        filterCardview = findViewById(R.id.filterCardview);
        buttonGo.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrev.setOnClickListener(this);
        buttonApply.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        productCardview.setVisibility(View.VISIBLE);
                        filterCardview.setVisibility(View.GONE);
                        break;
                    case 1:
                        filterCardview.setVisibility(View.VISIBLE);
                        productCardview.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, productCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        page = 0;
        pageSize = 2;

        ShowProductList(page, pageSize);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_navigation, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        setActivityMode(menuItem.getItemId());
        return super.onOptionsItemSelected(menuItem);
    }

    public void setActivityMode(int modeSelected){
        Intent moveIntent;
        switch (modeSelected){
            case R.id.search:
                break;
            case R.id.addbox:
                moveIntent = new Intent(MainActivity.this, CreateProductActivity.class);
                startActivity(moveIntent);
                break;
            case R.id.person:
                moveIntent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(moveIntent);
                break;
            default:
                break;
        }
    }

    public void ShowProductList(int page, int pageSize){
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    productnameList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type productlistType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, productlistType);
                    Log.d("ProductFragment", "" + productList.get(0).getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (Product product : productList) {
                    productnameList.add(product.getName());
                }
                Log.d("ProductFragment", "Array Size: " + productnameList.size());
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.product_list_view, productnameList);
                listView.setAdapter(adapter);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ProductFragmentError", "" + error.toString());
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getPage("product", page, pageSize, stringListener, errorListener));
    }

    public void ShowProductListFiltered(int page, Integer pageSize){
        filterApllied = true;
        String dataName = editName.getText().toString();
        String minPrice = editLowestprice.getText().toString();
        String maxPrice = editHighestprice.getText().toString();
        int dataHighestprice, dataLowestprice;
        if(TextUtils.isEmpty(minPrice)){
            dataLowestprice = 0;
        }
        else{
            dataLowestprice = Integer.parseInt(editLowestprice.getText().toString());
        }
        if(TextUtils.isEmpty(maxPrice)){
            dataHighestprice = 0;
        }
        else {
            dataHighestprice = Integer.parseInt(editHighestprice.getText().toString());
        }

        ProductCategory category = ProductCategory.BOOK;
        String dataSpinner = spinnerCategory.getSelectedItem().toString();
        for(ProductCategory productCategory : ProductCategory.values()){
            if(productCategory.toString().equals(dataSpinner)){
                category = productCategory;
            }
        }

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    productnameList.clear();
                    JSONArray object = new JSONArray(response);
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, productListType);
                    for (Product product : productList) {
                        productnameList.add(product.getName());
                    }
                    Log.d("FilteredProduct", "Array Size: " + productnameList.size());
                    ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.product_list_view, productnameList);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        FilterRequest filterRequest;
        if (dataHighestprice == 0 && dataLowestprice == 0){
            filterRequest = new FilterRequest(page, pageSize, account.id, dataName, category, listener, errorListener);
        }
        else if(dataHighestprice == 0){
            filterRequest = new FilterRequest(page,pageSize,  account.id, dataLowestprice, dataName, category, listener, errorListener);
        }
        else if(dataLowestprice == 0){
            filterRequest = new FilterRequest(dataName, page, account.id, dataHighestprice, category, listener, errorListener);
        }
        else {
            filterRequest = new FilterRequest(page, account.id, dataName, dataLowestprice, dataHighestprice, category, listener, errorListener);
        }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(filterRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonGo){
            page = Integer.valueOf(editPage.getText().toString());
            page--;
            if(filterApllied){
                ShowProductListFiltered(page, pageSize);
            }
            else {
                ShowProductList(page, pageSize);
            }
        }
        else if(v.getId() == R.id.buttonNext){
            page++;
            if(filterApllied){
                ShowProductListFiltered(page, pageSize);
            }
            else {
                ShowProductList(page, pageSize);
            }
        }
        else if(v.getId() == R.id.buttonPrev){
            if(page == 0){
                Toast.makeText(MainActivity.this, "Already in first page", Toast.LENGTH_SHORT).show();
            }
            else{
                page--;
                if(filterApllied){
                    ShowProductListFiltered(page, pageSize);
                }
                else {
                    ShowProductList(page, pageSize);
                }
            }
        }
        else if(v.getId() == R.id.buttonApply){
            ShowProductListFiltered(page, pageSize);
        }
        else if(v.getId() == R.id.buttonClear){
            filterApllied = false;
            ShowProductList(page, pageSize);
        }
    }
}