package com.akari.ppx.common.constant;

import com.akari.ppx.BuildConfig;

public interface Const {
	String TEST = "测试";
	String NOW = "当前：";
	String BANNED = "被拉黑，即将退出";
	String ALIPAY_URI = "alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/fkx16213vf1yql4hjgu1k5c";
	String GITHUB_URI = "https://github.com/Secack/ppx";
	String HOME_ACTIVITY_ALIAS = BuildConfig.APPLICATION_ID + ".HomeActivityAlias";
	String XSPreferencesName = BuildConfig.APPLICATION_ID + "_preferences";
	String[] CATEGORY_LIST_NAME = {"关注", "推荐", "视频", "虾聊", "全部", "颜值", "真香", "直播", "汽车", "图片", "文字", "游戏", "小康"};
	int[] CATEGORY_LIST_TYPE = {2, 55, 4, 52,1, 48, 27, 50, 28, 10, 11, 16, 111};
	long AUTHOR_ID = 50086989143L;
}
