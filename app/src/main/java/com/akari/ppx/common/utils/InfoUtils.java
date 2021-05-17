package com.akari.ppx.common.utils;

import com.akari.ppx.common.constant.Prefs;

public class InfoUtils {
	private final String certifyType;
	private final String certifyDesc;
	private final String userName;
	private final String description;
	private final String likeCount;
	private final String followersCount;
	private final String followingCount;
	private final String point;

	public InfoUtils() {
		certifyType = XSP.gets(Prefs.CERTIFY_TYPE, "0");
		certifyDesc = XSP.gets(Prefs.CERTIFY_DESC, "");
		userName = XSP.gets(Prefs.USER_NAME, "");
		description = XSP.gets(Prefs.DESCRIPTION, "");
		likeCount = XSP.gets(Prefs.LIKE_COUNT, "");
		followersCount = XSP.gets(Prefs.FOLLOWERS_COUNT, "");
		followingCount = XSP.gets(Prefs.FOLLOWING_COUNT, "");
		point = XSP.gets(Prefs.POINT, "");
	}

	public String getUserName() {
		return userName;
	}

	public String getCertifyType() {
		return certifyType;
	}

	public String getCertifyDesc() {
		return certifyDesc;
	}

	public String getDescription() {
		return description;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public String getFollowersCount() {
		return followersCount;
	}

	public String getFollowingCount() {
		return followingCount;
	}

	public String getPoint() {
		return point;
	}

}
