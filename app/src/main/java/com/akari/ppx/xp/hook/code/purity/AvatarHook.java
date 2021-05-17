package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.REMOVE_AVATAR_DECORATION;
import static de.robv.android.xposed.XposedHelpers.getIntField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class AvatarHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(REMOVE_AVATAR_DECORATION, "com.sup.android.mi.usercenter.model.UserInfo", "getDecorationList", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				List list = (List) param.getResult();
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						List infos = (List) getObjectField(list.get(i), "decorationInfos");
						if (getIntField(infos.get(0), "decorationType") == 2) {
							list.remove(i);
						}
					}
				}
			}
		});
	}
}
