package helperClasses;


import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Philip on 3/12/2016.
 */
public class FinanceDataHelper {

    public static double amountRemaining(double total, double amount) {
        return total - amount;
    }

    public static String setMonthlyExpense(int income, int percent, double amountSpent){
        double monthlyIncome = income;
        //convert percent to decimal form
        double percentDecimal = percent/100.0;

        double totalToSpend = monthlyIncome*percentDecimal;

        double totalRemaining = totalToSpend - amountSpent;

        DecimalFormat df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.DOWN);

        String result = df.format(totalRemaining);
        return result;
    }

}
