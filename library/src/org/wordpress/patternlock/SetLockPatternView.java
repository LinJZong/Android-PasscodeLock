package org.wordpress.patternlock;


import org.wordpress.passcodelock.R;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SetLockPatternView {
	private SetLockPatternActivity mActivity = null;

	private LockPatternView mLockPatternView = null;
	private TextView mTipTextView = null;
	private Button mReSetBtn = null;

	public SetLockPatternView(SetLockPatternActivity activity) {
		mActivity = activity;
		//
		mActivity.setContentView(R.layout.activity_set_lockpattern);
		mLockPatternView= (LockPatternView) mActivity.findViewById(R.id.lock_pattern_view);
		mLockPatternView.setOnPatternListener(mActivity);
		//
		mTipTextView = (TextView) mActivity.findViewById(R.id.tip_textview);
		//
		mReSetBtn = (Button)mActivity.findViewById(R.id.btnReSet);
		mReSetBtn.setOnClickListener(mActivity);

	}

	public void updateView(int status) {
		switch (status) {
		case SetLockPatternActivity.STATUS_FIRST_READY:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.white));
			mReSetBtn.setVisibility(View.INVISIBLE);
			activatePatternView();
			clearPattern();
			mTipTextView.setText(R.string.lock_tip_pattern_ready);
			break;
		case SetLockPatternActivity.STATUS_FIRST_STARTED:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.white));
			mReSetBtn.setVisibility(View.INVISIBLE);
			mTipTextView.setText(R.string.lock_tip_pattern_start);
			break;
		case SetLockPatternActivity.STATUS_TOO_SHORT:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.holo_red_dark));
			mReSetBtn.setVisibility(View.INVISIBLE);
			mTipTextView.setText(R.string.lock_tip_pattern_error_tooless);
			break;
		case SetLockPatternActivity.STATUS_FIRST_OK:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.white));
			mReSetBtn.setVisibility(View.INVISIBLE);
			freezePatternView();
			mTipTextView.setText(R.string.lock_tip_pattern_recored);
			break;
		case SetLockPatternActivity.STATUS_SECOND_READY:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.white));
			mReSetBtn.setVisibility(View.INVISIBLE);
			activatePatternView();
			clearPattern();
			mTipTextView.setText(R.string.lock_tip_pattern_check);
			break;
		case SetLockPatternActivity.STATUS_SECOND_STARTED:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.white));
			mReSetBtn.setVisibility(View.INVISIBLE);
			mTipTextView.setText(R.string.lock_tip_pattern_start);
			//
			break;
		case SetLockPatternActivity.STATUS_SECOND_NOT_MATCHED:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.holo_red_dark));
			mReSetBtn.setVisibility(View.VISIBLE);
			mTipTextView.setText(R.string.lock_tip_pattern_error_checkerror);
			//
			break;
		case SetLockPatternActivity.STATUS_SECOND_OK:
			mTipTextView.setTextColor(mActivity.getResources().getColor(android.R.color.white));
			mReSetBtn.setVisibility(View.INVISIBLE);
			freezePatternView();
			mTipTextView.setText(R.string.lock_tip_pattern_done);
			break;
		}
	}

	/**
	 * 冻结图案View，使其不能被绘制
	 */
	private void freezePatternView() {
		mLockPatternView.setEnabled(false);
	}

	/**
	 * 激活图案View, 使其可以绘制图案
	 */
	private void activatePatternView() {
		mLockPatternView.setEnabled(true);
	}

	/**
	 * 清除图案
	 */
	private void clearPattern() {
		mLockPatternView.clearPattern();
	}
}