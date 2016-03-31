package helperClasses;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Philip on 3/12/2016.
 */
public class FinanceDataHelper {

    //calculate the total amount left to spend for the month
    public static double amountRemaining(double total, double amount) {
        return total - amount;
    }

    //returns amount remaining to spend for the month on startup
    public static String setMonthlyExpense(String income, String percent, String amountSpent){
        //Convert Parameters to BigDecimal
        BigDecimal percentage = new BigDecimal(percent);
        BigDecimal monthlyIncome = new BigDecimal(income);
        BigDecimal spent = new BigDecimal(amountSpent);

        //calculate percentage of total income to spend
        BigDecimal totalToSpend = monthlyIncome.multiply(percentage);
        //Make sure decimal places are limited to 2
        totalToSpend = totalToSpend.setScale(2);

        //calculate total remaining to spend for the month
        BigDecimal result = totalToSpend.subtract(spent);

        return result.toString();
    }

    //returns the monthly amount remaining to spend after user added data to the Parse database
    public static String returnTotalRemaining(CharSequence amountRemaining, String expense){
        //Convert Parameters into BigDecimal
        BigDecimal remaining = new BigDecimal(amountRemaining.toString());
        BigDecimal spent = new BigDecimal(expense);

        //Calculate new total remaining
        BigDecimal result = remaining.subtract(spent);

        //return result as String
        return result.toString();
    }

    //returns the total amount of expenditure left to spend
    public static String setSpendingTotal(String parseCategory, Context context, String TAG){

        double expenses = ParseHelper.getExpenditure(parseCategory);

        if(expenses == 0) {
            Toast.makeText(context, "Unable to Retrieve Data from Server", Toast.LENGTH_LONG).show();
            Log.d(TAG, "no data retrieved from Parse server");
            return Double.toString(expenses);
        }
        else{
            return Double.toString(expenses);
        }
    }


}
