package com.green.philip.budgetplannerdroid;

import android.content.Context;
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
    private boolean mToDelete;

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

    // Returns a user friendly version of mExpenditureType
    public String getExpenditureCategory(Context context) {
        String category;
        switch(mExpenditureType) {
            case FLEXIBLE_EXPENSE:
                category = context.getResources().getString(R.string.flexibleExpenditure);
                break;
            case FIXED_EXPENSE:
                category = context.getResources().getString(R.string.fixedExpenditure);
                break;
            default:
                category = null;
                Log.e(TAG, "No category set");
                break;
        }
        return category;
    }

    public void setExpenditureType(String expenditureType, String TAG) {
        switch(expenditureType) {
            case FLEXIBLE_EXPENSE:
                mExpenditureType = expenditureType;
                break;
            case FIXED_EXPENSE:
                mExpenditureType = expenditureType;
                break;
            default:
                Log.e(TAG, "unable to retrieve category");
                break;
        }
    }

    public boolean isToDelete() {
        return mToDelete;
    }

    public void setToDelete(boolean toDelete) {
        mToDelete = toDelete;
    }
}
