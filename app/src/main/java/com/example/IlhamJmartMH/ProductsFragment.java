package com.example.IlhamJmartMH;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Product;
import com.example.IlhamJmartMH.request.RequestFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment implements View.OnClickListener {

    public static List<Product> productList = new ArrayList<>();
    public static List<String> productnameList = new ArrayList<>();
    public static boolean tes = false;
    private static final Gson gson = new Gson();
    private EditText editPage;
    private ListView listView;
    private Button buttonGo, buttonNext, buttonPrev;
    private int page;

    public ProductsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        listView = view.findViewById(R.id.productListview);
        editPage = view.findViewById(R.id.pageNumber);
        buttonGo = view.findViewById(R.id.buttonGo);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonPrev = view.findViewById(R.id.buttonPrev);
        buttonGo.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrev.setOnClickListener(this);
        page = 0;

        ShowProductList(page, 1);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonGo){
            page = Integer.valueOf(editPage.getText().toString());
            page--;
            ShowProductList(page, 1);
        }
        else if(v.getId() == R.id.buttonNext){
            page++;
            ShowProductList(page, 1);
        }
        else if(v.getId() == R.id.buttonPrev){
            if(page == 0){
                Toast.makeText(getActivity(), "Already in first page", Toast.LENGTH_SHORT).show();
            }
            else{
                page--;
                ShowProductList(page, 1);
            }
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
                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.product_list_view, productnameList);
                listView.setAdapter(adapter);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ProductFragmentError", "" + error.toString());
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(RequestFactory.getPage("product", page, pageSize, stringListener, errorListener));
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.product_list_view, productnameList);
        listView.setAdapter(adapter);
    }

}