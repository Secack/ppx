package com.akari.ppx.xp.hook.code.auto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.AUTO_BROWSE_ENABLE;
import static com.akari.ppx.common.constant.Prefs.AUTO_BROWSE_VIDEO_MODE;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class BrowseHook extends SuperbHook {
	private static boolean autoBrowse = true;
	private static Object viewPager;

	public static boolean getAutoBrowse() {
		return autoBrowse;
	}

	public static void setAutoBrowse(boolean autoBrowse) {
		BrowseHook.autoBrowse = autoBrowse;
	}

	public static Object getViewPager() {
		return viewPager;
	}

	@Override
	protected void onHook(ClassLoader cl) {
		//ui.j
		//ui.DetailPagerFragment
		hookMethod(AUTO_BROWSE_ENABLE, "com.sup.android.detail.ui.DetailPagerFragment", "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				viewPager = getObjectField(param.thisObject, "A");
			}

		});
		hookMethod(AUTO_BROWSE_VIDEO_MODE, "com.sup.superb.video.viewholder.a", "onPlayerStateChanged", int.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				//3.5.0: ab
				//3.5.1: P
				Object cell = callMethod(param.thisObject, "P");
				if (cell != null && (int) callMethod(cell, "getCellType") == 1 && (int) param.args[0] == 5) {
					Object viewPager = BrowseHook.viewPager;
					if (viewPager != null) {
						callMethod(viewPager, "postDelayed", new ItemThread(viewPager), 0);
					}
				}
			}
		});
	}
}
