package com.green.philip.budgetplannerdroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by Philip on 12/1/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "finances.db";
    public static final String TABLE_NAME = "finances_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CATEGORY";
    public static final String COL_3 = "AMOUNT";
    public static final String COL_4 = "DETAILS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    public void onCreate(SQLiteDatabase db) {
        //Create query. when called it will execute query and create table
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY TEXT, AMOUNT TEXT, DETAILS TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    //Insert Data into database
    public boolean insertFlexibleData(String amount, String details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //put data into contentValues 2 args: (1) Column (2) data
        contentValues.put(COL_2, "Flexible_Spending");
        contentValues.put(COL_3, amount);
        contentValues.put(COL_4, details);
        //Insert content into database three args: (1) table name (2) null (3) contentValues object
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertFixedData(String amount, String details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, "Fixed_Spending");
        contentValues.put(COL_3, amount);
        contentValues.put(COL_4, details);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getAllData() {
        //write query to get all the data
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }

    public String getFlexibleData(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor result = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_2 + "=Flexible Spending", null);
        //return result;
        String[] columns = {DatabaseHelper.COL_3, DatabaseHelper.COL_4};
        String[] param = {"Flexible_Spending"};
        Cursor result = db.query(TABLE_NAME, columns, COL_2 + "=?", param, null, null, null);
        StringBuffer sb = new StringBuffer();
        while(result.moveToNext()){
            int index1 = result.getColumnIndex(DatabaseHelper.COL_3);
            String amount = result.getString(index1);
            int index2 = result.getColumnIndex(DatabaseHelper.COL_4);
            String details = result.getString(index2);
            sb.append("Amount: " + amount + "\n");
            sb.append("Details: " + details + "\n"+"\n");
        }
        return sb.toString();
    }

    public String getFixedData(){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {DatabaseHelper.COL_3, DatabaseHelper.COL_4};
        String[] param = {"Fixed_Spending"};

        Cursor result = db.query(TABLE_NAME, columns, COL_2 + "=?", param, null, null, null);

        StringBuffer sb = new StringBuffer();
        while(result.moveToNext()){
            int index1 = result.getColumnIndex(DatabaseHelper.COL_3);
            String amount = result.getString(index1);
            int index2 = result.getColumnIndex(DatabaseHelper.COL_4);
            String details = result.getString(index2);
            sb.append("Amount: " + amount +  "\n");
            sb.append("Details: " + details + "\n");
        }
        return sb.toString();
    }

    public double getFlexibleSpendingTotal(){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {DatabaseHelper.COL_3};
        String[] param = {"Flexible_Spending"};

        Cursor result = db.query(TABLE_NAME, columns, COL_2 + "=?", param, null, null, null);

        double total = 0;

        while(result.moveToNext()){
            int index = result.getColumnIndex(DatabaseHelper.COL_3);
            String amount = result.getString(index);
            double parserDoub = Double.parseDouble(amount);
            total += parserDoub;
        }
        return total;
    }

    public double getFixedSpendingTotal(){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {DatabaseHelper.COL_3};
        String[] param = {"Fixed_Spending"};

        Cursor result = db.query(TABLE_NAME, columns, COL_2 + "=?", param, null, null, null);

        double total = 0;

        while(result.moveToNext()){
            int index = result.getColumnIndex(DatabaseHelper.COL_3);
            String amount = result.getString(index);
            double parserDoub = Double.parseDouble(amount);
            total += parserDoub;
        }

        return total;
    }


    /*public boolean updateData(String id, String category, String amount, String details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, category);
        contentValues.put(COL_3, amount);
        contentValues.put(COL_4, details);
        //id = ? query by id
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        return true;
    }*/

    public Integer deleteRow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String [] {id});
    }

    //returns 0 if no data deleted
    public int deleteAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();

        LinkedList<Integer> idList = getAllID();
        Cursor result = getAllData();
        if(idList.isEmpty()){
            return 0;
        }
        else{
            for(int value:idList){
                String str = Integer.toString(value);
                db.delete(TABLE_NAME, "ID = ?", new String [] {str});
            }
            return 1;
        }

    }

    public LinkedList<Integer> getAllID(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);

        LinkedList<Integer> idList = new LinkedList<Integer>();

        while(result.moveToNext()){
            int index = result.getColumnIndex(DatabaseHelper.COL_1);
            int id = result.getInt(index);
            idList.add(id);
        }
        return idList;
    }

}
