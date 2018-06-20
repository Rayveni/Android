package com.kart.track24;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import  android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class database_handler extends SQLiteOpenHelper implements idb_handler {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private   String create_table;
    private String  TABLE_CONTACTS;
    //private static  String TABLE_CONTACTS = "";

    public database_handler(Context context,String create_table,String table_name) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //create_table= create_table.format(table_name);
        TABLE_CONTACTS=table_name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TAG123",create_table );
        //String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
        //        + KEY_ID + " INTEGER PRIMARY KEY , " + KEY_NAME + " TEXT, " + KEY_PH_NO + " TEXT ) " ;

        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    @Override
    public void addContact(track contact) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    @Override
    public track getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        track contact = new track(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        cursor.close();
        return contact;
    }

    //@Override
    public List<track> getAllContacts() {
        List<track> contactList = new ArrayList<track>();
        String selectQuery = "SELECT  * FROM " +TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                track contact = new track();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    @Override
    public int updateContact(track contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    @Override
    public void deleteContact(track contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    @Override
    public int getContactsCount() {
        int count=0;
        String countQuery = "SELECT  count(*) FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();

            count=cursor.getInt(0);
        }


        cursor.close();

        return count;//cursor.getCount();
    }

    public void execSQL(boolean write,String sql_text) {
        SQLiteDatabase db;
        if (write)
        {
             db = this.getWritableDatabase();
        }
        else
        {
             db = this.getReadableDatabase();
        }
        //SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL(sql_text);
    }


    public String print_table( String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Log.d("dbTag", "getTableAsString called");
        // String tableString ="";//= String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);

        List<String> string_arr = new ArrayList();
        String[] columnNames = allRows.getColumnNames();
        String tableString=TextUtils.join("|",columnNames)+"\n";
        if (allRows.moveToFirst() ){



            do {
                string_arr.clear();
                for (String name: columnNames) {
                    string_arr.add(allRows.getString(allRows.getColumnIndex(name)));
                   // tableString += String.format("%s: %s\n", name,
                     //     allRows.getString(allRows.getColumnIndex(name)));

                    //tableString=tableString+"\n"+tableString;
                }

                tableString = tableString+TextUtils.join("|",string_arr)+"\n";

            } while (allRows.moveToNext());
        }
        allRows.close();
        return String.format("table %s \n",tableName)+tableString;
    }


    //Map staff = new HashMap< String, Employee>();
    public List<Map>  dataframe( String tableName) {
        Map<String,String> row = new HashMap<String,String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);

        List<Map> result = new ArrayList();


        String[] columnNames = allRows.getColumnNames();


        if (allRows.moveToFirst() ){



            do {

                for (String name: columnNames) {
                    row.put(name,allRows.getString(allRows.getColumnIndex(name)));



                    // tableString += String.format("%s: %s\n", name,
                    //     allRows.getString(allRows.getColumnIndex(name)));

                    //tableString=tableString+"\n"+tableString;
                }

                result.add(row);
                Log.d("db1235",Integer.toString(result.get(0).size()));
                row.clear();

            } while (allRows.moveToNext());
        }
        Log.d("db12356",Integer.toString(result.get(0).size()));
        allRows.close();
        return result;
    }

    public String[] column_names( String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();
        //Log.d("dbTag", "getTableAsString called");
        String tableString ="";//= String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName+" LIMIT 1", null);


        String[] columnNames = allRows.getColumnNames();

        allRows.close();
        return columnNames;
    }

    public String[][]  dataframe2( String tableName) {
        int i=0;int j=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        String[] columnNames = allRows.getColumnNames();

        String[][] result;
        result = new String[allRows.getCount()][columnNames.length];






        if (allRows.moveToFirst() ){



            do {
                j=0;
                for (String name: columnNames) {
                    result[i][j]=allRows.getString(allRows.getColumnIndex(name));
                    j+=1;

                }

                i+=1;

            } while (allRows.moveToNext());
        }

        allRows.close();
        return result;
    }
}