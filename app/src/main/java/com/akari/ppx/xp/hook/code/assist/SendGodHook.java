package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.AUTO_SEND_GOD_ENABLE;
import static com.akari.ppx.common.constant.Prefs.AUTO_SEND_GOD_TIME_LIMIT;
import static com.akari.ppx.common.constant.Prefs.UNLOCK_SEND_GOD;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getBooleanField;
import static de.robv.android.xposed.XposedHelpers.getIntField;
import static de.robv.android.xposed.XposedHelpers.getLongField;
import static de.robv.android.xposed.XposedHelpers.newInstance;
import static de.robv.android.xposed.XposedHelpers.setLongField;

public class SendGodHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final boolean autoSendGod = XSP.get(AUTO_SEND_GOD_ENABLE);
		final int timeLimit = XSP.getI(AUTO_SEND_GOD_TIME_LIMIT, 21600);
		hookMethod(UNLOCK_SEND_GOD, "com.sup.android.mi.feed.repo.bean.comment.Comment", "getSendGodStatus", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (getLongField(param.thisObject, "aliasItemId") == 1 && !getBooleanField(param.thisObject, "hasLiked")) {
					setLongField(param.thisObject, "aliasItemId", 2);
					callMethod(newInstance(findClass("com.sup.android.detail.util.o", cl)), "a",
							8, getLongField(param.thisObject, "commentId"), true, 10, 1);
					if (autoSendGod && System.currentTimeMillis() / 1000 - getLongField(param.thisObject, "createTime") <= timeLimit)
						callMethod(newInstance(findClass("com.sup.android.m_comment.util.helper.d", cl)), "a",
								getLongField(param.thisObject, "itemId"), getLongField(param.thisObject, "commentId"), null);
					param.setResult(1);
					return;
				}
				if (getIntField(param.thisObject, "sendGodStatus") == 3) {
					if (getLongField(param.thisObject, "aliasItemId") == 2) {
						param.setResult(autoSendGod && System.currentTimeMillis() / 1000 - getLongField(param.thisObject, "createTime") <= timeLimit ? 2 : 1);
						return;
					}
					setLongField(param.thisObject, "aliasItemId", 1);
					param.setResult(1);
				}
			}
		});

	}
}
