package com.akari.ppx.xp.hook.code.purity;

import android.app.Activity;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.REMOVE_TEENAGER_MODE_DIALOG;

public class TeenHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(REMOVE_TEENAGER_MODE_DIALOG, "com.sup.superb.m_teenager.TeenagerService", "tryShowTeenagerModeDialog", Activity.class, XC_MethodReplacement.DO_NOTHING);
	}
}
