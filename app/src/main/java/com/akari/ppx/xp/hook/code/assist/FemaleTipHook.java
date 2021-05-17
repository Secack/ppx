package com.akari.ppx.xp.hook.code.assist;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.SHOW_FEMALE_TIP;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class FemaleTipHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final boolean[] entered = new boolean[1];
		if (!XSP.get(SHOW_FEMALE_TIP)) return;
		final boolean[] isFemale = new boolean[1];
		hookMethod("com.sup.android.mi.feed.repo.utils.AbsFeedCellUtil$Companion", "getUserName", "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				isFemale[0] = 2 == (int) callMethod(callMethod(param.thisObject, "getAuthorInfo", param.args[0]), "getGender");
			}
		});
		hookMethod("com.sup.android.detail.viewholder.item.q", "a", "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				entered[0] = true;
			}

			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				entered[0] = false;
			}
		});
		hookMethod("com.sup.android.m_comment.docker.holder.c", "b", boolean.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				entered[0] = true;
			}
		});
		hookMethod("com.sup.android.utils.KotlinExtensionKt", "setViewWidth", View.class, int.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				entered[0] = false;
			}
		});
		hookMethod("android.widget.TextView", "setText", CharSequence.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				if (entered[0] && isFemale[0]) {
					String a = (String) param.args[0];
					SpannableString str = new SpannableString(a);
					str.setSpan(new ForegroundColorSpan(-38784), 0, a.length(), 33);
					param.args[0] = str;
				}
			}
		});
	}
}
