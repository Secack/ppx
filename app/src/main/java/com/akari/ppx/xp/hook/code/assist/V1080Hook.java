package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.UNLOCK_1080P;

public class V1080Hook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(UNLOCK_1080P, "com.sup.android.m_chooser.impl.c", "b", int.class, int.class, XC_MethodReplacement.returnConstant(true));
	}
}
