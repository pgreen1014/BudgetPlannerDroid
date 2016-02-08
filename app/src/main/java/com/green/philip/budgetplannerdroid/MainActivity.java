package com.green.philip.budgetplannerdroid;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editFlexibleAmount, editFlexibleDetails;
    TextView totalRemainingText;
    Button btnAddFlexibleData;
    Button btnToSetPreferences;
    Button btnManageData;

    int monthlyIncome;
    int fixedPercent;
    int savingsPercent;
    int flexiblePercent;

    double totalToSpend;
    double totalSpent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        editFlexibleAmount = (EditText)findViewById(R.id.editText_flexibleAmount);
        editFlexibleDetails = (EditText)findViewById(R.id.editText_flexibleDetails);

        totalRemainingText = (TextView)findViewById(R.id.textView_totalRemaining);

        btnAddFlexibleData = (Button)findViewById(R.id.button_addFlexibleData);
        btnToSetPreferences = (Button)findViewById(R.id.button_toPreferences);
        btnManageData = (Button)findViewById(R.id.button_manageData);

        //get data from sharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        monthlyIncome = prefs.getInt("MONTHLY_INCOME", 0);
        fixedPercent = prefs.getInt("FIXED_PERCENT", 0);
        savingsPercent = prefs.getInt("SAVINGS_PERCENT", 0);
        flexiblePercent = prefs.getInt("FLEXIBLE_PERCENT", 0);

        totalSpent = setSpendingTotal();

        //calculate monthly data
        double monthlyIncomeDoub = monthlyIncome;
        double flexiblePercentDoub = flexiblePercent;
        double percentDecimal = flexiblePercentDoub/100.0;
        totalToSpend = monthlyIncomeDoub*percentDecimal;

        totalRemainingText.setText(String.valueOf(totalToSpend - totalSpent));

        toSetPreferences();
        addData();
        toManageData();
    }

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
    public void addData(){
        btnAddFlexibleData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Initialize ParseObject
                        ParseObject data = new ParseObject("FlexibleExpenditure");
                        //Get Flexible amount and save to string
                        String flexibleText = editFlexibleAmount.getText().toString();
                        double flexibleDouble = 0;
                        //Convert flexible amount in string format to double format
                        try{
                            flexibleDouble = Double.parseDouble(flexibleText);
                        }catch(NumberFormatException e){
                            Toast.makeText(MainActivity.this, "Invalid Dollar Amount", Toast.LENGTH_LONG).show();
                        }
                        //if parsing was successful, flexibleDouble will not equal 0 and we can add data
                        if(flexibleDouble!=0){
                            data.put("amount", flexibleDouble);
                            data.put("details", editFlexibleDetails.getText().toString());
                            data.saveInBackground();
                            //calculate total remaining to spend
                            String text = String.valueOf(totalRemainingText.getText());
                            double total = 0;
                            try{
                                total = Double.parseDouble(text);
                            }catch(NumberFormatException e){
                                e.printStackTrace();
                            }
                            double result = total - flexibleDouble;
                            totalRemainingText.setText(String.valueOf(result));

                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        //else data is invalid and will not be inserted
                        else{
                            Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }

    public void toManageData(){
        btnManageData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, ManageData.class);
                        startActivity(i);
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

    public double setSpendingTotal() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FlexibleExpenditure");
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
