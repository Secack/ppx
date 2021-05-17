package com.akari.ppx.xp.hook.code.purity;

import android.content.Context;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.DISABLE_UPDATE;

public class UpdateHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(DISABLE_UPDATE, "com.sup.android.m_update.UpdateService", "checkUpdateByAutomatic", Context.class, XC_MethodReplacement.DO_NOTHING);
	}
}
