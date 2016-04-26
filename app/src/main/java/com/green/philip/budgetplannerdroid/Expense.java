package com.green.philip.budgetplannerdroid;

import android.util.Log;

import java.math.BigDecimal;

/**
 * Created by Philip on 4/25/2016.
 */
public class Expense {

    private String mId;
    private BigDecimal mAmount;
    private String mDetail;
    private String mExpenditureType;

    public static final String FLEXIBLE_EXPENSE = "Flexible_Expenditure";
    public static final String FIXED_EXPENSE = "Fixed_Expenditure";
    private static final String TAG = "Expense.java";

    public Expense() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public BigDecimal getAmount() {
        return mAmount;
    }

    public void setAmount(BigDecimal amount) {
        mAmount = amount;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public String getExpenditureType() {
        return mExpenditureType;
    }

    public void setExpenditureType(String expenditureType, String TAG) {
        if (expenditureType == FIXED_EXPENSE || expenditureType == FLEXIBLE_EXPENSE) {
            mExpenditureType = expenditureType;
        } else {
            Log.d("must use defined string", TAG);
        }
    }
}
