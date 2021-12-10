package com.bastel2020.ontrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DbContext {
    private static SQLiteDatabase db;

    public DbContext(Context context)
    {
        db = context.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
    }

    public static String GetToken() //Капец как небезопасно, согласны
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS token (token TEXT)");
        Cursor query = db.rawQuery("SELECT * FROM token;", null);
        if(query.moveToFirst()){

            String name = query.getString(0);
            return  name;
        }
        return null;
    }

    public static void SaveToken(String tok) //Капец как небезопасно, согласны
    {
        db.execSQL("DROP TABLE IF EXISTS token");
        db.execSQL("CREATE TABLE token (token TEXT)");
        ContentValues content = new ContentValues();
        content.put("token", tok);
        db.insert("token", null, content);
    }

    public static boolean RemovePlaceFromFavorites(int placeId)
    {
        if(ContainsPlaceInFavorites(placeId))
        {
            db.delete("favorite_places", "placeId =?", new String[]{String.valueOf(placeId)});
            return true;
        }
        return false;
    }

    public static boolean AddPlaceToFavorites(int placeId, String placeName, String placeTextLocation)
    {
        if(!ContainsPlaceInFavorites(placeId))
        {
            ContentValues content = new ContentValues();
            content.put("placeId", placeId);
            content.put("name", placeName);
            content.put("text_location", placeTextLocation);
            db.insert("favorite_places", null, content);
            return true;
        }
        return false;
    }

    public static boolean ContainsPlaceInFavorites(int placeId)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS favorite_places (placeId INTEGER, name TEXT, text_location TEXT)");
        Cursor query = db.rawQuery("SELECT * FROM favorite_places WHERE placeId = " + placeId, null);
        if(query.moveToFirst())
        {
            return true;
        }
        return false;
    }

    public static List<TripLiteModel> GetFavoritesPlaces()
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS favorite_places (placeId INTEGER, name TEXT, text_location TEXT)");
        Cursor query = db.rawQuery("SELECT * FROM favorite_places;", null);
        List<TripLiteModel> favoritesTrips = new ArrayList<>();
        while(query.moveToNext())
        {
            int tempId = query.getInt(0);
            String tempName = query.getString(1);
            String tempLocation = query.getString(2);
            favoritesTrips.add(new TripLiteModel(tempId, tempName, tempLocation));
        }
        return favoritesTrips;
    }

    public static class TripLiteModel
    {
        public int Id;
        public String Name;
        public String TextLocation;

        public TripLiteModel(int id, String name, String textLocation)
        {
            Id = id;
            Name = name;
            TextLocation = textLocation;
        }
    }
}
