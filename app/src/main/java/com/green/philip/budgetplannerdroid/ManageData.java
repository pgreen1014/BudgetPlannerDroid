package com.green.philip.budgetplannerdroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import helperClasses.ParseHelper;
import helperClasses.ParserHelper;

public class ManageData extends AppCompatActivity {
    EditText editID;
    Button btnViewData;
    Button btnDeleteData;
    Button btnReturnToMain;
    Button btnFixedExpenditure;
    Button btnDeleteAllData;
    private final static String TAG = "ManageData";

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
    private void viewData(){
        btnViewData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Initialize StringBuffer object
                        StringBuilder sb = new StringBuilder();

                        //query Parse for all data and save as a list of ParseObjects
                        List<ParseObject> results = ParseHelper.getAllData();

                        int count = 1;
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
                            sb.append("ID: ").append(count).append("\n");
                            sb.append("Amount: ").append(amount).append("\n");
                            sb.append("Details: ").append(details).append("\n");
                            sb.append("Category: ").append(category).append("\n").append("\n");

                            //increment count
                            count ++;
                        }
                        //convert StringBuilder to string
                        String message = sb.toString();
                        //if StringBuffer is not empty print data to screen
                        if(!message.isEmpty()){
                            showMessage("Data", message);
                        }
                        else{
                            Toast.makeText(ManageData.this, "Unable to Retrieve Data from Server", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    //Delete data by id
    private void deleteData(){
        btnDeleteData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get id input from editID
                        String inputValue = editID.getText().toString();

                        //Parse inputValue to int
                        int key = ParserHelper.parseInt(inputValue);

                        //if there was an input
                        if(key != 0){
                           ParseHelper.deleteObject(id.get(key));
                            Toast.makeText(ManageData.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ManageData.this, "Invalid Input", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "unable to delete object based on user id input");
                        }

                    }
                }
        );
    }

    //Delete all data in AsyncTask
    private void deleteAllData(){
        btnDeleteAllData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Delete all data asynchronously
                        ParseHelper.deleteAllData();
                        Toast.makeText(ManageData.this, "All Data Deleted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    //Takes user to MangeFixedExpenditure activity
    private void toManageFixedExpenditure(){
        btnFixedExpenditure.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ManageData.this, ManageFixedExpenditure.class));
                    }
                }
        );
    }

    //Takes user to MainMenu
    private void toMainMenu(){
        btnReturnToMain.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ManageData.this, MainActivity.class));
                    }
                }
        );
    }

    //shows message to screen
    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
