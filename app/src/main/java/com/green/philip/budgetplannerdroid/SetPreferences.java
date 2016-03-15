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
import android.widget.Toast;

import java.math.BigDecimal;


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

    public void confirmSettings() {
        btnConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fixedText = editFixedPercent.getText().toString();
                        String savingsText = editSavingsPercent.getText().toString();
                        String flexibleText = editFlexiblePercent.getText().toString();

                        if(editMonthlyIncome.getText().toString() != null) {
                            try {
                                monthlyIncome = Integer.parseInt(editMonthlyIncome.getText().toString());
                            } catch (NumberFormatException e) {
                                Toast.makeText(SetPreferences.this, "Invalid Input", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Unable to parse editMonthlyIncome input", e);
                            }
                        }

                        if(fixedText != null && savingsText != null && flexibleText != null){
                            //Parse editFixedPercent and set to fixedPercent
                            try{
                                fixedPercent = Integer.parseInt(fixedText);
                            }
                            catch(NumberFormatException e){
                                Toast.makeText(SetPreferences.this, "Invalid Fixed Expenditure Percent Input", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Unable to parse fixedText input", e);
                            }
                            //Parse editSavingsPercent and set to savingsPercent
                            try{
                                savingsPercent = Integer.parseInt(savingsText);
                            }
                            catch(NumberFormatException e){
                                Toast.makeText(SetPreferences.this, "Invalid Savings Percent Input", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Unable to parse savingsText input", e);
                            }
                            //Parse flexibleText and set to flexiblePercent
                            try{
                                flexiblePercent = Integer.parseInt(flexibleText);
                            }
                            catch(NumberFormatException e){
                                Toast.makeText(SetPreferences.this, "Invalid Flexible Expenditure Percent Input", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Unable to parse flexibleText Input", e);
                            }
                        }
                        else{
                            Toast.makeText(SetPreferences.this, "Error Adding Data", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(fixedPercent + savingsPercent + flexiblePercent == 100){
                            //Toast.makeText(SetPreferences.this, "Data Set", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("MONTHLY_INCOME", monthlyIncome);
                            editor.putInt("FIXED_PERCENT", fixedPercent);
                            editor.putInt("SAVINGS_PERCENT", savingsPercent);
                            editor.putInt("FLEXIBLE_PERCENT", flexiblePercent);
                            editor.commit();

                            //return user to MainActivity
                            startActivity(new Intent(SetPreferences.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(SetPreferences.this, "Pecentages Do Not Equal 100", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
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
