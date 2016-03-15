package helperClasses;

import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
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

    //retrieves and returns total spent in a certain expenditure category from Parse synchronously
    //Categories: 'Flexible_Expenditure' or 'Fixed_Expenditure'
    public static double getExpenditure(String category) {
        //Query Parse Expenditure table for all data in the given category input
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Expenditure");
        query.whereEqualTo("category", category);

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

    //Returns a list of all ParseObjects, runs synchronously
    public static List<ParseObject> getAllData(){
        ParseQuery<ParseObject> flexibleQuery = ParseQuery.getQuery("Expenditure");

        //Initialize ArrayList object of ParseObjects
        List<ParseObject> results = new ArrayList<>();

        //Try to query Parse and save data to List results
        try {
            results = flexibleQuery.find();
        } catch (ParseException e){
            Log.e(TAG, "Unable to query data from the parse database", e);
        }

        return results;
    }

    //Delete a single ParseObject from Parse in an asynchronous task via object's key value
    public static void deleteObject(String key){
        //Initialize ParseQuery object
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Expenditure");

        //retrieve object in asynchronous task
        query.getInBackground(key, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //delete object in background
                    object.deleteInBackground();
                } else {
                    Log.e(TAG, "Unable to retrieve data from Parse for deletion", e);
                }
            }
        });

    }

    //deletes all data from Parse database asynchronously
    public static void deleteAllData(){
        //Initialize ParseQuery object
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Expenditure");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                //if no error in retrieve objects
                if(e == null){
                    for(ParseObject object: objects){
                        //delete object
                        object.deleteInBackground();
                    }
                }
                else{
                    Log.e(TAG, "Unable to retrieve data from Parse for deletion", e);
                }
            }
        });

    }




























}
