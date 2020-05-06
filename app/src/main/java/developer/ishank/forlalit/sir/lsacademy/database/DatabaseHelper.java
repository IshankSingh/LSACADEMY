package developer.ishank.forlalit.sir.lsacademy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ls_apps.db";

    String TABLE_NAME;
    public DatabaseHelper(@Nullable Context context, String TABLE_NAME) {
        super(context, DATABASE_NAME, null, 1);
        this.TABLE_NAME = TABLE_NAME;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, AUTHOR TEXT, DOWNLOAD_STATUS BOOLEAN)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String title, String author, Boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", title);
        contentValues.put("AUTHOR", author);
        contentValues.put("DOWNLOAD_STATUS", status);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getDownloadStatus(String title, String author) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE TITLE = ? AND AUTHOR = ?";
        return db.rawQuery(query, new String[]{title, author});
    }
}
