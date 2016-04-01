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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.FinanceDataHelper;
import helperClasses.ParseHelper;
import helperClasses.ParserHelper;
import helperClasses.SharedPreferenceHelper;

public class ManageFixedExpenditure extends AppCompatActivity {
    @Bind(R.id.editText_fixedExpense) EditText editFixedAmount;
    @Bind(R.id.editText_fixedDetails) EditText editFixedDetails;
    @Bind(R.id.textView_totalFixedExpenditureRemaining) TextView totalRemainingFixedExpenditure;
    private static final String TAG = "ManageFixedExpenditure";
    private static final String parseCategory = "Fixed_Expenditure";

    private static String monthlyIncome;
    private static String fixedPercent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fixed_expenditure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //get data from sharedPreferences
        getPreferences();

        //Calculate monthly fixed data
        String totalToSpend = FinanceDataHelper.setSpendingTotal(parseCategory, ManageFixedExpenditure.this, TAG);
        String monthlyAmountRemaining = FinanceDataHelper.setMonthlyExpense(monthlyIncome, fixedPercent, totalToSpend);

        totalRemainingFixedExpenditure.setText(monthlyAmountRemaining);
    }

    //Add fixed expenditure data
    @OnClick(R.id.button_addFixedExpense) protected void addData() {
        //Get fixed amount and save to string
        String fixedText = editFixedAmount.getText().toString();
        //parse editFixedAmount and save as a double
        double fixedExpense = ParserHelper.parseDouble(editFixedAmount.getText().toString());

        //if parsing was successful, fixedDouble will not equal 0 and we can add data
        if(fixedText != null){
            ParseHelper.putExpenditure(parseCategory, fixedText, editFixedDetails.getText().toString());

            //calculate new totalRemainingFixedExpenditure and show to screen
            String totalExpenditureRemaining = FinanceDataHelper.returnTotalRemaining(totalRemainingFixedExpenditure.getText(), fixedText);
            totalRemainingFixedExpenditure.setText(String.valueOf(totalExpenditureRemaining));

            Toast.makeText(ManageFixedExpenditure.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        //else data is invalid and will not be inserted
        else{
            Toast.makeText(ManageFixedExpenditure.this, "Invalid Amount", Toast.LENGTH_LONG).show();
        }
    }

    //Takes user to manage data
    @OnClick(R.id.button_returnToManageData) protected void returnToManageData() {
        startActivity(new Intent(ManageFixedExpenditure.this, ManageData.class));
    }

    //gets user finance preferences and saves to global variables
    private void getPreferences() {
        //initialized shared preferences object
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        //retrieve shared preferences, convert to string, and save to global variable
        int income = SharedPreferenceHelper.getMonthlyIncome(ManageFixedExpenditure.this);
        monthlyIncome = Integer.toString(income);
        int percent = SharedPreferenceHelper.getFixedPercent(ManageFixedExpenditure.this);

        //convert percent to decimal form
        double result = percent/100.0;
        fixedPercent = Double.toString(result);
    }

}
