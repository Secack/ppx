package com.akari.ppx.xp.hook.code.view;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.REMOVE_DETAIL_BOTTOM_VIEW;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class DetailBottomHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(REMOVE_DETAIL_BOTTOM_VIEW)) return;
		hookConstructor("com.sup.android.detail.util.viewcontroller.a", "com.sup.superb.dockerbase.misc.DockerContext", "com.sup.android.detail.view.DetailBottomView", String.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				callMethod(param.args[1], "setVisibility", 4);
			}
		});
		hookMethod("com.sup.android.detail.util.viewcontroller.a", "a", int.class, boolean.class, XC_MethodReplacement.DO_NOTHING);
	}
}
