package com.green.philip.budgetplannerdroid;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.FinanceDataHelper;
import helperClasses.ParseHelper;
import helperClasses.ParserHelper;
import helperClasses.SharedPreferenceHelper;

public class MainActivity extends AppCompatActivity {
    //Bind Views
    @Bind(R.id.editText_flexibleAmount) EditText editFlexibleAmount;
    @Bind(R.id.editText_flexibleDetails) EditText editFlexibleDetails;
    @Bind(R.id.textView_totalRemaining) TextView totalRemainingText;
    //Declare global fields
    private static final String TAG = "MainActivity";
    private static final String parseCategory = "Flexible_Expenditure";
    private static String monthlyIncome;
    private static String flexiblePercent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //get data from sharedPreferences and save to global variables

        getPreferences();

        //calculate monthly data
        String spendingTotal = FinanceDataHelper.setSpendingTotal(parseCategory, MainActivity.this, TAG);
        String monthlyAmountRemaining = FinanceDataHelper.setMonthlyExpense(monthlyIncome, flexiblePercent, spendingTotal);
        //Show total remaining to screen
        totalRemainingText.setText(monthlyAmountRemaining);
    }

    //takes user to SetPreferences activity
    @OnClick(R.id.button_toPreferences) protected void toSetPreferences(){
        startActivity(new Intent(MainActivity.this, SetPreferences.class));
    }

    //Adds data to the parse cloud when with the btnAddFlexibleData
    //Need to implement remaining amount into addDataCloud()
    @OnClick(R.id.button_addFlexibleData) protected void addData(){

        //Get Flexible amount and save to string
        String flexibleText = editFlexibleAmount.getText().toString();

        //if user input data, flexible amount not equal null and we can add data
        if (flexibleText != null) {

            //put data into parse database
            ParseHelper.putExpenditure(parseCategory, flexibleText, editFlexibleDetails.getText().toString());

            //calculate new total remaining to spend and show to screen
            String result = FinanceDataHelper.returnTotalRemaining(totalRemainingText.getText(), flexibleText);
            totalRemainingText.setText(result);

            //notify user of successful data insertion
            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        //else data is invalid and will not be inserted
        else {
            Toast.makeText(MainActivity.this, "Invalid Amount", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Unable to parse user input");
        }
    }

    //Changes to ManageData Activity
    @OnClick(R.id.button_manageData) protected void toManageData(){
        startActivity(new Intent(MainActivity.this, ManageData.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //gets user finance preferences and saves to global variables
    private void getPreferences(){

        //get shared preference data, convert to string and save to global variable
        monthlyIncome = Integer.toString(SharedPreferenceHelper.getMonthlyIncome(MainActivity.this));
        int percent = SharedPreferenceHelper.getFlexiblePercent(MainActivity.this);


        //convert Flexible Percent to a double and convert percentage to decimal format
        double result = percent/100.0;
        flexiblePercent = Double.toString(result);
    }


}
