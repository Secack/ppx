package com.akari.ppx.xp.hook.code.assist;

import android.view.MotionEvent;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.PREVENT_MISTAKEN_TOUCH;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class TouchHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(PREVENT_MISTAKEN_TOUCH)) return;
		hookMethod("com.sup.superb.video.controllerlayer.b.c", "dispatchTouchEvent", MotionEvent.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				try {
					getObjectField(callMethod(param.thisObject, "getActivity"), "mAccountService");
					callMethod(param.thisObject, "setGestureEnable", false);
				} catch (Error ignored) {
				}
			}
		});
		hookMethod("com.sup.superb.video.controllerlayer.b.c", "setGestureEnable", boolean.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				param.args[0] = false;
			}
		});
	}
}
