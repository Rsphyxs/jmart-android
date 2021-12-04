package com.example.IlhamJmartMH;

import static com.example.IlhamJmartMH.ProductsFragment.productList;
import static com.example.IlhamJmartMH.ProductsFragment.tes;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.IlhamJmartMH.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment implements View.OnClickListener {

    private EditText editName, editLowestprice, editHighestprice;
    private CheckBox checkBoxNew, checkBoxUsed;
    private Spinner spinnerCategory;
    private Button buttonApply, buttonCancel;

    public static String[] productCategory = {"BOOK", "KITCHEN", "ELECTRONIC", "FASHION", "GAMING", "GADGET", "MOTHERCARE", "COSMETICS",
            "HEALTHCARE", "FURNITURE", "JEWELRY", "TOYS", "FNB", "STATIONERY", "SPORTS", "AUTOMOTIVE",
            "PETCARE", "ART_CRAFT", "CARPENTRY", "MISCELLANEOUS", "PROPERTY", "TRAVEL", "WEDDING"};


    public FilterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            ProductsFragment.newInstance().ShowProductList(0,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        editName = view.findViewById(R.id.filterName);
        editLowestprice = view.findViewById(R.id.filterLowerprice);
        editHighestprice = view.findViewById(R.id.filterHighestprice);
        checkBoxNew = view.findViewById(R.id.checkboxNew);
        checkBoxUsed = view.findViewById(R.id.checkboxUsed);
        buttonApply = view.findViewById(R.id.buttonApply);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonApply.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        spinnerCategory = (Spinner) view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, productCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonApply){
            String dataName = editName.getText().toString();
            int dataLowestPrice = Integer.valueOf(editLowestprice.getText().toString());
            int dataHighestPrice = Integer.valueOf(editHighestprice.getText().toString());
//            boolean isEmptyFields = false;
//            if (TextUtils.isEmpty) {
//                isEmptyFields = true;
//                edtLength.setError("Field ini tidak boleh kosong");
//            }
//            if (!isEmptyFields) {
//                double volume = Double.valueOf(inputLength) * Double.valueOf(inputWidth) * Double.valueOf(inputHeight);
//                tvResult.setText(String.valueOf(volume));
//            }
        }
    }
}