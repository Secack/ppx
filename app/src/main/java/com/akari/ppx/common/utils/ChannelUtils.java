package com.akari.ppx.common.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.entity.ChannelEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ChannelUtils {
	private static SharedPreferences preferences;

	public static void initSP(SharedPreferences preferences) {
		ChannelUtils.preferences = preferences;
	}

	@SuppressLint("ApplySharedPref")
	public static void setSPDataList(Prefs prefs, List<ChannelEntity> list) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(prefs.getKey(), new Gson().toJson(list));
		editor.apply();
	}

	public static List<ChannelEntity> getSPDataList(Prefs prefs) {
		return getDataList(preferences.getString(prefs.getKey(), ""));
	}

	@SuppressLint("ApplySharedPref")
	public static void setDefaultChannel(String name) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(Prefs.DEFAULT_CHANNEL.getKey(), name);
		editor.apply();
	}

	public static List<ChannelEntity> getDataList(String content) {
		try {
			List<ChannelEntity> dataList;
			Gson gson = new Gson();
			dataList = gson.fromJson(content, new TypeToken<List<ChannelEntity>>() {
			}.getType());
			return dataList;
		} catch (Exception e) {
			return null;
		}
	}
}
