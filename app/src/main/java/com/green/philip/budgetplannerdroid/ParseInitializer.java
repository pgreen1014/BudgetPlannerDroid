package com.green.philip.budgetplannerdroid;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Created by Philip on 1/22/2016.
 */
public class ParseInitializer extends android.app.Application {
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "q1mHERrhzpSNhLad36q4y8A7ni32qkHWwZnniZxs", "2oL9Q7knnxcrx3dwUPSeejJTtWIZELcaCQv5L2ID");
    }
}
