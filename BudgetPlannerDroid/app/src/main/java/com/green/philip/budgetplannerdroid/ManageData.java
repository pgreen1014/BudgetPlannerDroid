package com.green.philip.budgetplannerdroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ManageData extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editID;
    Button btnViewData;
    Button btnDeleteData;
    Button btnReturnToMain;
    Button btnFixedExpenditure;
    Button btnDeleteAllData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        myDb = new DatabaseHelper(this);

        editID = (EditText) findViewById(R.id.editText_ID);
        btnViewData = (Button) findViewById(R.id.button_viewData);
        btnDeleteData = (Button) findViewById(R.id.button_deleteData);
        btnFixedExpenditure = (Button) findViewById(R.id.button_toFixedExpenditure);
        btnDeleteAllData = (Button) findViewById(R.id.button_deleteAllData);
        btnReturnToMain = (Button) findViewById(R.id.button_returnToMain);

        viewAllData();
        toManageFixedExpenditure();
        deleteData();
        deleteAllData();
        toMainMenu();
    }

    public void viewAllData(){
        btnViewData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor result = myDb.getAllData();

                        if(result.getCount() == 0 ){
                            showMessage("Error", "No Data Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(result.moveToNext()) {
                            buffer.append("ID: " + result.getString(0) + "\n");
                            buffer.append("Category: " + result.getString(1)+ "\n");
                            buffer.append("Amount: " +result.getString(2)+ "\n");
                            buffer.append("Details: " + result.getString(3)+ "\n" + "\n");
                        }

                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void deleteData(){
        btnDeleteData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteRow(editID.getText().toString());
                        if(deletedRows > 0){
                            Toast.makeText(ManageData.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ManageData.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }

    public void deleteAllData(){
        btnDeleteAllData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int deletedRows = myDb.deleteAllRows();
                        if(deletedRows == 1){
                            Toast.makeText(ManageData.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ManageData.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

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

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
