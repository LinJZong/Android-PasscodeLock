package org.wordpress.patternlock;

import java.util.List;

import org.wordpress.AppLockManager;
import org.wordpress.passcodelock.R;
import org.wordpress.patternlock.LockPatternView.Cell;
import org.wordpress.patternlock.LockPatternView.OnPatternListener;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;



public class SetLockPatternActivity extends Activity implements OnPatternListener, OnClickListener {

	private final static String TAG = "LockPatternActivity";

	public static final int STATUS_FIRST_READY = 101;
	public static final int STATUS_FIRST_STARTED = 102;
	public static final int STATUS_TOO_SHORT = 103;
	public static final int STATUS_FIRST_OK = 105;
	public static final int STATUS_SECOND_READY = 106;
	public static final int STATUS_SECOND_STARTED = 107;
	public static final int STATUS_SECOND_NOT_MATCHED = 108;
	public static final int STATUS_SECOND_OK = 109;

	private SetLockPatternView mView = null;
	private SetLockPatternModel mModel = null;

	private int mCurrentStatus = STATUS_FIRST_READY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		mModel = new SetLockPatternModel();
		mView = new SetLockPatternView(this);
		//
		reset();
	}
	private void reset() {
		mCurrentStatus = STATUS_FIRST_READY;
		mModel.setLockPattern(null);
		mView.updateView(mCurrentStatus);
	}

	private void dealLockPattern(Context context, List<Cell> lockPattern) {
		String data = LockPatternUtils.convertToSequence(lockPattern);
		//
		if (mModel.getLockPattern() == null) {
			if (mModel.isTooShort(data) == true) {
				mCurrentStatus = STATUS_TOO_SHORT;
				Log.d(TAG, "is too short.");
				return;
			}
			mModel.setLockPattern(data);
			mCurrentStatus = STATUS_SECOND_READY;
			mView.updateView(mCurrentStatus);
			Log.d(TAG, "first pattern is ok.");
			return;
		} else {
			if (mModel.getLockPattern().equals(data)) {
				mCurrentStatus = STATUS_SECOND_OK;
				AppLockManager.getInstance().getCurrentAppLock().setPassword( mModel.getLockPattern());
				closeActivity(true);
				Toast.makeText(this, "手势设置成功", Toast.LENGTH_SHORT).show();
				return;
			} else {
				mCurrentStatus = STATUS_SECOND_NOT_MATCHED;
				Log.d(TAG, "pattern is not matched.");
				return;
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnReSet) {
			if (mCurrentStatus == STATUS_FIRST_OK||mCurrentStatus==STATUS_SECOND_NOT_MATCHED) {
				reset();
			}
		}
	}

	@Override
	public void onBackPressed() {
		AppLockManager.getInstance().getCurrentAppLock().forcePasswordLock();
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		this.startActivity(i);
	}


	public void closeActivity(boolean lockPatternSaved) {
		if(lockPatternSaved == true) {
			setResult(Activity.RESULT_OK);
		} else {
			setResult(Activity.RESULT_CANCELED);
		}
		finish();
	}

	@Override
	public void onPatternStart() {
		Log.d(TAG, "onPatternStart");
		//
		if (mCurrentStatus == STATUS_FIRST_READY||mCurrentStatus==STATUS_TOO_SHORT) {
			mCurrentStatus = STATUS_FIRST_STARTED;
		} else if (mCurrentStatus == STATUS_SECOND_READY||mCurrentStatus==STATUS_SECOND_NOT_MATCHED) {
			mCurrentStatus = STATUS_SECOND_STARTED;
		}
		//
		mView.updateView(mCurrentStatus);
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		Log.d(TAG, "onPatternDetected:" + pattern);
		//
		dealLockPattern(this, pattern);
		//
		mView.updateView(mCurrentStatus);
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		Log.d(TAG, "onPatternCellAdded:" + pattern);
	}

	@Override
	public void onPatternCleared() {
		Log.d(TAG, "onPatternCleared");
	}

}
