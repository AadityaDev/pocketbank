package com.technawabs.pocketbank;

import android.app.Application;
import android.support.multidex.MultiDex;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
