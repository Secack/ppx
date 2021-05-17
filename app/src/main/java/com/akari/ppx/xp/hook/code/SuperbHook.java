package com.akari.ppx.xp.hook.code;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.akari.ppx.BuildConfig;
import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.BaseHook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class SuperbHook extends BaseHook {
	public static StringBuilder ex;
	private static ClassLoader cl;

	@Override
	public void onLoadPackage(String packageName) {
		if (!"com.sup.android.superb".equals(packageName)) return;
		XSP.initXSP(new XSharedPreferences(BuildConfig.APPLICATION_ID));
		cl = getCl();
		hookMethod("com.sup.android.base.MainActivity", "onCreate", Bundle.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				for (Class<?> hookClass : getHookClasses((Context) param.thisObject))
					((SuperbHook) hookClass.newInstance()).onHook(cl);
			}
		});
	}

	protected void onHook(ClassLoader cl) {
	}

	protected void hookMethod(String className, String methodName, Object... parameterTypesAndCallback) {
		try {
			findAndHookMethod(className, cl, methodName, parameterTypesAndCallback);
		} catch (Error e) {
			handleError(e);
		}
	}

	protected void hookMethod(Prefs prefs, String className, String methodName, Object... parameterTypesAndCallback) {
		if (XSP.get(prefs))
			hookMethod(className, methodName, parameterTypesAndCallback);
	}

	protected void hookConstructor(String className, Object... parameterTypesAndCallback) {
		try {
			findAndHookConstructor(className, cl, parameterTypesAndCallback);
		} catch (Error e) {
			handleError(e);
		}
	}

	protected void hookConstructor(boolean isEnable, String className, Object... parameterTypesAndCallback) {
		if (isEnable)
			hookConstructor(className, parameterTypesAndCallback);
	}

	private List<Class<?>> getHookClasses(Context context) {
		List<Class<?>> list = new ArrayList<>();
		try {
			DexFile df = new DexFile(context.getPackageManager().getApplicationInfo(BuildConfig.APPLICATION_ID, 0).sourceDir);
			Enumeration<String> enums = df.entries();
			while (enums.hasMoreElements()) {
				String className = enums.nextElement();
				if (className.contains("com.akari.ppx.xp.hook.code")) {
					Class<?> clazz = Class.forName(className);
					if (clazz.getSuperclass() == SuperbHook.class)
						list.add(clazz);
				}
			}
		} catch (PackageManager.NameNotFoundException | IOException | ClassNotFoundException ignored) {
		}
		return list;
	}

	private void handleError(Error e) {
		ex = new StringBuilder(e.toString());
		StackTraceElement[] stack = e.getStackTrace();
		for (int i = 0; i < 3; i++)
			ex.append('\n').append(stack[i]);
	}
}
