package com.example.android.themovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reviews);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ReviewsActivityFragment()).commit();
        }



    }
}
