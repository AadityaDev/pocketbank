package com.technawabs.pocketbank;

import android.app.Application;
import android.support.multidex.MultiDex;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class BaseApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "NmGn8vOUwuKdQt6LcPBhr3AwD";
    private static final String TWITTER_SECRET = "LNNku9rn8WUhmZoPePJwqrTExnIAAoVFWVLALS7RFTWOGdGYMV";

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        MultiDex.install(this);
    }
}
