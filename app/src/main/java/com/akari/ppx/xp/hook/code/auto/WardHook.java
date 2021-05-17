package com.akari.ppx.xp.hook.code.auto;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.AUTO_WARD_CONDITION;
import static com.akari.ppx.common.constant.Prefs.AUTO_WARD_ENABLE;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getLongField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

public class WardHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final int condition = XSP.getI(AUTO_WARD_CONDITION, 0);
		Ward ward = new Ward(condition, cl);
		if (!XSP.get(AUTO_WARD_ENABLE)) return;
		switch (condition) {
			case 0:
				hookMethod("com.sup.android.detail.util.o", "a", int.class, long.class, boolean.class, int.class, int.class, ward);
				break;
			case 1:
				hookMethod("com.sup.android.module.publish.publish.PublishLooper$enqueue$1", "invoke", "com.sup.android.mi.publish.bean.PublishBean", ward);
				break;
			case 2:
				hookMethod("com.sup.android.m_comment.util.helper.d", "a", long.class, long.class, "com.sup.android.m_comment.util.helper.g", ward);
				break;
		}

	}

	static class Ward extends XC_MethodHook {
		private final int condition;
		private final ClassLoader cl;

		public Ward(int condition, ClassLoader cl) {
			this.condition = condition;
			this.cl = cl;
		}

		private void wardCell(Object itemId) {
			callMethod(callStaticMethod(findClass("com.sup.android.module.feed.repo.FeedCellService", cl), "getInst"), "wardCell", itemId, 1, true);
		}

		@Override
		protected void beforeHookedMethod(MethodHookParam param) {
			switch (condition) {
				case 0:
					wardCell(param.args[1]);
					break;
				case 1:
					Object bean = param.args[0];
					if ((long) getObjectField(bean, "fakeId") != 666L) {
						setObjectField(bean, "fakeId", 666L);
						wardCell(getLongField(bean, "cellId"));
					}
					break;
				case 2:
					wardCell(param.args[0]);
					break;
				default:
					break;
			}
		}
	}
}
