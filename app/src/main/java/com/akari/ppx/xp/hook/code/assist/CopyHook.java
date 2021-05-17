package com.akari.ppx.xp.hook.code.assist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;

import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.COPY_ITEM;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class CopyHook extends SuperbHook {
	private final String OptionActionType = "ACTION_PI";

	@SuppressLint("WrongConstant")
	private void copyText(ClassLoader cl, Object activity, Object absFeedCell) {
		String text;
		try {
			text = (String) callMethod(callMethod(absFeedCell, "getFeedItem"), "getContent");
		} catch (Error ignored) {
			text = (String) callMethod(callMethod(absFeedCell, "getComment"), "getText");
		}
		((ClipboardManager) ((Activity) activity).getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(text, text));
		Utils.showSystemToastXP(cl, "复制成功");
	}

	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(COPY_ITEM)) return;
		hookMethod("com.sup.android.mi.feed.repo.utils.AbsFeedCellUtil$Companion", "canBeRecreated", "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", XC_MethodReplacement.returnConstant(true));
		hookMethod("android.view.View", "setTag", Object.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				try {
					if (Enum.valueOf((Class) findClass("com.sup.android.i_sharecontroller.model.OptionAction$OptionActionType", cl), OptionActionType).equals(param.args[0])) {
						callMethod(callMethod(param.thisObject, "getChildAt", 1), "setText", "复制文字");
					}
				} catch (Error ignored) {
				}
			}
		});
		hookMethod("com.sup.android.base.feed.a", "a", Activity.class, "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", String.class, String.class, String.class, boolean.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				copyText(cl, param.args[0], param.args[1]);
				return null;
			}
		});
		hookMethod("com.sup.android.base.e.a", "a", Activity.class, "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", String.class, String.class, String.class, HashMap.class, boolean.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				copyText(cl, param.args[0], param.args[1]);
				return null;
			}
		});
		hookMethod("kotlin.jvm.internal.Intrinsics", "throwUninitializedPropertyAccessException", String.class, XC_MethodReplacement.DO_NOTHING);
	}
}
