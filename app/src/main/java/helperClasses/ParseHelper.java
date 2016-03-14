package helperClasses;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Philip on 3/12/2016.
 */
public class ParseHelper {
    private static final String TAG = "ParseHelper.java";

    //puts data into the Parse database where the Category field = Flexible_Expenditure
    public static void putFlexibleExpenditure(double amount, String details){
        //Initialize Parse Object
        ParseObject data = new ParseObject("Expenditure");

        //put data into the data ParseObject
        data.put("category", "Flexible_Expenditure");
        data.put("amount", amount);
        data.put("details", details);

        //save to Parse database in an async task
        data.saveInBackground();
    }

    //retrieves and returns Flexible_Expenditure data from Parse synchronously
    public static double getFlexibleExpenditure(){
        //Query Parse Expenditure table for all data where the category = Flexible_Expenditure
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Expenditure");
        query.whereEqualTo("category", "Flexible_Expenditure");

        //returns query as an array
        query.selectKeys(Arrays.asList("amount"));
        double total = 0;

        try{
            List<ParseObject> results = query.find();
            double amount;
            //loops through array and adds each amount to the total
            for(ParseObject result: results){
                amount = result.getDouble("amount");
                total += amount;
            }
        }catch(ParseException e){
            Log.e(TAG, "unable to query data from the parse database", e);
        }
        return total;
    }


}
