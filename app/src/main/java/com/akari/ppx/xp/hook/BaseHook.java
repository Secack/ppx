package com.akari.ppx.xp.hook;

public abstract class BaseHook {
	private static ClassLoader cl;

	public abstract void onLoadPackage(String packageName);

	public ClassLoader getCl() {
		return cl;
	}

	public void setCl(ClassLoader cl) {
		BaseHook.cl = cl;
	}
}
