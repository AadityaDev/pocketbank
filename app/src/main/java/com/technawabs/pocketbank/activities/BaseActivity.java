package com.technawabs.pocketbank.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.Environment;
import com.citrus.sdk.response.CitrusError;
import com.crashlytics.android.Crashlytics;
import com.technawabs.pocketbank.Factory;

import io.fabric.sdk.android.Fabric;

public abstract class BaseActivity extends FragmentActivity{

    protected final String TAG = this.getClass().getSimpleName();
    protected Context context;
    private CitrusClient citrusClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        Factory.setUpThreadPolicy();
        Fabric.with(context, new Crashlytics());

        //Setup citrus client
        citrusClient = CitrusClient.getInstance(getApplicationContext());
        citrusClient.enableLog(true);
        citrusClient.init("rlvmxr9q74-signup", "c3545d28806d28b015768830885fe105",
                "rlvmxr9q74-signin",
                "736b3b82775db4b9b557e966ecb4a8a0",
                "rlvmxr9q74", Environment.SANDBOX);

        citrusClient.isUserSignedIn(new com.citrus.sdk.Callback<Boolean>() {
            @Override
            public void success(Boolean loggedIn) {
                Log.d(TAG, "Success " + loggedIn);
            }

            @Override
            public void error(CitrusError error) {
                Log.d(TAG, "Error" + error.toString());
            }
        });
    }

    public String getTAG() {
        return TAG;
    }

    public CitrusClient getCitrusClient(){
        return citrusClient;
    }
}
