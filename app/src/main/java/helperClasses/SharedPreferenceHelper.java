package helperClasses;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Philip on 3/31/2016.
 */
public class SharedPreferenceHelper {
    //Title of the SharedPreference file
    public static final String TITLE = "MyData";
    //Field names for the data held in the file
    public static final String MONTHLY_INCOME = "MONTHLY_INCOME";
    public static final String FIXED_PERCENT = "FIXED_PERCENT";
    public static final String SAVINGS_PERCENT = "SAVINGS_PERCENT";
    public static final String FLEXIBLE_PERCENT = "FLEXIBLE_PERCENT";

    public static int getMonthlyIncome(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TITLE, Context.MODE_PRIVATE);

        int income = prefs.getInt(MONTHLY_INCOME, 0);
        return income;
    }

    public static int getFixedPercent(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TITLE, Context.MODE_PRIVATE);

        int percent = prefs.getInt(FIXED_PERCENT, 0);
        return percent;
    }

    public static int getSavingsPercent(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TITLE, Context.MODE_PRIVATE);

        int percent = prefs.getInt(SAVINGS_PERCENT, 0);
        return percent;
    }

    public static int getFlexiblePercent(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TITLE, Context.MODE_PRIVATE);

        int percent = prefs.getInt(FLEXIBLE_PERCENT, 0);
        return percent;
    }
}
