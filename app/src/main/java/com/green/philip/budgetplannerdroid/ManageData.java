package com.green.philip.budgetplannerdroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

public class ManageData extends AppCompatActivity {
    EditText editID;
    Button btnViewData;
    Button btnDeleteData;
    Button btnReturnToMain;
    Button btnFixedExpenditure;
    Button btnDeleteAllData;

    //HashMap to assign simpler ids to parse objects for individual deletion
    HashMap<Integer, String> id = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Cast EditText and Buttons
        editID = (EditText)findViewById(R.id.editText_ID);
        btnViewData = (Button)findViewById(R.id.button_viewData);
        btnDeleteData = (Button)findViewById(R.id.button_deleteData);
        btnFixedExpenditure = (Button)findViewById(R.id.button_toFixedExpenditure);
        btnDeleteAllData = (Button)findViewById(R.id.button_deleteAllData);
        btnReturnToMain = (Button)findViewById(R.id.button_returnToMain);

        toManageFixedExpenditure();
        deleteData();
        deleteAllData();
        toMainMenu();
        viewData();
    }

    //shows all data on the parse database to the screen
    //Runs in async task
    public void viewAllDataAsync(){
        btnViewData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final StringBuffer buffer = new StringBuffer();
                        ParseQuery<ParseObject> flexibleQuery = ParseQuery.getQuery("FlexibleExpenditure");
                        flexibleQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> expenseList, ParseException e) {
                                if (e == null) {
                                    for (ParseObject item: expenseList) {
                                        String amount = String.valueOf(item.getDouble("amount"));
                                        String details = item.getString("details");
                                        buffer.append("Category: Flexible Expenditure" + "\n");
                                        buffer.append("Amount: " + amount + "\n");
                                        buffer.append("Details: " + details + "\n" + "\n");
                                    }
                                    //showMessage("Data", buffer.toString());
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                        ParseQuery<ParseObject> fixedQuery = ParseQuery.getQuery("FixedExpenditure");
                        fixedQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    for (ParseObject item: objects) {
                                        String amount = String.valueOf(item.getDouble("amount"));
                                        String details = item.getString("details");
                                        buffer.append("Category: Fixed Expenditure" + "\n");
                                        buffer.append("Amount: " + amount + "\n");
                                        buffer.append("Details: " + details + "\n"+"\n");
                                    }
                                }
                                else {
                                    e.printStackTrace();
                                }
                            }
                        });
                        String message = buffer.toString();
                        if(message.isEmpty() == false){
                            showMessage("Data", message);
                        }
                        else{
                            Toast.makeText(ManageData.this, "problem with async task", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    //View Parse data in syncTask --> needs to run in a background task
    public void viewData(){
        btnViewData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuffer buffer = new StringBuffer();
                        ParseQuery<ParseObject> flexibleQuery = ParseQuery.getQuery("Expenditure");

                        //assigns value of count as key to the object id in HashMap id
                        int count = 1;
                        try {
                            //run query in synchronous task
                            List<ParseObject> results = flexibleQuery.find();
                            //loop through query
                            for(ParseObject result: results) {
                                //get id of object
                                String objectID = result.getObjectId();
                                //assign value of count as key to object id in HashMap id
                                id.put(count, objectID);
                                //get remaining object data
                                String amount = String.valueOf(result.getDouble("amount"));
                                String details = result.getString("details");
                                String category = result.getString("category");
                                //append data to StringBuffer buffer
                                buffer.append("ID: " + count + "\n");
                                buffer.append("Amount: " + amount + "\n");
                                buffer.append("Details: " + details + "\n");
                                buffer.append("Category: " + category + "\n" + "\n");
                                count ++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //convert StringBuffer to string
                        String message = buffer.toString();
                        //if StringBuffer is not empty print data to screen
                        if(message.isEmpty() == false){
                            showMessage("Data", message);
                        }
                        else{
                            Toast.makeText(ManageData.this, "No Data Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    //Delete data by id
    public void deleteData(){
        btnDeleteData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get id input from editID
                        String inputValue = editID.getText().toString();
                        //initialize key to hold the id input
                        int key = 0;
                        try{
                            key = Integer.parseInt(inputValue);
                        }catch(NumberFormatException e){
                            e.printStackTrace();
                        }
                        //if there was an input
                        if(key != 0){
                            //query the data in asynchronous task
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Expenditure");
                            query.getInBackground(id.get(key), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        //delete object in background
                                        object.deleteInBackground();
                                        Toast.makeText(ManageData.this, "Query Successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ManageData.this, "Query Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(ManageData.this, "Invalid Input", Toast.LENGTH_LONG).show();
                        }



                    }
                }
        );
    }

    //Delete all data in AsyncTask
    public void deleteAllData(){
        btnDeleteAllData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Expenditure");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e == null){
                                    //counts how many objects were deleted
                                    int count = 0;
                                    for(ParseObject object: objects){
                                        object.deleteInBackground();
                                        count++;
                                    }
                                    if(count > 0){
                                        Toast.makeText(ManageData.this, Integer.toString(count) + " Expenses Deleted", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(ManageData.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    Toast.makeText(ManageData.this, "No Data Found", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
        );
    }

    //Takes user to MangeFixedExpenditure activity
    public void toManageFixedExpenditure(){
        btnFixedExpenditure.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ManageData.this, ManageFixedExpenditure.class);
                        startActivity(i);
                    }
                }
        );
    }

    //Takes user to MainMenu
    public void toMainMenu(){
        btnReturnToMain.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ManageData.this, MainActivity.class);
                        startActivity(i);
                    }
                }
        );
    }

    //shows message to screen
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
