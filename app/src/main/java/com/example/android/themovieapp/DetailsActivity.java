package com.example.android.themovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extrasBundle = getIntent().getExtras();
        if (savedInstanceState == null) {

            DetailsActivityFragment detailsFragment = new DetailsActivityFragment();
            detailsFragment.setArguments(extrasBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, detailsFragment).commit();

        }





    }


}
