package helperClasses;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by Philip on 3/12/2016.
 */
public class ParserHelper {
    private static final String TAG = "ParseHelper";

    public static double parseDouble(String text){
        //Initialize double;
        double result = 0;

        try{
            result = Double.parseDouble(text);
        } catch(NumberFormatException e){
            Log.e(TAG, "unable to parse double text", e);
        }

        return result;
    }
}
