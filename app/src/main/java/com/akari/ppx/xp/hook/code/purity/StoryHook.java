package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.REMOVE_STORIES;

public class StoryHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(REMOVE_STORIES, "com.sup.android.mi.feed.repo.bean.cell.StoryInfo", "getStoryList", XC_MethodReplacement.returnConstant(null));
	}
}