package com.akari.ppx.xp.hook.code.view;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.REMOVE_BOTTOM_VIEW;

public class BottomHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(REMOVE_BOTTOM_VIEW, "com.sup.android.base.MainActivity", "channelFragmentIsSelected", boolean.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				param.args[0] = true;
			}
		});
	}
}
