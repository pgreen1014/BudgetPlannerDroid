package com.green.philip.budgetplannerdroid;


import android.content.Context;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.OnClick;

public class FlexibleExpenditureActivity extends ExpenditureActivityTemplate {
    //Bind Views
    @Bind(R.id.editText_flexibleAmount) EditText mFlexibleAmount;
    @Bind(R.id.editText_flexibleDetails) EditText mFlexibleDetails;
    @Bind(R.id.textView_totalRemaining) TextView mTotalRemaining;
    //Declare global fields
    private static final String TAG = "FlexExpenditureActivity";
    private static final String parseCategory = "Flexible_Expenditure";


    //takes user to SetPreferencesActivity activity
    @OnClick(R.id.button_toPreferences) protected void toSetPreferences(){
        startActivity(new Intent(FlexibleExpenditureActivity.this, SetPreferencesActivity.class));
    }

    //Changes to ManageDataActivity Activity
    @OnClick(R.id.button_manageData) protected void toManageData() {
        startActivity(new Intent(FlexibleExpenditureActivity.this, ManageDataActivity.class));
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

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected TextView totalRemainingTextView() {
        return mTotalRemaining;
    }

    @Override
    protected EditText amountExpense() {
        return mFlexibleAmount;
    }

    @Override
    protected EditText detailsExpense() {
        return mFlexibleDetails;
    }

    @Override
    protected String expenseCategory() {
        return Expense.FLEXIBLE_EXPENSE;
    }

    @Override
    protected String parseCategory() {
        return parseCategory;
    }

    @Override
    protected BigDecimal getExpenditureCategoryPercent() {
        ExpenseLab expenseLab = ExpenseLab.get(FlexibleExpenditureActivity.this);
        return expenseLab.getPercentFlexibleExpenditure();
    }

    @Override
    protected Context activityContext() {
        return FlexibleExpenditureActivity.this;
    }

    @Override
    protected String TAG() {
        return TAG;
    }
}

