package com.akari.ppx.common.constant;

public enum Prefs {
	SAVE_IMAGE("pref_save_image"),
	SAVE_VIDEO("pref_save_video"),
	REMOVE_ADS("pref_remove_ads"),
	REMOVE_STORIES("pref_remove_stories"),
	REMOVE_RED_DOTS("pref_remove_red_dots"),
	DISABLE_UPDATE("pref_disable_update"),
	REMOVE_TEENAGER_MODE_DIALOG("pref_remove_teenager_mode_dialog"),
	SIMPLIFY_SHARE("pref_simplify_share"),
	USE_NEW_FEED_FOOTER_STYLE("pref_use_new_feed_footer_style"),
	REMOVE_COMMENT("pref_remove_comment"),
	REMOVE_COMMENT_KEYWORDS("pref_remove_comment_keywords"),
	REMOVE_COMMENT_USERS("pref_remove_comment_users"),
	REMOVE_AVATAR_DECORATION("pref_remove_avatar_decoration"),
	DISABLE_HISTORY_ITEMS("pref_disable_history_items"),
	DIY_CATEGORY_LIST("pref_diy_category_list"),
	MY_CHANNEL("pref_my_channel"),
	OTHER_CHANNEL("pref_other_channel"),
	DEFAULT_CHANNEL("pref_default_channel"),
	REMOVE_RESTRICTIONS("pref_remove_restrictions"),
	SHOW_REGISTER_ESCAPE_TIME("pref_show_register_escape_time"),
	SAVE_AUDIO("pref_save_audio"),
	COPY_ITEM("pref_copy_item"),
	UNLOCK_ILLEGAL_WORD("pref_unlock_illegal_word"),
	UNLOCK_DANMAKU("pref_unlock_danmaku"),
	UNLOCK_1080P("pref_unlock_1080p_limit"),
	UNLOCK_EMOJI("pref_unlock_emoji_limit"),
	UNLOCK_SEND_GOD("pref_unlock_send_god_limit"),
	UNLOCK_VIDEO_COMMENT("pref_unlock_video_comment_limit"),
	PREVENT_MISTAKEN_TOUCH("pref_prevent_mistaken_touch"),
	SHOW_FEMALE_TIP("pref_show_female_tip"),
	SHOW_COMMENT_TIME("pref_show_exact_comment_time"),
	TODAY_COMMENT_TIME_FORMAT("pref_show_today_comment_time_format"),
	EXACT_COMMENT_TIME_FORMAT("pref_show_exact_comment_time_format"),
	KEEP_VIDEO_PLAY_SPEED("pref_keep_video_play_speed"),
	SET_VIDEO_PLAY_SPEED("pref_set_video_play_speed"),
	AUTO_BROWSE_ENABLE("pref_auto_browse_enable"),
	AUTO_BROWSE_FREQUENCY("pref_auto_browse_frequency"),
	AUTO_BROWSE_VIDEO_MODE("pref_auto_browse_video_mode"),
	AUTO_DIGG_ENABLE("pref_auto_digg_enable"),
	DIGG_STYLE("pref_digg_style"),
	AUTO_DIGG_PAUSE("pref_auto_digg_pause"),
	AUTO_COMMENT_ENABLE("pref_auto_comment_enable"),
	AUTO_COMMENT_TEXT("pref_auto_comment_text"),
	AUTO_COMMENT_PAUSE("pref_auto_comment_pause"),
	AUTO_WARD_ENABLE("pref_auto_ward_enable"),
	AUTO_WARD_CONDITION("pref_auto_ward_condition"),
	AUTO_SEND_GOD_ENABLE("pref_auto_send_god_enable"),
	AUTO_SEND_GOD_TIME_LIMIT("pref_auto_send_god_time_limit"),
	MODIFY_SHARE_COUNTS("pref_modify_share_counts"),
	MODIFY_MESSAGE_COUNTS("pref_modify_message_counts"),
	REMOVE_BOTTOM_VIEW("pref_remove_bottom_view"),
	REMOVE_PUBLISH_BUTTON("pref_remove_publish_button"),
	REMOVE_DETAIL_BOTTOM_VIEW("pref_remove_detail_bottom_view"),
	DIY_ENABLE("pref_diy_enable"),
	USER_NAME("pref_user_name"),
	PUNISH_ENABLE("pref_punish_enable"),
	CERTIFY_TYPE("pref_certify_type"),
	CERTIFY_DESC("pref_certify_desc"),
	DESCRIPTION("pref_description"),
	LIKE_COUNT("pref_like_count"),
	FOLLOWERS_COUNT("pref_followers_count"),
	FOLLOWING_COUNT("pref_following_count"),
	POINT("pref_point"),
	DONATE("pref_donate_by_alipay"),
	DONATEEDIT("pref_donate_by_alipay_edit"),
	JOIN_QQ_GROUP("pref_join_qq_group"),
	HIDE_LAUNCHER_ICON("pref_hide_launcher_icon"),
	SOURCE_CODE("pref_source_code"),
	SOURCE_CODE_EDIT("pref_source_code_edit");
	private final String key;

	Prefs(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static Prefs fromString(String key) throws IllegalArgumentException {
		for (Prefs prefs : Prefs.values()) {
			if (key.equals(prefs.key)) {
				return prefs;
			}
		}
		throw new IllegalArgumentException("unknown key:" + key);
	}
}