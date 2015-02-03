package org.wordpress.patternlock;

import java.util.List;

import org.wordpress.AppLockManager;
import org.wordpress.passcodelock.R;
import org.wordpress.patternlock.LockPatternView.Cell;
import org.wordpress.patternlock.LockPatternView.OnPatternListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
public class VerifyLockPatternActivity extends Activity implements OnPatternListener {

	private final static String TAG = "VerifyLockPatternActivity";

	public static final int VERIFY_STATUS_READY = 1001;
	public static final int VERIFY_STATUS_ERROR = 1002;

	private VerifyLockPatternView mView = null;

	private int mCurrentStatus = VERIFY_STATUS_READY;

	private int mLeftTimes = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加到activity堆栈中
		mView = new VerifyLockPatternView(this);
		Button button=(Button)findViewById(R.id.btnRelogin);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(VerifyLockPatternActivity.this);
				builder.setTitle(null)
				.setMessage("忘记手势密码，需重新登录")
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				})
				.setNegativeButton("重新登录", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
						reLogin();
					}


				});
				builder.create().show();

			}
		});
		reset();
	}
	private void reLogin() {
		// TODO Auto-generated method stub

	}
	private void reset() {
		mLeftTimes = 5;
		mCurrentStatus = VERIFY_STATUS_READY;
		mView.updateView(mCurrentStatus, mLeftTimes);
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		Log.d(TAG, "onPatternDetected:" + pattern);
		String passLock = LockPatternUtils.convertToSequence(pattern);
		if(AppLockManager.getInstance().getCurrentAppLock().verifyPassword(passLock)) {
			//解锁成功
			closeActivity(true);
		} else {//解锁失败
			mCurrentStatus = VERIFY_STATUS_ERROR;
			mLeftTimes--;
			if(mLeftTimes <= 0) {
				Toast.makeText(this, "多次解锁失败,请重新登录", Toast.LENGTH_SHORT).show();
				reLogin();
			} else {
				mView.updateView(mCurrentStatus, mLeftTimes);
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

	public void closeActivity(boolean verified) {
		if(verified == true) {
			setResult(Activity.RESULT_OK);
		} else {
			setResult(Activity.RESULT_CANCELED);
		}
		finish();
	}

	@Override
	public void onPatternStart() {
		Log.d(TAG, "onPatternStart");
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
