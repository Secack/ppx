package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.REMOVE_COMMENT;
import static com.akari.ppx.common.constant.Prefs.REMOVE_COMMENT_KEYWORDS;
import static com.akari.ppx.common.constant.Prefs.REMOVE_COMMENT_USERS;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class CommentHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final ArrayList<String> keywords = new ArrayList<>(), users = new ArrayList<>();
		Utils.parseTextPlusAdd(keywords, XSP.gets(REMOVE_COMMENT_KEYWORDS));
		Utils.parseTextPlusAdd(users, XSP.gets(REMOVE_COMMENT_USERS));
		hookMethod(REMOVE_COMMENT, "com.sup.android.mi.feed.repo.response.a", "a", ArrayList.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				ArrayList comments = (ArrayList) param.args[0];
				for (int i = 0; i < comments.size(); i++) {
					Object comment = null;
					String text, name;
					try {
						comment = callMethod(comments.get(i), "getReply");
						//Object reply2reply = getObjectField(comment, "reply");
					} catch (Error ignored) {
						comment = callMethod(comments.get(i), "getComment");
					} finally {
						name = (String) getObjectField(getObjectField(comment, "userInfo"), "name");
						text = (String) getObjectField(comment, "text");
					}
					for (String keyword : keywords) {
						if (Pattern.matches(keyword, text)) {
							comments.remove(i);
							break;
						}
					}
					for (String user : users) {
						if (Pattern.matches(user, name)) {
							comments.remove(i);
							break;
						}
					}
				}
			}
		});

	}
}
