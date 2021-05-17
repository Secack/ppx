package com.akari.ppx.xp;

import com.akari.ppx.xp.hook.BaseHook;
import com.akari.ppx.xp.hook.code.SuperbHook;
import com.akari.ppx.xp.hook.me.ModuleUtilsHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookEntry implements IXposedHookLoadPackage {
	private final List<BaseHook> hookList;

	{
		hookList = new ArrayList<>();
		hookList.add(new ModuleUtilsHook());
		hookList.add(new SuperbHook());
	}

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
		for (BaseHook hook : hookList) {
			hook.setCl(lpparam.classLoader);
			hook.onLoadPackage(lpparam.packageName);
		}
	}
}
