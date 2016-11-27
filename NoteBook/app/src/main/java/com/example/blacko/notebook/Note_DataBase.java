package com.example.blacko.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Note_DataBase {
    public static final String
            KEY_ROWID = "_id",
            KEY_TITLE = "title",
            KEY_BODY  = "body";

    private static final int DATABASE_VERSION = 2;

    private static final String
            DATABASE_NAME = "database",
            TABLE_NAME = "notes";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL( "create table notes (_id integer primary key autoincrement, title text not null, body text not null);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    private DatabaseHelper DbHelper;
    private SQLiteDatabase Db;
    private final Context Ctx;

    public Note_DataBase(Context ctx) { this.Ctx = ctx; }

    public Note_DataBase open() {
        DbHelper = new DatabaseHelper(Ctx);
        Db = DbHelper.getWritableDatabase();
        return this;
    }

    public void close() { DbHelper.close(); }

    public long createNote(String title, String body) {
        ContentValues value = new ContentValues();
        value.put(KEY_TITLE, title);
        value.put(KEY_BODY , body);
        return Db.insert(TABLE_NAME, null, value);
    }

    public long deleteNote(long rowId) {
        return Db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null);
    }

    public long updateNote(long rowId, String title, String body) {
        ContentValues value = new ContentValues();
        value.put(KEY_TITLE, title);
        value.put(KEY_BODY, body);
        return Db.update(TABLE_NAME, value, KEY_ROWID + "=" + rowId, null);
    }

    public Cursor fetchNote(long rowId) {
        Cursor mCursor = Db.query(true, TABLE_NAME, new String[]{KEY_ROWID, KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllNotes() {
        return Db.query(TABLE_NAME, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY}, null, null, null, null, null);
    }
}
