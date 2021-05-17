package com.akari.ppx.ui;

import android.app.Application;

import com.jaredrummler.cyanea.Cyanea;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Cyanea.init(this, super.getResources());
		Cyanea.getInstance().edit()
				.baseTheme(Cyanea.BaseTheme.LIGHT)
				.accent(-38784).primary(-38784)
				.apply();
	}
}
