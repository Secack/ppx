package com.akari.ppx.ui.home;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import com.akari.ppx.BuildConfig;
import com.akari.ppx.R;
import com.akari.ppx.ui.BaseActivity;

public class HomeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home_content, new HomeFragment(this))
				.commit();
		setSupportActionBar(findViewById(R.id.toolbar));
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(String.format("%s %s", getString(R.string.app_name), BuildConfig.VERSION_NAME));
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}
}
