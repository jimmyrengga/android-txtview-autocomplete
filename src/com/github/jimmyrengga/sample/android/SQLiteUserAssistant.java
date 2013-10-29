package com.github.jimmyrengga.sample.android;

import android.database.*;
import android.database.sqlite.*;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 10/28/13.
 */
public class SQLiteUserAssistant extends SQLiteOpenHelper {

    private static final String DB_NAME = "usingsqllite.db";
    private static final int DB_VERSION_NUMBER = 1;
    private static final String DB_TABLE_NAME = "user";
    private static final String DB_COLUMN_1_NAME = "username";
    private static final String DB_COLUMN_2_NAME = "fullname";

    private static final String DB_CREATE_SCRIPT = "create table " + DB_TABLE_NAME +
            " (username text primary key, fullname text not null);)";

    private SQLiteDatabase sqliteDBInstance = null;

    public SQLiteUserAssistant(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION_NUMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO: Implement onUpgrade
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDBInstance)
    {
        Log.i("onCreate", "Creating the database...");
        sqliteDBInstance.execSQL(DB_CREATE_SCRIPT);
        //check if db exist
    }

    public void openDB() throws SQLException
    {
        Log.i("openDB", "Checking sqliteDBInstance...");
        if(this.sqliteDBInstance == null)
        {
            Log.i("openDB", "Creating sqliteDBInstance...");
            this.sqliteDBInstance = this.getWritableDatabase();
        }
    }

    public void closeDB()
    {
        if(this.sqliteDBInstance != null)
        {
            if(this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
        }
    }

    public long insertUser(String username, String fullname)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_COLUMN_1_NAME, username);
        contentValues.put(DB_COLUMN_2_NAME, fullname);
        Log.i(this.toString() + " - insertUser", "Inserting: " + username + ", " + fullname);
        return this.sqliteDBInstance.insertWithOnConflict(DB_TABLE_NAME, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean removeUser(String username)
    {
        int result = this.sqliteDBInstance.delete(DB_TABLE_NAME, "username='" + username + "'", null);

        if(result > 0)
            return true;
        else
            return false;
    }

    public long updateUser(String oldUsername, String newUsername)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_COLUMN_1_NAME, newUsername);
        return this.sqliteDBInstance.update(DB_TABLE_NAME, contentValues, "username='" + oldUsername + "'", null);
    }

    public List<User> getAllUsers()
    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME, DB_COLUMN_2_NAME}, null, null, null, null, null);
        List<User> users = new ArrayList<User>();
        if(cursor.getCount() >0)
        {
//            String[] str = new String[cursor.getCount()];
//            int i = 0;
            while (cursor.moveToNext())
            {
//                str[i] = cursor.getString(cursor.getColumnIndex(DB_COLUMN_1_NAME));
//                i++;
                User u = new User();
                u.setUsername(cursor.getString(cursor.getColumnIndex(DB_COLUMN_1_NAME)));
                u.setFullname(cursor.getString(cursor.getColumnIndex(DB_COLUMN_2_NAME)));

                users.add(u);
            }
            return users;
        }
        else
        {
            return new ArrayList<User>();
        }
    }

}
