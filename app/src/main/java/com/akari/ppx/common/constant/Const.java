package com.akari.ppx.common.constant;

import com.akari.ppx.BuildConfig;

public interface Const {
	String TEST = "测试";
	String NOW = "当前：";
	String BANNED = "被拉黑，即将退出";
	String ALIPAY_URI = "alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/fkx16213vf1yql4hjgu1k5c";
	String ALIPAY_EDIT_URI = "alipays://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/aex00733fo8pe2wengwll19";
	String QQ_GROUP_URI = "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D6sN70t6K0eKimPrPECMNbigIH3BEI9E6";
	String GITHUB_URI = "https://github.com/Secack/ppx";
	String GITHUB_URI_EDIT = "https://github.com/tpnet/ppxhelper";
	String HOME_ACTIVITY_ALIAS = BuildConfig.APPLICATION_ID + ".HomeActivityAlias";
	String XSPreferencesName = BuildConfig.APPLICATION_ID + "_preferences";
	//删除放映厅
	String[] CATEGORY_LIST_NAME = {"关注", "推荐", "视频", "虾聊", "颜值", "真香", "直播", "汽车", "图片", "文字", "游戏", "小说", "小康"};
	//删除放映厅15
	int[] CATEGORY_LIST_TYPE = {2, 1, 4, 52, 48, 27, 50, 28, 10, 11, 16, 666666, 111};
	long AUTHOR_ID = 50086989143L;
}
