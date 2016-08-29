package com.example.android.themovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.themovieapp.*;
import com.example.android.themovieapp.Fragments.DetailsActivityFragment;
import com.example.android.themovieapp.Fragments.MainActivityFragment;
import com.example.android.themovieapp.Data.MoviesData;
import com.example.android.themovieapp.Interfaces.TwoPaneGetData;

public class MainActivity extends AppCompatActivity implements TwoPaneGetData {

    boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(findViewById(R.id.details_container) != null)
            twoPane = true;
        else
            twoPane = false;

        if(savedInstanceState == null)
        {
            MainActivityFragment mainFragment = new MainActivityFragment();
            mainFragment.setTwoPaneInterface(this);
            getSupportFragmentManager().beginTransaction().add(R.id.main_container,mainFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingActivity.class));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedMovie(MoviesData movie) {

        if(twoPane)
        {
            FrameLayout detailsFrame = (FrameLayout) findViewById(R.id.details_container);
            detailsFrame.setBackgroundColor(getResources().getColor(R.color.detailsTwoPane));
            DetailsActivityFragment detailsFragment = new DetailsActivityFragment();
            Bundle detailsBundle = new Bundle();
            detailsBundle.putParcelable("extra_bundle",movie);
            detailsFragment.setArguments(detailsBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container,detailsFragment).commit();
        }
        else
        {
            Intent intent = new Intent(this,DetailsActivity.class);
            intent.putExtra("extra_bundle",movie);
            startActivity(intent);
        }
    }

}
