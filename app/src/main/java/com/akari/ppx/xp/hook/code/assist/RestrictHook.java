package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.REMOVE_RESTRICTIONS;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class RestrictHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(REMOVE_RESTRICTIONS)) return;
		hookMethod("com.sup.android.mi.feed.repo.bean.cell.AbsFeedItem", "isCanDownload", XC_MethodReplacement.returnConstant(true));
		hookMethod("com.sup.android.mi.feed.repo.bean.comment.Comment", "getCanDownload", XC_MethodReplacement.returnConstant(true));
		hookMethod("com.sup.android.mi.feed.repo.bean.cell.VideoFeedItem", "getVideoDownload", new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				return getObjectField(param.thisObject, "originDownloadVideoModel");
			}
		});
	}
}
