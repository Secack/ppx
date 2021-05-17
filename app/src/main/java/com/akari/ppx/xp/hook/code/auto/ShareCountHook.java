package com.akari.ppx.xp.hook.code.auto;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.MODIFY_SHARE_COUNTS;
import static de.robv.android.xposed.XposedBridge.invokeOriginalMethod;

public class ShareCountHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(MODIFY_SHARE_COUNTS, "com.sup.android.module.feed.repo.FeedCellService", "shareCell", long.class, int.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				for (int i = 0; i < 99; i++)
					invokeOriginalMethod(param.method, param.thisObject, param.args);
				return invokeOriginalMethod(param.method, param.thisObject, param.args);
			}
		});
	}
}
