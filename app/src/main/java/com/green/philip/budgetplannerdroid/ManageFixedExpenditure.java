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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

public class ManageFixedExpenditure extends AppCompatActivity {
    EditText editFixedAmount, editFixedDetails;
    TextView totalRemainingFixedExpenditure;
    Button btnAddData;
    Button btnReturn;
    double totalToSpend;
    double totalRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fixed_expenditure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        double totalSpent = setSpendingTotal();
        totalRemaining = totalToSpend - totalSpent;
        totalRemainingFixedExpenditure.setText(String.valueOf(totalRemaining));

        addData();
        returnToManageData();
    }

    public void addData(){
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //initialize parse object
                        ParseObject data = new ParseObject("Expenditure");
                        //get fixed amount and save to double
                        String fixedText = editFixedAmount.getText().toString();
                        double fixedDouble = 0;
                        try {
                            fixedDouble = Double.parseDouble(fixedText);
                        } catch(NumberFormatException e){
                            Toast.makeText(ManageFixedExpenditure.this, "Invalid Dollar Amount", Toast.LENGTH_LONG).show();
                        }
                        //if parsing was successful, fixedDouble will not equal 0 and we can add data
                        if(fixedDouble != 0){
                            totalRemainingFixedExpenditure.setText(String.valueOf(totalRemaining - fixedDouble));
                            data.put("category", "Fixed_Expenditure");
                            data.put("amount", fixedDouble);
                            data.put("details", editFixedDetails.getText().toString());
                            data.saveInBackground();
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

    public double setSpendingTotal() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Expenditure");
        query.whereEqualTo("category", "Fixed_Expenditure");
        query.selectKeys(Arrays.asList("amount"));
        double total = 0;
        try {
            List<ParseObject> results = query.find();
            double amount;
            for(ParseObject result: results){
                amount = result.getDouble("amount");
                total += amount;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return total;
    }

}
