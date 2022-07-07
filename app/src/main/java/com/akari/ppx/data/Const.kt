package com.akari.ppx.data

import com.akari.ppx.BuildConfig.VERSION_NAME
import com.akari.ppx.data.model.ChannelItem

object Const {
    const val APP_NAME = "皮皮虾助手"
    const val TARGET_APP_ID = "com.sup.android.superb"
    const val PREFS_NAME = "settings"
    const val CACHE_NAME = ".cache1"
    const val CP_URI = "content://com.akari.ppx.CP/"
    const val TAB_SCHEMA = "akari://open_zs"
    const val TELEGRAM_URI = "https://t.me/akari_ppx"
    const val QQ_GROUP_URI =
        "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3DyPG2eOQjhiGYi0HC49NLZm-DFImqmfSb"
    const val GIT_PAGE_URI = "https://github.com/Secack/ppx"
    const val ALIPAY_URI =
        "alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/fkx16213vf1yql4hjgu1k5c"
    private const val BASE_URI = "https://qvq.su/ppx"
    const val LATEST_URI = "$BASE_URI/latest.json"
    const val CURRENT_URI = "$BASE_URI/${VERSION_NAME}.json"
    const val AUTHOR_ID = 50086989143L
    const val CHANNEL_KEY = "channels"
    const val ALLOW_STARTUP_HINT = "请给予助手后台运行权限，否则功能将不会生效"
    const val UNINSTALL_HINT = "请卸载第三方模块后重新启动"
    val CATEGORY_NAMES = arrayOf("关注", "推荐", "视频", "虾聊", "颜值", "真香", "汽车", "图片", "文字", "游戏", "小康")
    val CATEGORY_TYPES = arrayOf(2, 1, 4, 52, 48, 27, 28, 10, 11, 16, 111)
    val CHANNEL_DEFAULT = CATEGORY_NAMES.zip(CATEGORY_TYPES).map {
        ChannelItem(
            name = it.first,
            type = it.second,
            checked = true
        )
    }
}