package com.akari.ppx.xp.hook.code.fun;

import com.akari.ppx.common.utils.InfoUtils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Const.AUTHOR_ID;
import static com.akari.ppx.common.constant.Prefs.DIY_ENABLE;
import static com.akari.ppx.common.constant.Prefs.MODIFY_MESSAGE_COUNTS;
import static com.akari.ppx.common.constant.Prefs.PUNISH_ENABLE;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getLongField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;
import static de.robv.android.xposed.XposedHelpers.setLongField;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

public class InfoHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final boolean diyEnable = XSP.get(DIY_ENABLE), punishEnable = XSP.get(PUNISH_ENABLE);
		final InfoUtils mInfo = new InfoUtils();
		final String[] mName = new String[]{""};
		hookMethod("com.sup.android.uikit.avatar.FrameAvatarView", "setUserInfo", "com.sup.android.mi.usercenter.model.UserInfo", int.class, new XC_MethodHook() {
			private void setField(Object obj, String fieldName, String value) {
				if (!"".equals(value)) {
					try {
						setLongField(obj, fieldName, Long.parseLong(value));
					} catch (Exception ignored) {
					}
				}
			}

			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				Object userInfo = param.args[0];
				if (getLongField(userInfo, "id") == AUTHOR_ID) {
					final String desc = "皮皮虾助手开发者";
					Object certifyInfo = newInstance(findClass("com.sup.android.mi.usercenter.model.UserInfo$CertifyInfo", cl));
					callMethod(certifyInfo, "setCertifyType", 2);
					callMethod(certifyInfo, "setDescription", desc);
					setObjectField(userInfo, "certifyInfo", certifyInfo);
					setObjectField(userInfo, "description", desc);
				}
				if (diyEnable) {
					if ("".equals(mName[0])) {
						try {
							mName[0] = (String) getObjectField(callMethod(callStaticMethod(findClass("com.sup.android.module.usercenter.UserCenterService", cl), "getInstance"), "getMyUserInfo"), "name");
						} catch (Exception ignored) {
							mName[0] = "";
						}
					}
					if (getObjectField(userInfo, "name").equals(mName[0])) {
						if (punishEnable) {
							Object Punishment = newInstance(findClass("com.sup.android.mi.usercenter.model.UserInfo$Punishment", cl));
							setObjectField(Punishment, "shortDesc", "已被移送小黑屋");
							ArrayList list = new ArrayList();
							list.add(Punishment);
							setObjectField(userInfo, "punishmentList", list);
						}
						int certifyType = Integer.parseInt(mInfo.getCertifyType());
						if (certifyType > 0 && certifyType <= 4) {
							Object certifyInfo = newInstance(findClass("com.sup.android.mi.usercenter.model.UserInfo$CertifyInfo", cl));
							callMethod(certifyInfo, "setCertifyType", certifyType);
							String desc = mInfo.getCertifyDesc();
							callMethod(certifyInfo, "setDescription", !"".equals(desc) ? desc : "阿勒，忘填描述了？");
							setObjectField(userInfo, "certifyInfo", certifyInfo);
						}
						String name = mInfo.getUserName();
						if (!"".equals(name))
							setObjectField(userInfo, "name", name);
						String desc = mInfo.getDescription();
						if (!"".equals(desc))
							setObjectField(userInfo, "description", desc);
						setField(userInfo, "likeCount", mInfo.getLikeCount());
						setField(userInfo, "followersCount", mInfo.getFollowersCount());
						setField(userInfo, "followingCount", mInfo.getFollowingCount());
						setField(userInfo, "point", mInfo.getPoint());
					}
				}
			}
		});
		hookMethod(MODIFY_MESSAGE_COUNTS, "com.google.gson.Gson", "fromJson", String.class, Class.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (diyEnable && "com.sup.android.m_message.data.t".equals(((Class) param.args[1]).getName())) {
					for (Object i : (List) getObjectField(getObjectField(param.getResult(), "data"), "count_map")) {
						setObjectField(i, "count", 100);
					}
				}
			}
		});
	}
}
