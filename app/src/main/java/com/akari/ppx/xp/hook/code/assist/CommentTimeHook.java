package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.EXACT_COMMENT_TIME_FORMAT;
import static com.akari.ppx.common.constant.Prefs.SHOW_COMMENT_TIME;
import static com.akari.ppx.common.constant.Prefs.TODAY_COMMENT_TIME_FORMAT;

public class CommentTimeHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final String todayFormat = XSP.gets(TODAY_COMMENT_TIME_FORMAT, "今天HH点mm分ss秒"), exactFormat = XSP.gets(EXACT_COMMENT_TIME_FORMAT, "yyyy-MM-dd HH:mm:ss");
		hookMethod(SHOW_COMMENT_TIME, "com.sup.superb.m_feedui_common.util.a", "a", long.class, "kotlin.jvm.functions.Function0", new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				return Utils.ts2date((Long) param.args[0], Utils.isToday((Long) param.args[0]) ? todayFormat : exactFormat, false);
			}
		});

	}
}
