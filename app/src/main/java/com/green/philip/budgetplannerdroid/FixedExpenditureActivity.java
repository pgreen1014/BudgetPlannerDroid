package com.green.philip.budgetplannerdroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.FinanceDataHelper;
import helperClasses.ParseHelper;
import helperClasses.ParserHelper;
import helperClasses.SharedPreferenceHelper;

public class FixedExpenditureActivity extends ExpenditureActivityTemplate {
    @Bind(R.id.editText_fixedExpense) EditText mFixedAmount;
    @Bind(R.id.editText_fixedDetails) EditText mFixedDetails;
    @Bind(R.id.textView_totalFixedExpenditureRemaining) TextView mTotalRemainingExpenditure;

    private static final String parseCategory = "Fixed_Expenditure";
    private static final String TAG = "FixedExpenditureActivity";

    //Takes user to manage data
    @OnClick(R.id.button_returnToManageData) protected void returnToManageData() {
        startActivity(new Intent(FixedExpenditureActivity.this, ManageDataActivity.class));
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_manage_fixed_expenditure;
    }

    @Override
    protected TextView totalRemainingTextView() {
        return mTotalRemainingExpenditure;
    }

    @Override
    protected EditText amountExpense() {
        return mFixedAmount;
    }

    @Override
    protected EditText detailsExpense() {
        return mFixedDetails;
    }

    @Override
    protected String expenseCategory() {
        return Expense.FIXED_EXPENSE;
    }

    @Override
    protected String parseCategory() {
        return parseCategory;
    }

    @Override
    protected BigDecimal getExpenditureCategoryPercent() {
        ExpenseLab expenseLab = ExpenseLab.get(FixedExpenditureActivity.this);
        return expenseLab.getPercentFixedExpenditure();
    }

    @Override
    protected Context activityContext() {
        return FixedExpenditureActivity.this;
    }

    @Override
    protected String TAG() {
        return TAG;
    }
}
