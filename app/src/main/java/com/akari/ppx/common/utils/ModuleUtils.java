package com.akari.ppx.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ModuleUtils {
	public static boolean isModuleEnabled_Xposed() {
		return false;
	}

	public static boolean isModuleEnabled_Taichi(Context context) {
		boolean isExp = false;
		try {
			ContentResolver contentResolver = context.getContentResolver();
			Uri uri = Uri.parse("content://me.weishu.exposed.CP/");
			Bundle result = null;
			try {
				result = contentResolver.call(uri, "active", null, null);
			} catch (RuntimeException e) {
				try {
					Intent intent = new Intent("me.weishu.exp.ACTION_ACTIVE");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} catch (Throwable e1) {
					return false;
				}
			}
			if (result == null) {
				result = contentResolver.call(uri, "active", null, null);
			}

			if (result == null) {
				return false;
			}
			isExp = result.getBoolean("active", false);
		} catch (Throwable ignored) {
		}
		return isExp;
	}
}
