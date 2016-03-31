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


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.ParserHelper;


public class SetPreferences extends AppCompatActivity {
    @Bind(R.id.editText_setMonthlyIncome) EditText editMonthlyIncome;
    @Bind(R.id.editText_percentFixed) EditText editFixedPercent;
    @Bind(R.id.editText_percentSavings) EditText editSavingsPercent;
    @Bind(R.id.editText_percentFlexible) EditText editFlexiblePercent;
    private final static String TAG = "SetPreferences";

    Button btnConfirm;

    int monthlyIncome, fixedPercent, savingsPercent, flexiblePercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_confirmSettings) protected void confirmSettings() {
        //Save user inputs to Strings
        String income = editMonthlyIncome.getText().toString();
        String fixed = editFixedPercent.getText().toString();
        String savings = editSavingsPercent.getText().toString();
        String flexible = editFlexiblePercent.getText().toString();

        //save input to SharedPreferences
        setSharedPreferences(income, fixed, savings, flexible);

        //return user to MainActivity
        startActivity(new Intent(SetPreferences.this, MainActivity.class));

    }

    //Method saves user input to SharedPreferences
    private void setSharedPreferences(String monthlyIncome, String fixedPercent, String savingsPercent, String flexiblePercent) {
        //Initialize SharedPreferences object and and editor
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //convert user input to int and save to editor
        editor.putInt("MONTHLY_INCOME", ParserHelper.parseInt(monthlyIncome));
        editor.putInt("FIXED_PERCENT", ParserHelper.parseInt(fixedPercent));
        editor.putInt("SAVINGS_PERCENT", ParserHelper.parseInt(savingsPercent));
        editor.putInt("FLEXIBLE_PERCENT", ParserHelper.parseInt(flexiblePercent));

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
