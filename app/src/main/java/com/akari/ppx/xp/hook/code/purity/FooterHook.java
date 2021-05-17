package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.USE_NEW_FEED_FOOTER_STYLE;

public class FooterHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(USE_NEW_FEED_FOOTER_STYLE, "com.sup.superb.m_feedui_common.util.c", "a", XC_MethodReplacement.returnConstant(true));
	}
}
