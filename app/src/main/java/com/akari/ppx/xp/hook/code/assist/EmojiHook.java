package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.UNLOCK_EMOJI;
import static de.robv.android.xposed.XposedHelpers.setIntField;

public class EmojiHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(UNLOCK_EMOJI, "com.sup.android.emoji.EmojiService", "collectEmoticon", "com.sup.android.base.model.ImageModel", long.class, long.class, long.class, "com.sup.android.superb.i_emoji.IEmojiService$SingleCallBack", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				setIntField(param.thisObject, "EMOTICON_MAX_COUNT", 99999);
			}
		});
	}
}
