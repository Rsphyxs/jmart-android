package com.example.IlhamJmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.model.Payment;
import com.example.IlhamJmartMH.model.Product;
import com.example.IlhamJmartMH.model.ProductCategory;
import com.example.IlhamJmartMH.request.FilterRequest;
import com.example.IlhamJmartMH.request.PaymentRequest;
import com.example.IlhamJmartMH.request.RequestFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity sebagai Activity utama dari proses aplikasi
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static List<Product> productList = new ArrayList<>();
    public static List<String> productnameList = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();
    private EditText editPage, editName, editLowestprice, editHighestprice;
    private ListView listView;
    private Button buttonGo, buttonNext, buttonPrev, buttonApply, buttonClear;
    private CheckBox checkBoxNew, checkBoxUsed;
    private Spinner spinnerCategory;
    private TabLayout tabLayout;
    private CardView productCardview, filterCardview;
    private static final Gson gson = new Gson();
    private int page, pageSize, pageTemp, totalItem;
    private boolean filterApllied = false, lastPage = false;
    private String shipmentPlan, condition;
    private double totalPrice;
    private Payment payment = new Payment();

    public static String[] productCategory = {"BOOK", "KITCHEN", "ELECTRONIC", "FASHION", "GAMING", "GADGET", "MOTHERCARE", "COSMETICS",
            "HEALTHCARE", "FURNITURE", "JEWELRY", "TOYS", "FNB", "STATIONERY", "SPORTS", "AUTOMOTIVE",
            "PETCARE", "ART_CRAFT", "CARPENTRY", "MISCELLANEOUS", "PROPERTY", "TRAVEL", "WEDDING"};

    /**
     * Method onCreate yang akan berjalan saat Activity dibentuk
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshData();
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

            /**
             * Method onTabSelected sebagai handler dari tab
             * @param tab sebagai variabel yang menyimpan informasi tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
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
        listView.setOnItemClickListener(this);
    }

    /**
     * Method onCreateOptionsMenu yang akan menampilkan menu navigasi berdasarkan layout yang diberikan
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Method onOptionsItemSelected sebagai handler ketika ada menuItem yang diclick
     * @param menuItem
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        setActivityMode(menuItem.getItemId());
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * Method setActivityMode yang akan menentukan aktivitas ketika menuItem ditekan
     * @param modeSelected sebagai data yang menyimpan id dari itemMenu yang ditekan
     */
    public void setActivityMode(int modeSelected) {
        Intent moveIntent;
        switch (modeSelected) {
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

    /**
     * Method ShowProductList yang akan menampilkan product
     * @param pageTemp yang menyimpan halaman sementara
     * @param pageSize yang menyimpan ukuran halaman
     */
    public void ShowProductList(int pageTemp, int pageSize) {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    productnameList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type productlistType = new TypeToken<ArrayList<Product>>() {
                    }.getType();
                    productList = gson.fromJson(response, productlistType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (productList.isEmpty()) {
                    lastPage = true;
                    Toast.makeText(MainActivity.this, "You're in the last page", Toast.LENGTH_SHORT).show();
                } else {
                    lastPage = false;
                    for (Product product : productList) {
                        productnameList.add(product.getName());
                    }
                    Log.d("ProductFragment", "Array Size: " + productnameList.size());
                    ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.product_list_view, productnameList);
                    listView.setAdapter(adapter);
                }
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
        queue.add(RequestFactory.getPage("product", pageTemp, pageSize, stringListener, errorListener));
    }

    /**
     * Method ShowProductListFiltered yang akan menampilkan product yang sudah difilter
     * @param page yang menyimpan halaman
     * @param pageSize yang menyimpan ukuran halaman
     */
    public void ShowProductListFiltered(int page, Integer pageSize) {
        filterApllied = true;
        String dataName = editName.getText().toString();
        String minPrice = editLowestprice.getText().toString();
        String maxPrice = editHighestprice.getText().toString();
        int dataHighestprice, dataLowestprice;
        if (TextUtils.isEmpty(minPrice)) {
            dataLowestprice = 0;
        } else {
            dataLowestprice = Integer.parseInt(editLowestprice.getText().toString());
        }
        if (TextUtils.isEmpty(maxPrice)) {
            dataHighestprice = 0;
        } else {
            dataHighestprice = Integer.parseInt(editHighestprice.getText().toString());
        }

        ProductCategory category = ProductCategory.BOOK;
        String dataSpinner = spinnerCategory.getSelectedItem().toString();
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.toString().equals(dataSpinner)) {
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
                    Type productListType = new TypeToken<ArrayList<Product>>() {
                    }.getType();
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
        if (dataHighestprice == 0 && dataLowestprice == 0) {
            filterRequest = new FilterRequest(page, pageSize, account.id, dataName, category, listener, errorListener);
        } else if (dataHighestprice == 0) {
            filterRequest = new FilterRequest(page, pageSize, account.id, dataLowestprice, dataName, category, listener, errorListener);
        } else if (dataLowestprice == 0) {
            filterRequest = new FilterRequest(dataName, page, pageSize, account.id, dataHighestprice, category, listener, errorListener);
        } else {
            filterRequest = new FilterRequest(page, pageSize, account.id, dataName, dataLowestprice, dataHighestprice, category, listener, errorListener);
        }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(filterRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonGo) {
            page = Integer.valueOf(editPage.getText().toString());
            page--;
            if (filterApllied) {
                ShowProductListFiltered(page, pageSize);
            } else {
                ShowProductList(page, pageSize);
            }
        } else if (v.getId() == R.id.buttonNext) {
            Toast.makeText(MainActivity.this, "Page: " + page, Toast.LENGTH_SHORT).show();
            if (filterApllied) {
                ShowProductListFiltered(page, pageSize);
            } else {
                if (lastPage) {
                    Toast.makeText(MainActivity.this, "You're in the last page", Toast.LENGTH_SHORT).show();
                } else {
                    page++;
                    ShowProductList(page, pageSize);
                }
            }
        } else if (v.getId() == R.id.buttonPrev) {
            if (page == 0) {
                Toast.makeText(MainActivity.this, "Already in first page", Toast.LENGTH_SHORT).show();
            } else {
                page--;
                if (filterApllied) {
                    ShowProductListFiltered(page, pageSize);
                } else {
                    ShowProductList(page, pageSize);
                }
            }
        } else if (v.getId() == R.id.buttonApply) {
            ShowProductListFiltered(0, pageSize);
        } else if (v.getId() == R.id.buttonClear) {
            filterApllied = false;
            ShowProductList(0, pageSize);
        }
    }

    /**
     * Method onItemClick sebagai event handler ketika ada list view yang ditekan
     * @param parent parent
     * @param view sebagai halaman layout
     * @param position sebagai posisi dari listview yang ditekan
     * @param id id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        refreshData();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.product_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView productName = dialog.findViewById(R.id.productName);
        final TextView productWeight = dialog.findViewById(R.id.productWeight);
        final TextView productCondition = dialog.findViewById(R.id.productCondition);
        final TextView productPrice = dialog.findViewById(R.id.productPrice);
        final TextView productDiscount = dialog.findViewById(R.id.productDiscount);
        final TextView productShipmentplan = dialog.findViewById(R.id.productShipmentplan);
        final TextView productCategory = dialog.findViewById(R.id.productCategory);
        final Button buyButton = dialog.findViewById(R.id.buttonBuynow);

        Product product = productList.get(position);
        shipmentPlan = "REGULER";
        if (product.getShipmentPlans() == 1) {
            shipmentPlan = "INSTANT";
        } else if (product.getShipmentPlans() == 2) {
            shipmentPlan = "SAME DAY";
        } else if (product.getShipmentPlans() == 4) {
            shipmentPlan = "NEXT DAY";
        } else if (product.getShipmentPlans() == 8) {
            shipmentPlan = "REGULER";
        } else if (product.getShipmentPlans() == 16) {
            shipmentPlan = "KARGO";
        }

        condition = "New";
        if (product.isConditionUsed()) {
            condition = "Used";
        } else {
            condition = "New";
        }

        productName.setText(product.getName());
        productWeight.setText(product.getWeight() + " Kg");
        productCondition.setText(condition);
        productPrice.setText("Rp. " + product.getPrice());
        productDiscount.setText(product.getDiscount() + " %");
        productShipmentplan.setText(shipmentPlan);
        productCategory.setText(product.getCategory() + "");
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.payment_confirmation);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView productName = dialog.findViewById(R.id.productName);
                final TextView productWeight = dialog.findViewById(R.id.productWeight);
                final TextView productCondition = dialog.findViewById(R.id.productCondition);
                final TextView productPrice = dialog.findViewById(R.id.productPrice);
                final TextView productDiscount = dialog.findViewById(R.id.productDiscount);
                final TextView productShipmentplan = dialog.findViewById(R.id.productShipmentplan);
                final TextView productCategory = dialog.findViewById(R.id.productCategory);
                final TextView userBalance = dialog.findViewById(R.id.userBalance);
                final TextView finalPrice = dialog.findViewById(R.id.totalPrice);
                final EditText qty = dialog.findViewById(R.id.productQty);
                final EditText address = dialog.findViewById(R.id.userAddress);
                final ImageButton minusButton = dialog.findViewById(R.id.minusButton);
                final ImageButton plusButton = dialog.findViewById(R.id.plusButton);
                final Button buttonBuy = dialog.findViewById(R.id.buttonBuy);
                final Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

                totalItem = 1;
                totalPrice = product.getPrice();

                qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    /**
                     * Method onTextChanged sebagai event handler ketika adanya perubahan pada edit text
                     * @param s s
                     * @param start start
                     * @param before before
                     * @param count count
                     */
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (qty.getText().toString().equals("")) {

                        } else {
                            totalItem = Integer.valueOf(qty.getText().toString());
                            totalPrice = product.getPrice() * totalItem;
                            finalPrice.setText("Rp. " + totalPrice);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                Product product = productList.get(position);

                productName.setText(product.getName());
                productWeight.setText(product.getWeight() + " Kg");
                productCondition.setText(condition);
                productPrice.setText("Rp. " + product.getPrice());
                productDiscount.setText(product.getDiscount() + " %");
                productShipmentplan.setText(shipmentPlan);
                productCategory.setText(product.getCategory() + "");
                userBalance.setText("Rp. " + account.balance);
                finalPrice.setText("Rp. " + product.getPrice());

                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (totalItem > 1) {
                            totalItem--;
                            qty.setText(Integer.toString(totalItem));
                        }
                    }
                });
                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        totalItem++;
                        qty.setText(Integer.toString(totalItem));
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isEmptyFields = false;
                        if (TextUtils.isEmpty(address.getText().toString())) {
                            isEmptyFields = true;
                            address.setError("This can't be empty");
                        }
                        if (TextUtils.isEmpty(qty.getText().toString())) {
                            isEmptyFields = true;
                            qty.setError("This can't be empty");
                        }
                        if (!isEmptyFields) {
                            if (totalPrice < account.balance) {
                                Response.Listener<String> listenerCreatePayment = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject object = null;
                                        try {
                                            object = new JSONObject(response);
                                            if (object != null) {
                                                Toast.makeText(MainActivity.this, "Payment Success!", Toast.LENGTH_SHORT).show();
                                                refreshData();
                                                dialog.dismiss();
                                            }
                                            payment = gson.fromJson(response, Payment.class);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                Response.ErrorListener errorListenerCreatePayment = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", error.toString());
                                    }
                                };
                                PaymentRequest createPaymentRequest = new PaymentRequest(account.id, product.id, totalItem, address.getText().toString(), product.getShipmentPlans(), product.getAccountId(), listenerCreatePayment, errorListenerCreatePayment);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                queue.add(createPaymentRequest);
                            } else {
                                Toast.makeText(MainActivity.this, "Balance Insufficient", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });

        dialog.show();
    }

    /**
     * Method refreshData yang akan memperbarui data account
     */
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
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }
}