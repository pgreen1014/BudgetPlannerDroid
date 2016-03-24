package com.green.philip.budgetplannerdroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import helperClasses.ParserHelper;


public class SetPreferences extends AppCompatActivity {
    EditText editMonthlyIncome, editFixedPercent, editSavingsPercent, editFlexiblePercent;
    private final static String TAG = "SetPreferences";

    Button btnConfirm;

    int monthlyIncome, fixedPercent, savingsPercent, flexiblePercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Cast EditText and Buttons
        editMonthlyIncome = (EditText)findViewById(R.id.editText_setMonthlyIncome);
        editFixedPercent = (EditText)findViewById(R.id.editText_percentFixed);
        editSavingsPercent = (EditText)findViewById(R.id.editText_percentSavings);
        editFlexiblePercent = (EditText)findViewById(R.id.editText_percentFlexible);
        btnConfirm = (Button)findViewById(R.id.button_confirmSettings);

        confirmSettings();
    }

    private void confirmSettings() {
        btnConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Save user inputs to Strings
                        String income = editMonthlyIncome.getText().toString();
                        String fixedText = editFixedPercent.getText().toString();
                        String savingsText = editSavingsPercent.getText().toString();
                        String flexibleText = editFlexiblePercent.getText().toString();

                        //Parse monthlyIncome to global int
                        monthlyIncome = ParserHelper.parseInt(income);

                        //Parse editFixedPercent and set to fixedPercent
                        fixedPercent = ParserHelper.parseInt(fixedText);

                        //Parse editSavingsPercent and set to savingsPercent
                        savingsPercent = ParserHelper.parseInt(savingsText);

                        //Parse flexibleText and set to flexiblePercent
                        flexiblePercent = ParserHelper.parseInt(flexibleText);

                        //If percent values were valid by equaling 100, put data into shared preferences and return to MainActivity
                        if (fixedPercent + savingsPercent + flexiblePercent == 100) {
                            //save input to SharedPreferences
                            setSharedPreferences(monthlyIncome, fixedPercent, savingsPercent, flexiblePercent);

                            //return user to MainActivity
                            startActivity(new Intent(SetPreferences.this, MainActivity.class));
                        } else {
                            Toast.makeText(SetPreferences.this, "Percentages Do Not Equal 100", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Invalid user input");
                        }

                    }
                }
        );
    }

    //Method saves user input to SharedPreferences
    private void setSharedPreferences(int monthlyIncome, int fixedPercent, int savingsPercent, int flexiblePercent) {
        //Initialize SharedPreferences object and and editor
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //save user input to editor
        editor.putInt("MONTHLY_INCOME", monthlyIncome);
        editor.putInt("FIXED_PERCENT", fixedPercent);
        editor.putInt("SAVINGS_PERCENT", savingsPercent);
        editor.putInt("FLEXIBLE_PERCENT", flexiblePercent);

        //commit preferences to file
        editor.commit();
    }

    //shows current settings saved to SharedPreferences
    /*public void showSettings(){
        btnShowSettings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        int income = sharedPreferences.getInt("MONTHLY_INCOME", 0);
                        int fixPercent = sharedPreferences.getInt("FIXED_PERCENT", 0);
                        int savPercent = sharedPreferences.getInt("SAVINGS_PERCENT", 0);
                        int flexPercent = sharedPreferences.getInt("FLEXIBLE_PERCENT", 0);

                        Toast.makeText(SetPreferences.this, "Income: " + income + "\nFixed Percent: " + fixPercent + "\nSavings Percent: " + savPercent + "\nFlexible Percent: " + flexPercent, Toast.LENGTH_LONG).show();

                    }
                }
        );
    }*/

}
