package com.akari.ppx.xp.hook.me;

import com.akari.ppx.BuildConfig;
import com.akari.ppx.common.utils.ModuleUtils;
import com.akari.ppx.xp.hook.BaseHook;

import de.robv.android.xposed.XC_MethodReplacement;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class ModuleUtilsHook extends BaseHook {
	@Override
	public void onLoadPackage(String packageName) {
		if (BuildConfig.APPLICATION_ID.equals(packageName))
			findAndHookMethod(ModuleUtils.class.getName(), getCl(), "isModuleEnabled_Xposed", XC_MethodReplacement.returnConstant(true));
	}
}
