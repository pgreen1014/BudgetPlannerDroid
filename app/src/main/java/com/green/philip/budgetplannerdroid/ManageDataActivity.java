package com.green.philip.budgetplannerdroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.ParseHelper;
import helperClasses.ParserHelper;

public class ManageDataActivity extends AppCompatActivity {
    @Bind(R.id.editText_ID) EditText mEditID;
    private final static String TAG = "ManageDataActivity";

    //HashMap to assign simpler ids to parse objects for individual deletion
    HashMap<Integer, String> id = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

    }

    //View Parse data in syncTask --> needs to run in a background task
    @OnClick(R.id.button_viewData) protected void viewData() {
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
            Toast.makeText(ManageDataActivity.this, "Unable to Retrieve Data from Server", Toast.LENGTH_LONG).show();
        }
    }

    //Delete data by id
    @OnClick(R.id.button_deleteData) protected void deleteData(){
        //get id input from editID
        String inputValue = mEditID.getText().toString();

        //Parse inputValue to int
        int key = ParserHelper.parseInt(inputValue);

        //if there was an input
        if(key != 0){
            ParseHelper.deleteObject(id.get(key));
            Toast.makeText(ManageDataActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(ManageDataActivity.this, "Invalid Input", Toast.LENGTH_LONG).show();
            Log.d(TAG, "unable to delete object based on user id input");
        }
    }

    //Delete all data in AsyncTask
    @OnClick(R.id.button_deleteAllData) protected void deleteAllData() {
        //Delete all data asynchronously
        ParseHelper.deleteAllData();
        Toast.makeText(ManageDataActivity.this, "All Data Deleted", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.button_toFixedExpenditure) protected void toManageFixedExpenditure(){
        startActivity(new Intent(ManageDataActivity.this, ManageFixedExpenditure.class));
    }

    @OnClick(R.id.button_returnToMain) protected void toMainMenu() {
        startActivity(new Intent(ManageDataActivity.this, FlexibleExpenditureActivity.class));
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
