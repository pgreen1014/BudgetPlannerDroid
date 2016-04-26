package com.green.philip.budgetplannerdroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.FinanceDataHelper;
import helperClasses.ParseHelper;
import helperClasses.ParserHelper;
import helperClasses.SharedPreferenceHelper;

public class FixedExpenditureActivity extends AppCompatActivity {
    @Bind(R.id.editText_fixedExpense) EditText mFixedAmount;
    @Bind(R.id.editText_fixedDetails) EditText mFixedDetails;
    @Bind(R.id.textView_totalFixedExpenditureRemaining) TextView mTotalRemainingExpenditure;
    private static final String TAG = "FixedExpenditureActivity";
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
        String totalToSpend = FinanceDataHelper.setSpendingTotal(parseCategory, FixedExpenditureActivity.this, TAG);
        String monthlyAmountRemaining = FinanceDataHelper.setMonthlyExpense(monthlyIncome, fixedPercent, totalToSpend);

        mTotalRemainingExpenditure.setText(monthlyAmountRemaining);
    }

    //Add fixed expenditure data
    @OnClick(R.id.button_addFixedExpense) protected void addData() {
        //Get fixed amount and save to string
        String fixedText = mFixedAmount.getText().toString();
        //parse editFixedAmount and save as a double
        double fixedExpense = ParserHelper.parseDouble(mFixedAmount.getText().toString());

        //if parsing was successful, fixedDouble will not equal 0 and we can add data
        if(fixedText != null){
            ParseHelper.putExpenditure(parseCategory, fixedText, mFixedDetails.getText().toString());

            //calculate new totalRemainingFixedExpenditure and show to screen
            String totalExpenditureRemaining = FinanceDataHelper.returnTotalRemaining(mTotalRemainingExpenditure.getText(), fixedText);
            mTotalRemainingExpenditure.setText(String.valueOf(totalExpenditureRemaining));

            Toast.makeText(FixedExpenditureActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        //else data is invalid and will not be inserted
        else{
            Toast.makeText(FixedExpenditureActivity.this, "Invalid Amount", Toast.LENGTH_LONG).show();
        }
    }

    //Takes user to manage data
    @OnClick(R.id.button_returnToManageData) protected void returnToManageData() {
        startActivity(new Intent(FixedExpenditureActivity.this, ManageDataActivity.class));
    }

    //gets user finance preferences and saves to global variables
    private void getPreferences() {
        //initialized shared preferences object
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        //retrieve shared preferences, convert to string, and save to global variable
        int income = SharedPreferenceHelper.getMonthlyIncome(FixedExpenditureActivity.this);
        monthlyIncome = Integer.toString(income);
        int percent = SharedPreferenceHelper.getFixedPercent(FixedExpenditureActivity.this);

        //convert percent to decimal form
        double result = percent/100.0;
        fixedPercent = Double.toString(result);
    }

}
