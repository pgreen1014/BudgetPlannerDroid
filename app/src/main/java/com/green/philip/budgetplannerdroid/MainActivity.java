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
import helperClasses.FinanceDataHelper;
import helperClasses.ParseHelper;
import helperClasses.ParserHelper;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.editText_flexibleAmount) EditText editFlexibleAmount;
    @Bind(R.id.editText_flexibleDetails) EditText editFlexibleDetails;
    @Bind(R.id.textView_totalRemaining) TextView totalRemainingText;
    @Bind(R.id.button_addFlexibleData) Button btnAddFlexibleData;
    @Bind(R.id.button_toPreferences) Button btnToSetPreferences;
    @Bind(R.id.button_manageData) Button btnManageData;
    private static final String TAG = "MainActivity";
    private static final String parseCategory = "Flexible_Expenditure";

    private static double monthlyIncome;
    private static double flexiblePercent;


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
        String monthlyAmountRemaining = FinanceDataHelper.setMonthlyExpense(monthlyIncome, flexiblePercent, setTotalSpent());
        //Show total remaining to screen
        totalRemainingText.setText(monthlyAmountRemaining);

        //Button Methods
        toSetPreferences();
        addData();
        toManageData();
    }

    //takes user to SetPreferences activity
    public void toSetPreferences(){
        btnToSetPreferences.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, SetPreferences.class);
                        startActivity(i);
                    }
                }
        );
    }

    //Adds data to the parse cloud when with the btnAddFlexibleData
    //Need to implement remaining amount into addDataCloud()
    private void addData(){
        btnAddFlexibleData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Initialize ParseObject
                        ParseObject data = new ParseObject("Expenditure");

                        //Get Flexible amount and save to string
                        String flexibleText = editFlexibleAmount.getText().toString();

                        //Parse flexibleText and save to double
                        double flexibleDouble = ParserHelper.parseDouble(flexibleText);

                        //if parsing was successful, flexibleDouble will not equal 0 and we can add data
                        if (flexibleDouble != 0) {
                            //put data into parse database
                            ParseHelper.putExpenditure(parseCategory, flexibleDouble, editFlexibleDetails.getText().toString());

                            //calculate new total remaining to spend and show to screen
                            double result = FinanceDataHelper.returnTotalRemaining(totalRemainingText.getText(), flexibleDouble);
                            totalRemainingText.setText(String.valueOf(result));

                            //notify user of successful data insertion
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        //else data is invalid and will not be inserted
                        else {
                            Toast.makeText(MainActivity.this, "Invalid Amount", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Unable to parse user input");
                        }

                    }
                }
        );
    }

    //Changes to ManageData Activity
    private void toManageData(){
        btnManageData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent i = new Intent(MainActivity.this, ManageData.class);
                        startActivity(new Intent(MainActivity.this, ManageData.class));
                    }
                }
        );
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

    //returns the total amount of flexible expenditure spent
    private double setTotalSpent() {
        //query Parse for total spent in Flexible_Expenditure category and save to expenses
        double expenses = ParseHelper.getExpenditure("Flexible_Expenditure");

        //if total expense returned is 0 inform user that no data was retrieved
        if(expenses == 0) {
            Toast.makeText(MainActivity.this, "Unable to Retrieve Data from Server", Toast.LENGTH_LONG).show();
            Log.d(TAG, "no data retrieved from Parse server");
            return expenses;
        }
        else{
          return expenses;
        }

    }

    //gets user finance preferences and saves to global variables
    private void getPreferences(){
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        monthlyIncome = prefs.getInt("MONTHLY_INCOME", 0);
        int percent = prefs.getInt("FLEXIBLE_PERCENT", 0);
        //convert Flexible Percent to a double and save to global variable
        flexiblePercent = percent/100.0;
    }


}
