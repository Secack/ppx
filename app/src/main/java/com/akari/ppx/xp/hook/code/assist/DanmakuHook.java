package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.UNLOCK_DANMAKU;
import static de.robv.android.xposed.XposedHelpers.setBooleanField;

public class DanmakuHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(UNLOCK_DANMAKU, "com.sup.android.mi.usercenter.model.UserInfo", "getUserPrivilege", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				setBooleanField(param.getResult(), "canSendAdvanceDanmaku", true);
			}
		});
	}
}
