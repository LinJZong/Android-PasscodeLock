package com.example.lock;

import org.wordpress.passcodelock.PasscodePreferencesActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i("b", "onPause");
		super.onPause();
	}
	@Override
	protected void onStop() {
		Log.i("b", "onStop");
		super.onStop();
	}
	@Override
	protected void onResume() {
		Log.i("b", "onResume");
		super.onResume();
	}
	@Override
	protected void onRestart() {
		Log.i("b", "onRestart");
		super.onRestart();
	};
}