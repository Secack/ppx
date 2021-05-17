package com.akari.ppx.ui.channel;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import com.akari.ppx.R;
import com.akari.ppx.ui.BaseActivity;

public class ChannelActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home_content, new ChannelFragment(this))
				.commit();
		setSupportActionBar(findViewById(R.id.toolbar));
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(getString(R.string.channel_title));
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

}
