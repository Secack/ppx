package com.akari.ppx.xp.hook.code.view;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.REMOVE_PUBLISH_BUTTON;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class PublishButtonHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(REMOVE_PUBLISH_BUTTON, "com.sup.android.base.MainActivity", "onWindowFocusChanged", boolean.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				callMethod(callMethod(param.thisObject, "getAutoPlayPopupBottomView"), "setVisibility", 4);
			}
		});
	}
}
