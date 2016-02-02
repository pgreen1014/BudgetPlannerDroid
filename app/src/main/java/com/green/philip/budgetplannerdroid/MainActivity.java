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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editFlexibleAmount, editFlexibleDetails;
    TextView totalRemainingText;
    Button btnAddFlexibleData;
    Button btnToSetPreferences;
    Button btnManageData;
    Button btnTest;

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

        

        myDb = new DatabaseHelper(this);


        editFlexibleAmount = (EditText)findViewById(R.id.editText_flexibleAmount);
        editFlexibleDetails = (EditText)findViewById(R.id.editText_flexibleDetails);

        totalRemainingText = (TextView)findViewById(R.id.textView_totalRemaining);

        btnAddFlexibleData = (Button)findViewById(R.id.button_addFlexibleData);
        btnToSetPreferences = (Button)findViewById(R.id.button_toPreferences);
        btnManageData = (Button)findViewById(R.id.button_manageData);
        btnTest = (Button)findViewById(R.id.button_test);

        //get data from sharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        monthlyIncome = prefs.getInt("MONTHLY_INCOME", 0);
        fixedPercent = prefs.getInt("FIXED_PERCENT", 0);
        savingsPercent = prefs.getInt("SAVINGS_PERCENT", 0);
        flexiblePercent = prefs.getInt("FLEXIBLE_PERCENT", 0);

        //calculate monthly data
        double monthlyIncomeDoub = monthlyIncome;
        double flexiblePercentDoub = flexiblePercent;
        double percentDecimal = flexiblePercentDoub/100.0;
        totalToSpend = monthlyIncomeDoub*percentDecimal;

        if(myDb.getFlexibleData() != null){
            totalSpent = myDb.getFlexibleSpendingTotal();
            totalRemainingText.setText(String.valueOf(totalToSpend - totalSpent));
        }
        else{
            totalRemainingText.setText(String.valueOf(totalToSpend));
        }

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

    public void addData(){
        btnAddFlexibleData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertFlexibleData(editFlexibleAmount.getText().toString(), editFlexibleDetails.getText().toString());
                        if(isInserted == true){
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();

                            double totalSpent = myDb.getFlexibleSpendingTotal();
                            double total = totalToSpend - totalSpent;
                            totalRemainingText.setText(String.valueOf(totalToSpend - totalSpent));
                        }
                        else {
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

}
