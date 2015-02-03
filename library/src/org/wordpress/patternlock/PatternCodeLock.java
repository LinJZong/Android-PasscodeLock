package org.wordpress.patternlock;

import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import org.wordpress.AbstractAppLock;
import org.wordpress.passcodelock.EncryptConfig;


public class PatternCodeLock extends AbstractAppLock {

	private Application currentApp; //Keep a reference to the app that invoked the locker
	private Date lastFocusDate;
	public PatternCodeLock(Application currentApp) {
		super();
		this.currentApp = currentApp;
	}

	public void enable(){
		if (android.os.Build.VERSION.SDK_INT < 14)
			return;

		currentApp.unregisterActivityLifecycleCallbacks(this);
		currentApp.registerActivityLifecycleCallbacks(this);
	}

	public void disable( ){
		if (android.os.Build.VERSION.SDK_INT < 14)
			return;

		currentApp.unregisterActivityLifecycleCallbacks(this);
	}

	public void forcePasswordLock(){
		lastFocusDate = null;
	}
	@Override
	public boolean verifyPassword(String password) {
		String savedData = LockPatternUtils.loadFromPreferences(currentApp);
		if(savedData != null && savedData.equals(password)){
			lastFocusDate=new Date();
			return true;
		}
		return false;
	}

	@Override
	public boolean isPasswordLocked() {
		// TODO Auto-generated method stub
		String savedData = LockPatternUtils.loadFromPreferences(currentApp);
		return savedData!=null;
	}

	@Override
	public boolean setPassword(String password) {
		LockPatternUtils.saveToPreferences(currentApp, password);
		lastFocusDate=new Date();
		return true;
	}
	
	private boolean mustShowUnlockSceen() {


		if( lastFocusDate == null )
			return true; //first startup or when we forced to show the password

		int currentTimeOut = lockTimeOut; //get a reference to the current password timeout and reset it to default
		lockTimeOut = DEFAULT_TIMEOUT;
		Date now = new Date();
		long now_ms = now.getTime();
		long lost_focus_ms = lastFocusDate.getTime();
		int secondsPassed = (int) (now_ms - lost_focus_ms)/(1000);
		secondsPassed = Math.abs(secondsPassed); //Make sure changing the clock on the device to a time in the past doesn't by-pass PIN Lock
		if (secondsPassed >= currentTimeOut) {
			lastFocusDate = null;
			return true;
		}
		return false;
	}

	@Override
	public void onActivityPaused(Activity arg0) {
		if( arg0.getClass() == VerifyLockPatternActivity.class )
			
			return;

		if( ( this.appLockDisabledActivities != null ) && Arrays.asList(this.appLockDisabledActivities).contains( arg0.getClass().getName() ) )
			return;

		lastFocusDate = new Date();

	}

	@Override
	public void onActivityResumed(Activity arg0) {

		if( arg0.getClass() == VerifyLockPatternActivity.class
				||arg0.getClass()==SetLockPatternActivity.class)
			return;

		if(  ( this.appLockDisabledActivities != null ) && Arrays.asList(this.appLockDisabledActivities).contains( arg0.getClass().getName() ) )
			return;

		if(mustShowUnlockSceen()) {
			Intent i = null;
			if(isPasswordLocked())
				i=new Intent(arg0.getApplicationContext(), VerifyLockPatternActivity.class);
			else {
				i=new Intent(arg0.getApplicationContext(),SetLockPatternActivity.class);
			}
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			arg0.getApplication().startActivity(i);
			return;
		}

	}

	@Override
	public void onActivityCreated(Activity arg0, Bundle arg1) {
	}

	@Override
	public void onActivityDestroyed(Activity arg0) {
	}

	@Override
	public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
	}

	@Override
	public void onActivityStarted(Activity arg0) {
	}

	@Override
	public void onActivityStopped(Activity arg0) {
	}


}
