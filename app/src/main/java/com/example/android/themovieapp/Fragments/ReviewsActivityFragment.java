package com.example.android.themovieapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.themovieapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewsActivityFragment extends Fragment {

    //ListView listView;
    @BindView(R.id.reviewsList) ListView listView;
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private ArrayAdapter adapter;
    private ArrayList<String> reviewsArrayList;

    public ReviewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this,rootView);

        reviewsArrayList = new ArrayList<>();
        Intent i = getActivity().getIntent();
        reviewsArrayList = i.getStringArrayListExtra("reviewsList");

                 //listView = (ListView) rootView.findViewById(R.id.reviewsList);
                adapter = new ArrayAdapter<>(getActivity(),R.layout.reviewsonerow,R.id.reviewsonerowtext,reviewsArrayList);
                listView.setAdapter(adapter);

        return rootView;
    }
}
