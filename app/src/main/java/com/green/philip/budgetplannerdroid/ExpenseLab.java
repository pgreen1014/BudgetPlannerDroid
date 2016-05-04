package com.green.philip.budgetplannerdroid;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import helperClasses.ParseHelper;
import helperClasses.SharedPreferenceHelper;

/**
 * Created by Philip on 4/25/2016.
 */
public class ExpenseLab {
    private static final String TAG = "ExpenseLab";

    private static ExpenseLab sExpenseLab;
    private static BigDecimal sMonthlyIncome;
    private static BigDecimal sPercentFlexibleExpenditure;
    private static BigDecimal sPercentFixedExpenditure;
    private static BigDecimal sPercentSavings;
    private List<Expense> mExpenses;

    public static ExpenseLab get(Context context) {
        if (sExpenseLab == null) {
            sExpenseLab = new ExpenseLab(context);
        }
        return sExpenseLab;
    }

    private ExpenseLab(Context context) {
        mExpenses = new ArrayList<>();
        setPercentFixedExpenditure(context);
        setPercentFlexibleExpenditure(context);
        setPercentSavings(context);
        setMonthlyIncome(context);
        setExpenses();
    }

    private void setExpenses() {
        List<ParseObject> results = ParseHelper.getAllData();
        for(ParseObject result: results){
            Expense expense = new Expense();

            String objectID = result.getObjectId();
            expense.setId(objectID);

            String amount = String.valueOf(result.getDouble("amount"));
            expense.setAmount(new BigDecimal(amount));

            String details = result.getString("details");
            expense.setDetail(details);

            String category = result.getString("category");
            expense.setExpenditureType(category, TAG);

            mExpenses.add(expense);
        }
    }

    public List<Expense> getExpenses() {
        return mExpenses;
    }

    public BigDecimal getMonthlyIncome() {
        return sMonthlyIncome;
    }

    public BigDecimal getPercentFlexibleExpenditure() {
        return sPercentFlexibleExpenditure;
    }

    public BigDecimal getPercentFixedExpenditure() {
        return sPercentFixedExpenditure;
    }

    public BigDecimal getPercentSavings() {
        return sPercentSavings;
    }

    // gets flexible expenditure percent from shared preferences and saves as BigDecimal
    public void setPercentFlexibleExpenditure(Context context) {
        int flexiblePercent = SharedPreferenceHelper.getFlexiblePercent(context);

        double decimalPercent = flexiblePercent/100.0;
        sPercentFlexibleExpenditure = new BigDecimal(Double.toString(decimalPercent));
    }

    // gets fixed expenditure percent from shared preferences and saves as BigDecimal
    public void setPercentFixedExpenditure(Context context) {
        int fixedPercent = SharedPreferenceHelper.getFixedPercent(context);

        double decimalPercent = fixedPercent/100.0;
        sPercentFixedExpenditure = new BigDecimal(Double.toString(decimalPercent));
    }

    // gets savings  percent from shared preferences and saves as BigDecimal
    public void setPercentSavings(Context context) {
        int savingsPercent = SharedPreferenceHelper.getSavingsPercent(context);

        double decimalPercent = savingsPercent/100.0;
        sPercentSavings = new BigDecimal(Double.toString(decimalPercent));
    }

    // gets monthly income from shared preferences and saves as BigDecimal
    public void setMonthlyIncome(Context context) {
        int monthlyIncome = SharedPreferenceHelper.getMonthlyIncome(context);

        sMonthlyIncome = new BigDecimal(Integer.toString(monthlyIncome));
    }

    public void updatePreferences(Context context) {
        setPercentFixedExpenditure(context);
        setPercentFlexibleExpenditure(context);
        setPercentSavings(context);
        setMonthlyIncome(context);
    }

    // Returns the total amount to spend for a particular type of expenditure
    public String getMonthlyExpense(BigDecimal income, BigDecimal percent, BigDecimal amountSpent) {
        //calculate percentage of total income to spend
        BigDecimal totalToSpend = income.multiply(percent);
        //Make sure decimal places are limited to 2
        totalToSpend = totalToSpend.setScale(2);

        //calculate total remaining to spend for the month
        BigDecimal result = totalToSpend.subtract(amountSpent);
        return result.toString();
    }

    //returns the total amount of expenditure left to spend
    public BigDecimal setSpendingTotal(String parseCategory, Context context){

        double expenses = ParseHelper.getExpenditure(parseCategory);

        if(expenses == 0) {
            Toast.makeText(context, "Unable to Retrieve Data from Server", Toast.LENGTH_SHORT).show();
            return new BigDecimal(Double.toString(expenses));
        }
        else{
            return new BigDecimal(Double.toString(expenses));
        }
    }
}
