package com.green.philip.budgetplannerdroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManageFixedExpenditure extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editFixedAmount, editFixedDetails;
    TextView totalRemainingFixedExpenditure;
    Button btnAddData;
    Button btnReturn;
    double totalToSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fixed_expenditure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        myDb = new DatabaseHelper(this);

        editFixedAmount = (EditText)findViewById(R.id.editText_fixedExpense);
        editFixedDetails = (EditText)findViewById(R.id.editText_fixedDetails);
        totalRemainingFixedExpenditure = (TextView)findViewById(R.id.textView_totalFixedExpenditureRemaining);

        btnAddData = (Button)findViewById(R.id.button_addFixedExpense);
        btnReturn = (Button)findViewById(R.id.button_returnToManageData);

        //get data from sharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        int monthlyIncome = prefs.getInt("MONTHLY_INCOME", 0);
        int fixedPercent = prefs.getInt("FIXED_PERCENT", 0);

        //Calculate monthly fixed data
        double monthlyIncomeDoub = monthlyIncome;
        double fixedPercentDoub = fixedPercent;
        double percentDecimal = fixedPercentDoub/100.0;
        totalToSpend = monthlyIncomeDoub*percentDecimal;
        double totalSpent;

        if(myDb.getFixedData() != null){
            totalSpent = myDb.getFixedSpendingTotal();
            totalRemainingFixedExpenditure.setText(String.valueOf(totalToSpend - totalSpent));
        }
        else{
            totalRemainingFixedExpenditure.setText(String.valueOf(totalToSpend));
        }

        addData();
        returnToManageData();
    }

    public void addData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertFixedData(editFixedAmount.getText().toString(), editFixedDetails.getText().toString());
                        if(isInserted == true){
                            Toast.makeText(ManageFixedExpenditure.this, "Data Inserted", Toast.LENGTH_LONG).show();

                            double totalSpent = myDb.getFixedSpendingTotal();
                            totalRemainingFixedExpenditure.setText(String.valueOf(totalToSpend - totalSpent));
                        }
                        else {
                            Toast.makeText(ManageFixedExpenditure.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void returnToManageData(){
        btnReturn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ManageFixedExpenditure.this, ManageData.class);
                        startActivity(i);
                    }
                }
        );
    }

}
