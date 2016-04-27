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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.FinanceDataHelper;
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

        //Get Flexible amount and save to string
        String flexibleText = amountExpense().getText().toString();

        //if user input data, flexible amount not equal null and we can add data
        if (flexibleText != null) {

            //put data into parse database
            ParseHelper.putExpenditure(parseCategory(), flexibleText, detailsExpense().getText().toString());

            //calculate new total remaining to spend and show to screen
            String result = FinanceDataHelper.returnTotalRemaining(totalRemainingTextView().getText(), flexibleText);
            totalRemainingTextView().setText(result);

            //notify user of successful data insertion
            Toast.makeText(activityContext(), "Data Inserted", Toast.LENGTH_LONG).show();
        }
        //else data is invalid and will not be inserted
        else {
            Toast.makeText(activityContext(), "Invalid Amount", Toast.LENGTH_LONG).show();
            Log.d(TAG(), "Unable to parse user input");
        }

    }


}
