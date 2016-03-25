package helperClasses;


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
    public static String setMonthlyExpense(double income, double percent, double amountSpent){
        double monthlyIncome = income;

        //calculate percentage of total income to spend
        double totalToSpend = monthlyIncome*percent;

        //calculate total remaining to spend for the month
        double totalRemaining = totalToSpend - amountSpent;

        //round to the hundredth decimal
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        String result = df.format(totalRemaining);

        return result;
    }

    //returns the monthly amount remaining to spend after user added data to the Parse database
    public static double returnTotalRemaining(CharSequence amountRemaining, double expense){
        //Convert CharSequence amountRemaining to a String and Parse to double
        String cs = amountRemaining.toString();
        double monthlyAmountRemaining = ParserHelper.parseDouble(cs);

        //calculate the new amount remaining after new expense and return the result
        return monthlyAmountRemaining - expense;
    }

}
