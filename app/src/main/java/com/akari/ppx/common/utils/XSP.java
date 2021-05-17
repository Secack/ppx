package com.akari.ppx.common.utils;

import com.akari.ppx.common.constant.Prefs;

import de.robv.android.xposed.XSharedPreferences;

public class XSP {
	public static XSharedPreferences xsp;

	public static void initXSP(XSharedPreferences xsp) {
		XSP.xsp = xsp;
	}

	public static boolean get(Prefs prefs) {
		return xsp.getBoolean(prefs.getKey(), false);
	}

	public static String gets(Prefs prefs, String defValue) {
		return xsp.getString(prefs.getKey(), defValue);
	}

	public static String gets(Prefs prefs) {
		return gets(prefs, "");
	}

	public static int getI(Prefs prefs, int defValue) {
		return Integer.parseInt(gets(prefs, String.valueOf(defValue)));
	}

	public static float getF(Prefs prefs, float defValue) {
		return Float.parseFloat(gets(prefs, String.valueOf(defValue)));
	}
}