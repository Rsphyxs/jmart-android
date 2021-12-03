package com.example.IlhamJmartMH;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, productCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return view;
    }

}