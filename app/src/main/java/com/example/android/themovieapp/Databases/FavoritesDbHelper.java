package com.example.android.themovieapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.example.android.themovieapp.Data.MoviesData;

import java.util.ArrayList;

/**
 * Created by Ahmed on 8/12/2016.
 */
public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Favorites.db";
    public static final String FAV_TABLE_NAME = "favorites_table";
    public static final String FAV_COLUMN_ID = "id";
    public static final String FAV_COLUMN_POSTER = "poster";
    public static final String FAV_COLUMN_TITLE = "title";
    public static final String FAV_COLUMN_RATE = "rate";
    public static final String FAV_COLUMN_RELEASE = "release";
    public static final String FAV_COLUMN_OVERVIEW = "overview";


    public FavoritesDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table favorites_table " +
                        "(id integer primary key, poster text, title text, rate text, release text, overview text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorites_table");
        onCreate(db);
    }

    public boolean insertToDb(MoviesData movie)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(checkIfExists(movie.getId()) == false) {
            contentValues.put("id", movie.getId());
            contentValues.put("poster", movie.getPoster_path());
            contentValues.put("title", movie.getTitle());
            contentValues.put("rate", movie.getRate());
            contentValues.put("release", movie.getRelease());
            contentValues.put("overview", movie.getOverview());

            db.insert("favorites_table", null, contentValues);
        }
        else
        {return false;}

        return true;
    }

    public Integer removeFromDb(String id)
    {

         SQLiteDatabase db = this.getWritableDatabase();
         return db.delete(FAV_TABLE_NAME,"id = ?",new String[]{ id });
    }

    public boolean checkIfExists(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String check = "Select * from " + FAV_TABLE_NAME + " where " + FAV_COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(check, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public ArrayList<MoviesData> getAll()
    {
        ArrayList<MoviesData> array_list = new ArrayList<MoviesData>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from favorites_table", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            MoviesData movie = new MoviesData();
            movie.setId(res.getString(0));
            movie.setPoster_path(res.getString(1));
            movie.setTitle(res.getString(2));
            movie.setRate(res.getString(3));
            movie.setRelease(res.getString(4));
            movie.setOverview(res.getString(5));
            array_list.add(movie);
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }


    public void deleteAll ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete("favorites_table",null,null);
    }

    public class DBOperations extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            getAll();

            return null;
        }
    }
}
