package org.wordpress.patternlock;

import java.util.ArrayList;
import java.util.List;

import org.wordpress.patternlock.LockPatternView.Cell;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class LockPatternUtils {

	private static final String KEY_LOCK_PATTERN = "KeyLockPattern";

	public final static int MIN_LENGTH_OF_CELLS = 4;

	public static List<LockPatternView.Cell> stringToPattern(String string) {
		List<LockPatternView.Cell> result = new ArrayList<LockPatternView.Cell>();

		final byte[] bytes = string.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			result.add(LockPatternView.Cell.of(b / 3, b % 3));
		}
		return result;
	}

	public static String patternToString(List<LockPatternView.Cell> pattern) {
		if (pattern == null) {
			return "";
		}
		final int patternSize = pattern.size();

		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			LockPatternView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}
		return new String(res);
	}

	public static void saveToPreferences(Context context, String lockPattern) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(KEY_LOCK_PATTERN, lockPattern);
		editor.commit();
	}

	public static String loadFromPreferences(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(KEY_LOCK_PATTERN, null);
	}
	public static void clearPreferences(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().clear().commit();
	}
	public static String convertToSequence(List<Cell> pattern) {
		if (pattern == null || pattern.size() < MIN_LENGTH_OF_CELLS) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer(pattern.size());
		for (Cell cell : pattern) {
			strBuff.append(cell.getRow());
			strBuff.append(cell.getColumn());
		}
		return strBuff.toString();
	}

}