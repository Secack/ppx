package com.akari.ppx.xp.hook.code.misc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.akari.ppx.BuildConfig;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static de.robv.android.xposed.XposedBridge.invokeOriginalMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class InnerOpenHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod("com.sup.android.m_mine.utils.f", "a", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				ArrayList list = (ArrayList) param.getResult();
				if(list.size() == 0){
					return;
				}
				Object item = list.get(1);
				callMethod(item, "setSchemaNeedLogin", Boolean.FALSE);
				callMethod(item, "setTabName", "皮皮虾助手");
				callMethod(item, "setTabSchema", "akari://open");
				callMethod(item, "setExtra", "{\"icon_list\":null,\"alert\":false}");
				callMethod(item, "setType", 4);
			}
		});
		hookMethod("com.sup.android.m_mine.view.subview.e$1", "doClick", View.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				Object thiz = getObjectField(param.thisObject, "b");
				String schema = (String) callMethod(callStaticMethod(findClass("com.sup.android.m_mine.view.subview.e", cl), "b", thiz), "getTabSchema");
				if ("akari://open".equals(schema)) {
					Context context = ((View) getObjectField(thiz, "i")).getContext();
					Intent intent = new Intent();
					intent.setComponent(new ComponentName(BuildConfig.APPLICATION_ID, "com.akari.ppx.ui.home.HomeActivity"));
					context.startActivity(intent);
					return null;
				}
				return invokeOriginalMethod(param.method, param.thisObject, param.args);
			}
		});
	}
}
