package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

public class RedDotsHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(Prefs.REMOVE_RED_DOTS)) return;



		//updateFollowFeedRedDot
		//a
		hookMethod("com.sup.superb.feedui.view.FeedTabFragment", "a", boolean.class, int.class, int.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				param.args[0] = false;
				param.args[1] = 0;
			}
		});
		hookMethod("com.sup.android.base.MainActivity", "handleBadgeAndPopup", long.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				param.args[0] = 0L;
			}
		});
		hookMethod("com.sup.android.m_mine.view.subview.MyProfileHeaderLayout", "a", boolean.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				param.args[0] = false;
			}
		});
	}
}
