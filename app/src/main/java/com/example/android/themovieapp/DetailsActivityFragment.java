package com.example.android.themovieapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
public class DetailsActivityFragment extends Fragment{


    FavoritesDbHelper mydb;
    private String id;

    private ArrayList<String> reviewsDatas;


    TrailersData trailersData;
    ArrayAdapter trailersAdapter;
    ListView trailersList;
    ArrayList<TrailersData> trailersContent;
    MoviesData movies;
    String poster,title,rate,release,overview;
    boolean isInserted = false;

    ListView testListView;
    TextView titleText;
    ImageView moviePoster;
    TextView releaseDate;
    TextView Rate;
    TextView overView;


    public DetailsActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateReviews();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mydb = new FavoritesDbHelper(getContext());

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextWrapper = new ContextThemeWrapper(getActivity(),R.style.AppTheme);
        LayoutInflater localInflater = inflater.cloneInContext(contextWrapper);

        View rootView = localInflater.inflate(R.layout.fragment_details, container, false);

        View testView = inflater.inflate(R.layout.header,null,false);
        testListView = (ListView) rootView.findViewById(R.id.testListView);
        testListView.addHeaderView(testView);

        titleText = (TextView) rootView.findViewById(R.id.movieTitle);
        moviePoster = (ImageView) rootView.findViewById(R.id.detailsMoviePoster);
        releaseDate = (TextView) rootView.findViewById(R.id.releaseyear);
        Rate = (TextView) rootView.findViewById(R.id.movieRate);
        overView = (TextView) rootView.findViewById(R.id.movieDescription);

        MoviesData movie = new MoviesData();
        movie = getArguments().getParcelable("extra_bundle");

        titleText.setText(movie.getTitle());
        Picasso.with(getContext()).load(movie.getPoster_path()).into(moviePoster);
        releaseDate.setText(movie.getRelease());
        Rate.setText(movie.getRate());
        overView.setText(movie.getOverview());





        id = movie.getId();
        poster = movie.getPoster_path();
        title = movie.getTitle();
        rate = movie.getRate();
        release = movie.getRelease();
        overview = movie.getOverview();


        Button reviewsButton = (Button) rootView.findViewById(R.id.movieReviews);
        reviewsDatas = new ArrayList<>();



        updateReviews();
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ReviewsActivity.class);
                i.putStringArrayListExtra("reviewsList", reviewsDatas);
                startActivity(i);
            }
        });


        trailersList = (ListView) rootView.findViewById(R.id.testListView);
        updateTrailers();




        movies = new MoviesData();
        movies.setId(id);
        movies.setPoster_path(poster);
        movies.setTitle(title);
        movies.setRate(rate);
        movies.setRelease(release);
        movies.setOverview(overview);

        final Button addToFavorite = (Button) rootView.findViewById(R.id.addtofav);

        changeFavButton(mydb, movie, addToFavorite);
        addToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mydb.insertToDb(movies))
                {
                    isInserted = true;
                    addToFavorite.setText(getResources().getText(R.string.favorited).toString().toUpperCase());
                    Toast.makeText(getActivity(),getResources().getText(R.string.added_to_fav).toString().toUpperCase(),Toast.LENGTH_LONG).show();
                }

                if(mydb.checkIfExists(movies.getId()) && !isInserted)
                {
                    mydb.removeFromDb(movies.getId());
                    addToFavorite.setText(getResources().getText(R.string.add_to_fav).toString().toUpperCase());
                    Toast.makeText(getActivity(),getResources().getText(R.string.removed_from_fav).toString().toUpperCase(),Toast.LENGTH_LONG).show();
                }

                isInserted = false;
            }

        });






        return rootView;
    }


    private void changeFavButton(FavoritesDbHelper db, MoviesData movie, Button button)
    {
        if(db.checkIfExists(movie.getId()))
        {
            button.setText("FAVORITED");
        }
    }

    private void updateReviews()
    {
        FetchReviews fetchReviews = new FetchReviews();
        fetchReviews.execute();
    }

    private void updateTrailers()
    {
        FetchTrailers fetchTrailers = new FetchTrailers();
        fetchTrailers.execute();
    }





    public class FetchReviews extends AsyncTask<String,Void,ArrayList<String>> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewsJsonStr;


        private ArrayList<String> getReviewsFromJson(String reviewsJsonStr) throws JSONException {


            JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
            JSONArray reviewsArray = reviewsJson.getJSONArray("results");

            String author, content;
            ArrayList<String> resultStrs = new ArrayList<>();
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject reviewsObject = reviewsArray.getJSONObject(i);
                author = reviewsObject.getString("author");
                content = reviewsObject.getString("content");
                resultStrs.add(getString(R.string.author) + " " + author + "\n" + getString(R.string.content) + "\n"  + content );
            }

            return resultStrs;
        }


        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {

                URL url;
                url = new URL("http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=9a005dd380ec772cf6045b8c370f8ef7");

                HttpURLConnection.setFollowRedirects(false);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

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

                reviewsJsonStr = buffer.toString();
                Log.v("Reviews JSON ", reviewsJsonStr);


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
                return getReviewsFromJson(reviewsJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {

            if(result != null) {
                reviewsDatas.clear();
                reviewsDatas.addAll(result);
            }

        }
    }

    public class FetchTrailers extends AsyncTask<String,Void,ArrayList<TrailersData>>
    {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailerJsonStr;



        private ArrayList<TrailersData> getTrailersFromJson(String moviesJsonStr) throws
                JSONException
        {


            JSONObject trailersJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = trailersJson.getJSONArray("results");

            trailersContent = new ArrayList<>();



            for(int i = 0; i <moviesArray.length(); i++)
            {

                trailersData = new TrailersData();

                JSONObject movieObject = moviesArray.getJSONObject(i);
//                Log.v("Trailer URL ", trailerURL);
                trailersData.setTrailerURL(movieObject.getString("key"));
                trailersData.setTrailerName(movieObject.getString("name"));
                trailersContent.add(trailersData);


            }

            return trailersContent;
        }





        @Override
        protected ArrayList<TrailersData> doInBackground(String... params) {

            try {

                URL url;
                url = new URL("http://api.themoviedb.org/3/movie/"+id+"/videos?api_key=9a005dd380ec772cf6045b8c370f8ef7");

                HttpURLConnection.setFollowRedirects(false);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

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

                trailerJsonStr = buffer.toString();


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
                return getTrailersFromJson(trailerJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<TrailersData> trailersResult) {


            if(trailersContent != null) {
            trailersAdapter = new ArrayAdapter(getActivity(),R.layout.trailersonerow,R.id.trailersonerowtext,trailersContent);

                trailersList.setAdapter(trailersAdapter);
                trailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailersContent.get(position - 1).getTrailerURL()));
                            startActivity(intent);

                    }
                });
            }


        }

    }
}

