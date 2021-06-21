package com.akari.ppx.xp.hook.code.assist;

import android.view.MotionEvent;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

import static com.akari.ppx.common.constant.Prefs.KEEP_VIDEO_PLAY_SPEED;
import static de.robv.android.xposed.XposedBridge.invokeOriginalMethod;

public class KeepSpeedHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(KEEP_VIDEO_PLAY_SPEED)) return;

		final boolean[] longPressing = new boolean[2], firstPress = new boolean[2];

		hookMethod("com.sup.superb.video.viewholder.a$b", "onLongPress", MotionEvent.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				longPressing[0] = true;
				firstPress[0] = !firstPress[0];
			}
		});
		hookMethod("com.sup.superb.video.viewholder.a$b", "a", MotionEvent.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				if (longPressing[0]) {
					if (!firstPress[0])
						invokeOriginalMethod(param.method, param.thisObject, param.args);
					longPressing[0] = false;
				}
				return null;
			}
		});
		//3.4.5 j_
		//3.5.0 k_
		//3.5.1 i_
		hookMethod("com.sup.superb.video.viewholder.a", "i_", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				longPressing[1] = true;
				firstPress[1] = !firstPress[1];
			}
		});

		//3.5.9 Y
		//3.5.1 j_
		hookMethod("com.sup.superb.video.viewholder.a", "j_", new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				if (longPressing[1]) {
					if (!firstPress[1])
						invokeOriginalMethod(param.method, param.thisObject, param.args);
					longPressing[1] = false;
				}
				return null;
			}
		});
		hookMethod("com.sup.android.supvideoview.videoview.SupVideoView", "getPlayState", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				int playState = (int) param.getResult();
				if (playState == 0 || playState == 5)
					Arrays.fill(firstPress, false);
			}
		});
	}
}