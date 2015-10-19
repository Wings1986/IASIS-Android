package com.iasishealthcare.iasis;

import android.app.Application;

/**
 * Created by admin on 9/21/15.
 */
public class UIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppData.getInstance().init(getBaseContext());

        MyLocation.getInstance().init(getBaseContext());

    }

}