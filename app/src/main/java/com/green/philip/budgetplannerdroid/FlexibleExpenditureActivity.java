package com.green.philip.budgetplannerdroid;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helperClasses.FinanceDataHelper;
import helperClasses.ParseHelper;
import helperClasses.SharedPreferenceHelper;

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

    //Adds data to the parse cloud when with the btnAddFlexibleData
    //Need to implement remaining amount into addDataCloud()
    @OnClick(R.id.button_addFlexibleData) protected void addData(){

        //Get Flexible amount and save to string
        String flexibleText = mFlexibleAmount.getText().toString();

        //if user input data, flexible amount not equal null and we can add data
        if (flexibleText != null) {

            //put data into parse database
            ParseHelper.putExpenditure(parseCategory, flexibleText, mFlexibleDetails.getText().toString());

            //calculate new total remaining to spend and show to screen
            String result = FinanceDataHelper.returnTotalRemaining(mTotalRemaining.getText(), flexibleText);
            mTotalRemaining.setText(result);

            //notify user of successful data insertion
            Toast.makeText(FlexibleExpenditureActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        //else data is invalid and will not be inserted
        else {
            Toast.makeText(FlexibleExpenditureActivity.this, "Invalid Amount", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Unable to parse user input");
        }
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
    protected String expenseCategory() {
        return Expense.FLEXIBLE_EXPENSE;
    }

    @Override
    protected BigDecimal getExpenditureCategoryPercent() {
        ExpenseLab expenseLab = ExpenseLab.get(FlexibleExpenditureActivity.this);
        return expenseLab.getPercentFlexibleExpenditure();
    }
}

