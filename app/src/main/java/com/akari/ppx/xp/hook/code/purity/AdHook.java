package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.REMOVE_ADS;

public class AdHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(REMOVE_ADS)) return;
		hookMethod("com.sup.android.mi.feed.repo.bean.ad.AdFeedCell", "getAdInfo", XC_MethodReplacement.returnConstant(null));
		hookMethod("com.sup.android.superb.m_ad.initializer.c", "c", XC_MethodReplacement.returnConstant(false));
		hookMethod("com.sup.android.m_mine.utils.e", "b", XC_MethodReplacement.DO_NOTHING);
		hookMethod("com.sup.android.base.model.BannerModel", "getBannerData", XC_MethodReplacement.returnConstant(null));
	}
}
