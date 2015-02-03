package com.example.lock;


import org.wordpress.AppLockManager;
import org.wordpress.patternlock.PatternAppLock;

import android.app.Application;

public class MyApplication extends Application{
     public void onCreate() {
 		AppLockManager.getInstance().enableDefaultAppLockIfAvailable(this);
 		AppLockManager.getInstance().setCurrentAppLock(new PatternAppLock((Application) getApplicationContext()));
 		super.onCreate();
     };
}
