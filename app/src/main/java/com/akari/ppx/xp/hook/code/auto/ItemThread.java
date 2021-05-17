package com.akari.ppx.xp.hook.code.auto;

import static de.robv.android.xposed.XposedHelpers.callMethod;

public class ItemThread implements Runnable {
	private final Object viewPager;

	public ItemThread(Object viewPager) {
		this.viewPager = viewPager;
	}

	@Override
	public void run() {
		callMethod(viewPager, "setCurrentItem", (int) callMethod(viewPager, "getCurrentItem") + 1);
	}
}
