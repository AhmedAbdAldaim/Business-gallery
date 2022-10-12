package com.example.businesgalleryadmin.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlliteLove extends SQLiteOpenHelper {
    private static final String MYDB = "love.db";
    private static final String MYTAB = "work_table";
    private static final int VERSION = 1;

    //ROW
    private static final String COL_ID = "id";


    public SqlliteLove(@Nullable Context context) {
        super(context, MYDB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String create = "CREATE TABLE " + SqlliteLove.MYTAB + " ("
                + SqlliteLove.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT "
                + " );";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
         sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +SqlliteLove.MYTAB );
    }

    //iNSERT
    public void insert(String work_id){
        SQLiteDatabase databaseinset = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlliteLove.COL_ID,work_id);
        databaseinset.insert(SqlliteLove.MYTAB,null,contentValues);
    }


    public Cursor show(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM work_table",null);
        return cursor;
    }

    public int delete(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(SqlliteLove.MYTAB,"id=?",new String[]{String.valueOf(id)});
    }

}
