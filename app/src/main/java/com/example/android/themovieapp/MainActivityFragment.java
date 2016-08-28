package com.example.android.themovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecyclerViewAdapter.Clicklistener, TwoPaneGetData {

    private String sortByStr;
    private String posKey = "posKey";

    private int positionState = 0;

    private RecyclerView recyclerView;


    private FavoritesDbHelper mydb;

    private ArrayList<MoviesData> moviesDatas;
    private ArrayList<MoviesData> movie;

    TwoPaneGetData twoPaneInterface;


    public void setTwoPaneInterface(TwoPaneGetData twoPaneInterface) {
        this.twoPaneInterface = twoPaneInterface;
    }






    @Override
    public void onResume() {
        super.onResume();
    }


    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void updateMovies()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortByStr = prefs.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_default));
        if(sortByStr.equals("favorite"))
        {
            showFavoriteMovies();

        }
        else {

            FetchMovies movies = new FetchMovies();
            movies.execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refreshmovies, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_refresh)
        {
           updateMovies();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mydb = new FavoritesDbHelper(getContext());

        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);


        if(savedInstanceState != null && savedInstanceState.containsKey(posKey))
        {
            positionState = savedInstanceState.getInt(posKey);

        }


        return rootView;

    }

    public void showFavoriteMovies()
    {
        movie = new ArrayList<>();
        movie = mydb.getAll();
        if(movie != null)
        {

        RecyclerViewAdapter favArrayAdapter;

        favArrayAdapter = new RecyclerViewAdapter(getActivity(),movie);
        recyclerView.setAdapter(favArrayAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            if (positionState != -1)
                recyclerView.scrollToPosition(positionState);
        favArrayAdapter.setClickListener(new RecyclerViewAdapter.Clicklistener() {
            @Override
            public void itemClicked(View view, int position) {
                positionState = position;
                MoviesData movies =  movie.get(position);
//                Intent i = new Intent(getActivity(),DetailsActivity.class);
//                i.putExtra("extra_bundle",movies);
//                startActivity(i);
                twoPaneInterface.setSelectedMovie(movies);
            }
        });
        }
        else
        {
            Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void itemClicked(View view, int position) {


    }

    @Override
    public void setSelectedMovie(MoviesData movie) {

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(positionState != -1)
        {
            outState.putInt(posKey,positionState);
        }


    }

    public class FetchMovies extends AsyncTask<String, Void, ArrayList<MoviesData>>
    {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr;

        private ArrayList<MoviesData> getMoviesDataFromJson(String moviesJsonStr) throws JSONException
        {


            JSONObject forecastJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = forecastJson.getJSONArray("results");

            moviesDatas = new ArrayList<>();

            for(int i = 0; i <moviesArray.length(); i++)
            {


               JSONObject movieObject = moviesArray.getJSONObject(i);


                MoviesData movie = new MoviesData();

                movie.setPoster_path(movieObject.getString("poster_path"));
                movie.setTitle(movieObject.getString("title"));
                movie.setOverview(movieObject.getString("overview"));
                movie.setRate(movieObject.getString("vote_average"));
                movie.setRelease(movieObject.getString("release_date"));
                movie.setId(movieObject.getString("id"));
                moviesDatas.add(movie);

            }


            return moviesDatas;


        }



        @Override
        protected ArrayList<MoviesData> doInBackground(String... params) {

            try {

                URL url = null;

                if(sortByStr.equals("popular"))
                {
                    url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=9a005dd380ec772cf6045b8c370f8ef7");

                }

                else if(sortByStr.equals("top_rated"))
                {
                    url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=9a005dd380ec772cf6045b8c370f8ef7");

                }


                    HttpURLConnection.setFollowRedirects(false);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    urlConnection.setConnectTimeout(5000);



                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }

                moviesJsonStr = buffer.toString();


            }

            catch (SocketException e) {
                Log.e("Found ", "Error ", e);

                return null;
            }

            catch (IOException e) {
                Log.e("Found ", "Error ", e);

                return null;
            } finally {

               // Log.e("TMDB AP Throws: ", trailerJsonStr);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Found ", "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MoviesData> result) {
            if (result != null) {

                recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview);

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), moviesDatas);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                if (positionState != -1)
                    recyclerView.scrollToPosition(positionState);


                adapter.setClickListener(new RecyclerViewAdapter.Clicklistener() {
                    @Override
                    public void itemClicked(View view, int position) {

                        positionState = position;

                        MoviesData movie = moviesDatas.get(position);
                        twoPaneInterface.setSelectedMovie(movie);

                    }
                });

            }
        }


    }



}
