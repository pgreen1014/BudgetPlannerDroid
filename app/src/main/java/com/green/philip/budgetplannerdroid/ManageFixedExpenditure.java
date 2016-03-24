package com.green.philip.budgetplannerdroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import helperClasses.FinanceDataHelper;
import helperClasses.ParseHelper;
import helperClasses.ParserHelper;

public class ManageFixedExpenditure extends AppCompatActivity {
    EditText editFixedAmount, editFixedDetails;
    TextView totalRemainingFixedExpenditure;
    Button btnAddData;
    Button btnReturn;
    double totalToSpend;
    double totalRemaining;
    private static final String TAG = "ManageFixedExpenditure";
    private static final String parseCategory = "Fixed_Expenditure";

    private static double monthlyIncome;
    private static double fixedPercent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fixed_expenditure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Cast EditTexts, TextViews and Buttons
        editFixedAmount = (EditText)findViewById(R.id.editText_fixedExpense);
        editFixedDetails = (EditText)findViewById(R.id.editText_fixedDetails);
        totalRemainingFixedExpenditure = (TextView)findViewById(R.id.textView_totalFixedExpenditureRemaining);
        btnAddData = (Button)findViewById(R.id.button_addFixedExpense);
        btnReturn = (Button)findViewById(R.id.button_returnToManageData);

        //get data from sharedPreferences
        getPreferences();

        //Calculate monthly fixed data
        String monthlyAmountRemaining = FinanceDataHelper.setMonthlyExpense(monthlyIncome, fixedPercent, setTotalSpent());

        totalRemainingFixedExpenditure.setText(monthlyAmountRemaining);

        addData();
        returnToManageData();
    }

    //Add fixed expenditure data
    private void addData(){
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //initialize parse object
                        ParseObject data = new ParseObject("Expenditure");
                        //parse editFixedAmount and save as a double
                        double fixedExpense = ParserHelper.parseDouble(editFixedAmount.getText().toString());

                        //if parsing was successful, fixedDouble will not equal 0 and we can add data
                        if(fixedExpense != 0){
                            ParseHelper.putExpenditure(parseCategory, fixedExpense, editFixedDetails.getText().toString());

                            //calculate new totalRemainingFixedExpenditure and show to screen
                            double totalExpenditureRemaining = FinanceDataHelper.returnTotalRemaining(totalRemainingFixedExpenditure.getText(), fixedExpense);
                            totalRemainingFixedExpenditure.setText(String.valueOf(totalExpenditureRemaining));

                            Toast.makeText(ManageFixedExpenditure.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        //else data is invalid and will not be inserted
                        else{
                            Toast.makeText(ManageFixedExpenditure.this, "Invalid Amount", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    //Takes user to manage data
    private void returnToManageData(){
        btnReturn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ManageFixedExpenditure.this, ManageData.class));
                    }
                }
        );
    }

    //returns total amount of fixed expenditure spent
    private double setTotalSpent() {

        double expenses = ParseHelper.getExpenditure(parseCategory);

        if(expenses == 0) {
            Toast.makeText(ManageFixedExpenditure.this, "Unable to Retrieve Data from Server", Toast.LENGTH_LONG).show();
            Log.d(TAG, "no data retrieved from Parse server");
            return expenses;
        }
        else{
            return expenses;
        }
    }

    //gets user finance preferences and saves to global variables
    private void getPreferences() {
        //initialized shared preferences object
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //retrieve shared preferences
        monthlyIncome = prefs.getInt("MONTHLY_INCOME", 0);
        int percent = prefs.getInt("FIXED_PERCENT", 0);
        //convert percent to decimal form
        fixedPercent = percent/100.0;
    }

}
