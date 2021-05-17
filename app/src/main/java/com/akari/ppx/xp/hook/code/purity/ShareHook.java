package com.akari.ppx.xp.hook.code.purity;

import android.content.Context;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.SIMPLIFY_SHARE;

public class ShareHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(SIMPLIFY_SHARE)) return;
		hookConstructor("com.sup.android.m_sharecontroller.ui.c", Context.class, "[Lcom.sup.android.i_sharecontroller.model.ShareInfo;",
				"[Lcom.sup.android.i_sharecontroller.model.OptionAction$OptionActionType;", "com.sup.android.i_sharecontroller.model.OptionAction$OptionActionListener", "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) {
						param.args[1] = null;
					}
				});
		hookMethod("com.sup.android.m_sharecontroller.service.BaseShareService", "getAllShareletTypes", Context.class, int.class, XC_MethodReplacement.returnConstant(null));
	}
}
