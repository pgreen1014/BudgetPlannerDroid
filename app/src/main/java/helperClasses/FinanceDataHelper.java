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

}
