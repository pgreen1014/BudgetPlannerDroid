package com.green.philip.budgetplannerdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.math.BigDecimal;

import butterknife.ButterKnife;

/**
 * Created by Philip on 4/25/2016.
 */
public abstract class ExpenditureActivityTemplate extends AppCompatActivity{
    private BigDecimal mSpendingTotal;

    protected abstract int setLayout();

    protected abstract TextView totalRemainingTextView();

    protected abstract String expenseCategory();

    protected abstract BigDecimal getExpenditureCategoryPercent();

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


}
