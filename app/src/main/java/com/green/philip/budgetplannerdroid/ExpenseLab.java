package com.green.philip.budgetplannerdroid;

import android.content.Context;

import java.math.BigDecimal;

import helperClasses.SharedPreferenceHelper;

/**
 * Created by Philip on 4/25/2016.
 */
public class ExpenseLab {
    private static ExpenseLab sExpenseLab;
    private static BigDecimal sMonthlyIncome;
    private static BigDecimal sPercentFlexibleExpenditure;
    private static BigDecimal sPercentFixedExpenditure;
    private static BigDecimal sPercentSavings;

    public ExpenseLab get(Context context) {
        if (sExpenseLab == null) {
            sExpenseLab = new ExpenseLab(context);
        }
        return sExpenseLab;
    }

    private ExpenseLab(Context context) {
        setPercentFixedExpenditure(context);
        setPercentFlexibleExpenditure(context);
        setPercentSavings(context);
        setMonthlyIncome(context);
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
}
