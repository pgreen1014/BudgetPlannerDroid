package com.green.philip.budgetplannerdroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.ParseHelper;

/**
 * Created by Philip on 4/25/2016.
 */
public abstract class ExpenditureActivityTemplate extends AppCompatActivity{

    private BigDecimal mSpendingTotal;

    protected abstract int setLayout();

    protected abstract TextView totalRemainingTextView();
    protected abstract EditText amountExpense();
    protected abstract EditText detailsExpense();

    protected abstract String expenseCategory();
    protected abstract String parseCategory();

    protected abstract BigDecimal getExpenditureCategoryPercent();

    protected abstract Context activityContext();

    protected abstract String TAG();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        ExpenseLab expenseLab = ExpenseLab.get(getApplicationContext());
        expenseLab.updatePreferences(getApplicationContext());

        mSpendingTotal = expenseLab.setSpendingTotal(expenseCategory(), getApplicationContext());
        String monthlyAmountRemaining = expenseLab.getMonthlyExpense(expenseLab.getMonthlyIncome(), getExpenditureCategoryPercent(), mSpendingTotal);

        //Show total remaining to screen
        totalRemainingTextView().setText(monthlyAmountRemaining);
    }

    @OnClick(R.id.button_addData) protected void addData() {

        // Get expense amount and save to string
        String amountText = amountExpense().getText().toString();

        // if not empty then we can use data else inform user of error
        if (amountText != null) {

            // put data into parse database
            ParseHelper.putExpenditure(parseCategory(), amountText, detailsExpense().getText().toString());

            // calculate new total remaining to spend and show to screen
            String result = totalRemainingToSpend(totalRemainingTextView().getText(), amountText);
            totalRemainingTextView().setText(result);

            // notify user of successful data insertion
            Toast.makeText(activityContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(activityContext(), "Invalid Amount", Toast.LENGTH_SHORT).show();
            Log.d(TAG(), "Unable to parse user input");
        }

    }

    private String totalRemainingToSpend(CharSequence amountRemaining, String expense){
        //Convert Parameters into BigDecimal
        BigDecimal remaining = new BigDecimal(amountRemaining.toString());
        BigDecimal spent = new BigDecimal(expense);

        //Calculate new total remaining
        BigDecimal result = remaining.subtract(spent);

        //return result as String
        return result.toString();
    }


}
