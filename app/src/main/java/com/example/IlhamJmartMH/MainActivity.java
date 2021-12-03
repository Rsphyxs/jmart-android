package com.example.IlhamJmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(0);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
}