package com.nguyentinhdeveloper.ghichu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nguyentinhdeveloper.ghichu.model.ModelNote;


public class BaseData {

    private final String DB_NAME = "NOTE";
    private final String TB_NAME = "Note";
    private final String TB_NAME_1 = "ID";
    private final int DB_VERSION = 1;
    private SQLiteDatabase database;

    public BaseData(Context context) {
        OpenHelper helper = new OpenHelper(context);
        database = helper.getWritableDatabase();

    }

    public class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS Note(_id INTEGER PRIMARY KEY AUTOINCREMENT,date NVARCHAR,note NVARCHAR,img NVARCHAR,id_note NVARCHAR,subject NVARCHAR,color NVARCHAR,star NVARCHAR)");
            db.execSQL("CREATE TABLE IF NOT EXISTS ID(_id INTEGER PRIMARY KEY AUTOINCREMENT,id_note NVARCHAR)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Note");
            db.execSQL("DROP TABLE IF EXISTS ID");
            onCreate(db);
        }
    }

    public long insert(ModelNote modelNote) {
        ContentValues values = new ContentValues();
        values.put("date", modelNote.getDate());
        values.put("note", modelNote.getNode());
        values.put("img", modelNote.getImg());
        values.put("id_note", modelNote.getId());
        values.put("subject", modelNote.getSubject());
        values.put("color", modelNote.getAudio());
        values.put("star", modelNote.getStar());
        return database.insert(TB_NAME, null, values);
    }

    public long insert_id(String id){
        ContentValues values = new ContentValues();
        values.put("id_note", id);
        return database.insert(TB_NAME_1, null, values);
    }


    public void update(String date, String note, String img, int id, String subject, String color, String star) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("note", note);
        values.put("img", img);
        values.put("subject", subject);
        values.put("color", color);
        values.put("star", star);
        database.update(TB_NAME, values, "_id=" + id, null);
    }

    public void delete(int id) {
        database.delete(TB_NAME, "_id=" + id, null);
    }

    public void deleteTABLE(){
        database.execSQL("DELETE FROM Note");
    }
    public void deleteTABLE_ID(){
        database.execSQL("DELETE FROM ID");
    }

    public Cursor getData() {
        return database.query(TB_NAME, null, null, null, null, null, null);
    }
    public Cursor getDataa() {
        return database.query(TB_NAME_1, null, null, null, null, null, null);
    }
}
