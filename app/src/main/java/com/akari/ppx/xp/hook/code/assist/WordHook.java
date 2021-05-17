package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.UNLOCK_ILLEGAL_WORD;

public class WordHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(UNLOCK_ILLEGAL_WORD)) return;
		hookMethod("com.sup.android.module.publish.view.NewInputCommentDialog$tryPublish$2", "invoke", String.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				return param.args[0];
			}
		});
		hookMethod("com.sup.android.m_illegalword.utils.RuleTable", "getReplaceMap", XC_MethodReplacement.returnConstant(null));
	}
}
