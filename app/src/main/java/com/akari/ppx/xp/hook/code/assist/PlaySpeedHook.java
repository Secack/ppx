package com.akari.ppx.xp.hook.code.assist;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.math.BigDecimal;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.SET_VIDEO_PLAY_SPEED;

public class PlaySpeedHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final float playSpeed = XSP.getF(SET_VIDEO_PLAY_SPEED, 2.0f);
		final boolean[] entered = new boolean[1];
		if (playSpeed == 2.0f) return;
		hookMethod("com.sup.android.supvideoview.videoview.SupVideoView", "setPlaySpeed", float.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				if ((float) param.args[0] == 2.0f) {
					param.args[0] = playSpeed;
				}
			}
		});
		//i
		//j
		hookMethod("com.sup.superb.video.controllerlayer.j", "z", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				entered[0] = true;
			}

			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				entered[0] = false;
			}
		});
		hookMethod("android.widget.TextView", "setText", CharSequence.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				if (entered[0]) {
					String speed = new BigDecimal(String.valueOf(playSpeed)).stripTrailingZeros().toPlainString();
					SpannableString str = new SpannableString(speed + "X快进中");
					str.setSpan(new ForegroundColorSpan(-38784), 0, speed.length() + 1, 33);
					param.args[0] = str;
				}
			}
		});
	}
}
