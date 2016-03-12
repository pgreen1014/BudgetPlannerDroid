package helperClasses;

import com.parse.ParseObject;

/**
 * Created by Philip on 3/12/2016.
 */
public class ParseHelper {

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
}
