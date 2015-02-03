package org.wordpress;

import java.util.HashSet;
import java.util.Set;


import android.app.Application;

public abstract class AbstractAppLock implements Application.ActivityLifecycleCallbacks
{
    public static final int DEFAULT_TIMEOUT = 2; //2 seconds
    public static final int EXTENDED_TIMEOUT = 60; //60 seconds
    
    protected int lockTimeOut = DEFAULT_TIMEOUT;
    //修改为使用set
    protected Set<String> appLockDisabledActivities = new HashSet<String>();
    
    /*
     * There are situations where an activity will start a different application with an intent.  
     * In these situations call this method right before leaving the app.
     */
    public void setOneTimeTimeout(int timeout) {
        this.lockTimeOut = timeout;
    }

    /*
     * There are situations where we don't want call the AppLock on activities (sharing items to out app for example).  
     */
    public void setDisabledActivities( String disabledActs ) {
    	appLockDisabledActivities.add(disabledActs);
    }
    
    public abstract void enable();
    public abstract void disable();
    public abstract void forcePasswordLock();
    public abstract boolean verifyPassword( String password );
    public abstract boolean isPasswordLocked();
    public abstract boolean setPassword(String password);
}
