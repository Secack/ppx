package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.List;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.DISABLE_HISTORY_ITEMS;

public class HistoryHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(DISABLE_HISTORY_ITEMS, "com.sup.superb.feedui.repo.a", "a", List.class, XC_MethodReplacement.returnConstant(true));
	}
}
