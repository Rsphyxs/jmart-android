package com.example.IlhamJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerstoreButton;
    private Button cancelButton;
    private LinearLayout registerstoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        registerstoreButton = findViewById(R.id.registerstoreButton);
        registerstoreLayout = findViewById(R.id.registerstoreLayout);
        cancelButton = findViewById(R.id.cancelButton);
        registerstoreButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.registerstoreButton){
            registerstoreButton.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(registerstoreLayout);
            registerstoreLayout.setVisibility(View.VISIBLE);
        }
        else if(v.getId() == R.id.cancelButton){
            TransitionManager.beginDelayedTransition(registerstoreLayout);
            registerstoreLayout.setVisibility(View.GONE);
            registerstoreButton.setVisibility(View.VISIBLE);
        }
    }
}