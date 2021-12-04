package com.bastel2020.ontrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
}
