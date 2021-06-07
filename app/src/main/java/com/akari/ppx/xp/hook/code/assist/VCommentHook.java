package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.UNLOCK_VIDEO_COMMENT;

public class VCommentHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final boolean[] callStatus = new boolean[1];
		if (!XSP.get(UNLOCK_VIDEO_COMMENT)) return;
		//r
		//k
		hookMethod("com.sup.android.module.publish.view.k", "a", boolean.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				callStatus[0] = true;
			}

			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				callStatus[0] = false;
			}
		});
		hookMethod("android.widget.ImageView", "setVisibility", int.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				if ((int) param.args[0] == 8 && callStatus[0])
					param.args[0] = 0;
			}
		});

	}
}
