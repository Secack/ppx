package com.akari.ppx.data

import android.content.Context
import com.akari.ppx.data.Const.CATEGORY_NAMES
import com.akari.ppx.data.Const.CATEGORY_TYPES
import com.akari.ppx.data.model.CheckBoxItem
import com.akari.ppx.utils.hideIcon

val prefTabs = listOf("主页", "辅助", "自动", "杂项", "关于")

val prefItems: List<List<PrefItem?>> = prefTabs.mapIndexed { index, _ ->
    when (Page.fromIndex(index)) {
        Page.PURITY -> {
            listOf(
                SwitchItem(
                    key = "save_image",
                    title = "图片去水印",
                    summary = "图片/动图无损保存"
                ),
                SwitchItem(
                    key = "save_video",
                    title = "视频去水印",
                    summary = "下载链接重解析为播放链接"
                ),
                SwitchItem(
                    key = "remove_ads",
                    title = "去除广告",
                    summary = "去除启动页/帖子/评论/小程序等广告"
                ),
                SwitchItem(
                    key = "remove_red_dots",
                    title = "去除红点",
                    summary = "去除关注频道/底栏/插眼红点"
                ),
                SwitchItem(
                    key = "remove_stories",
                    title = "去除头像列表",
                    summary = "去除推荐/关注频道下方头像列表"
                ),
                SwitchItem(
                    key = "disable_update",
                    title = "屏蔽更新",
                    summary = "不显示更新提示/禁用自动更新"
                ),
                SwitchItem(
                    key = "remove_teenager_dialog",
                    title = "关闭青少年模式弹窗",
                    summary = "仍可从设置中进入青少年模式"
                ),
                SwitchItem(
                    key = "simplify_share",
                    title = "精简分享",
                    summary = "移除分享到QQ/QQ空间/微信/朋友圈"
                ),
                SwitchItem(
                    key = "remove_comments",
                    title = "屏蔽评论（正则表达式，以|分割）",
                    summary = "依据关键词/用户名屏蔽评论"
                ),
                EditItem(
                    key = "remove_comments_keywords",
                    dependency = "remove_comments",
                    title = "关键词",
                    default = """\[((?!b).)*\]|.*送了.*|.*直接送.*|.*已送.*|.*想送.*|.*送|.*必神.*|.*神了.*|.*送神.*|.*神评.*|.*要神.*|冲.*|.*来晚了|.*尊/|.*\uD83D\uDD25.*|.*业绩.*|.*青铜.*|.*白银.*|.*黄金.*|.*钻石.*|.*至尊.*|.*啃|.*冲锋|💎|，|。|走量|""",
                    multi = true
                ),
                EditItem(
                    key = "remove_comments_users",
                    dependency = "remove_comments",
                    title = "用户名",
                    default = """至尊军团.*|散仙阁.*|神评学院.*|""",
                    multi = true
                ),
                SwitchItem(
                    key = "remove_feeds",
                    title = "屏蔽帖子（正则表达式，以|分割）",
                    summary = "依据关键词/用户名屏蔽帖子"
                ),
                EditItem(
                    key = "remove_feeds_keywords",
                    dependency = "remove_feeds",
                    title = "关键词",
                    multi = true
                ),
                EditItem(
                    key = "remove_feeds_users",
                    dependency = "remove_feeds",
                    title = "用户名",
                    multi = true
                ),
                SwitchItem(
                    key = "remove_official_feeds",
                    title = "屏蔽官方账号帖子"
                ),
                SwitchItem(
                    key = "remove_promotional_feeds",
                    title = "屏蔽带货帖子"
                ),
                SwitchItem(
                    key = "remove_live_feeds",
                    title = "屏蔽直播帖子"
                ),
                SwitchItem(
                    key = "remove_avatar_decoration",
                    title = "屏蔽头像挂饰",
                    summary = "移除活动挂饰"
                ),
                SwitchItem(
                    key = "disable_history_items",
                    title = "关闭浏览历史记录",
                    summary = "不再记录浏览历史"
                ),
                ChannelListItem(
                    key = "modify_channels",
                    title = "管理频道",
                    dialogTitle = "长按频道进行排序",
                    summary = "自定义头部板块",
                ),
                ListItem(
                    key = "default_channel",
                    title = "默认频道",
                    entries = CATEGORY_NAMES.zip(CATEGORY_TYPES.map { it.toString() }).toMap(),
                    default = "1"
                )
            )
        }
        Page.ASSIST -> {
            listOf(
                SwitchItem(
                    key = "remove_download_restrictions",
                    title = "解除下载限制",
                    summary = "解除无法保存/不显示下载按钮的限制"
                ),
                SwitchItem(
                    key = "show_register_escape_time",
                    title = "个人主页显示注册/出黑屋时间",
                    summary = "点击主页时间将复制到剪贴板"
                ),
                SwitchItem(
                    key = "save_audio",
                    title = "保存音频",
                    summary = "分享->保存音频"
                ),
                SwitchItem(
                    key = "copy_item",
                    title = "复制文字",
                    summary = "分享->复制文字"
                ),
                SwitchItem(
                    key = "unlock_illegal_words",
                    title = "解锁屏蔽词",
                    summary = "允许使用被和谐词语(如\uD83D\uDC02\uD83C\uDF7A等)"
                ),
                SwitchItem(
                    key = "unlock_danmaku",
                    title = "解锁高级弹幕特权",
                    summary = "允许使用彩色弹幕、置顶/置底弹幕"
                ),
                SwitchItem(
                    key = "unlock_highlight",
                    title = "解锁视频亮点功能",
                    summary = "可在评论添加时间传送门"
                ),
                SwitchItem(
                    key = "unlock_search_user_limits",
                    title = "解除搜索用户限制",
                    summary = "允许搜索用户的评论/插眼"
                ),
                SwitchItem(
                    key = "unlock_1080p_limit",
                    title = "解除发帖1080P限制",
                    summary = "发帖时支持1080P以上的视频"
                ),
                SwitchItem(
                    key = "unlock_emoji_limit",
                    title = "解除收藏表情数限制",
                    summary = "支持收藏更多的表情"
                ),
                SwitchItem(
                    key = "unlock_send_god_limit",
                    title = "解除神评已送满限制",
                    summary = "已送满的评论会自动点赞"
                ),
                SwitchItem(
                    key = "unlock_video_comment_limit",
                    title = "解除楼中楼视频限制",
                    summary = "楼中楼可以评论视频"
                ),
                SwitchItem(
                    key = "prevent_mistouch",
                    title = "划视频防误触",
                    summary = "左右划视频时不会触发快进快退"
                ),
                SwitchItem(
                    key = "query_danmaku_sender",
                    title = "查询弹幕发送人",
                    summary = "双击弹幕跳转其主页"
                ),
                SwitchItem(
                    key = "enable_female_prompt",
                    title = "开启母虾提示",
                    summary = "评论区的母虾名字显示为粉色"
                ),
                CheckBoxListItem(
                    key = "enable_double_layout_style",
                    title = "开启双列布局",
                    summary = "指定区域使用双列布局",
                    items = listOf(
                        CheckBoxItem(title = "推荐帖子", key = "bds_default_enable_feed_double_layout_list"),
                        CheckBoxItem(title = "用户全部", key = "bds_enable_double_layout_style_moments_profile"),
                        CheckBoxItem(title = "用户帖子", key = "bds_enable_double_layout_style_contributions_profile"),
                        CheckBoxItem(title = "用户评论", key = "bds_enable_double_layout_style_comments_profile"),
                        CheckBoxItem(title = "用户插眼", key = "bds_enable_double_layout_style_wards_profile"),
                        CheckBoxItem(title = "个人收藏", key = "bds_enable_double_layout_style_collection"),
                        CheckBoxItem(title = "标签话题", key = "bds_enable_double_layout_style_hash_tag"),
                        CheckBoxItem(title = "历史记录", key = "bds_enable_double_layout_style_read_history"),
                    )
                ),
                SwitchItem(
                    key = "enable_digg_sound",
                    title = "开启点赞音效",
                    summary = "点赞时附带音效"
                ),
                SwitchItem(
                    key = "enable_old_god_icon_style",
                    title = "开启旧版神评样式",
                    summary = "不带皮皮虾文字"
                ),
                SwitchItem(
                    key = "enable_show_location_label",
                    title = "发帖显示地点标签",
                    summary = "发帖时可添加地点"
                ),
                SwitchItem(
                    key = "use_feed_footer_new_style",
                    title = "帖子页脚使用新布局",
                    summary = "分享、评论、赞、踩"
                ),
                SwitchItem(
                    key = "modify_interaction_style",
                    title = "修改默认交互样式",
                    summary = "影响自动点赞/踩"
                ),
                ListItem(
                    key = "digg_style",
                    dependency = "modify_interaction_style",
                    title = "点赞样式",
                    entries = mapOf(
                        "默认" to "10",
                        "沙雕" to "50",
                        "握草" to "40",
                        "刺激" to "20",
                        "感动" to "30"
                    )
                ),
                ListItem(
                    key = "diss_style",
                    dependency = "modify_interaction_style",
                    title = "点踩样式",
                    entries = mapOf(
                        "默认" to "10",
                        "咳~忒" to "20",
                        "我打" to "30",
                        "鲲" to "40"
                    )
                ),
                SwitchItem(
                    key = "show_exact_comment_time",
                    title = "显示评论具体时间"
                ),
                EditItem(
                    key = "recent_time_format",
                    dependency = "show_exact_comment_time",
                    title = "近日时间格式（倒序，以|分割）",
                    default = "今天HH点mm分ss秒|昨天HH点mm分ss秒|前天HH点mm分ss秒|",
                    multi = true
                ),
                EditItem(
                    key = "exact_time_format",
                    dependency = "show_exact_comment_time",
                    default = "yyyy-MM-dd HH:mm:ss",
                    title = "之前时间格式"
                ),
                SwitchItem(
                    key = "keep_video_play_speed",
                    title = "长按视频保持加速",
                    summary = "再次长按取消加速"
                ),
                ListItem(
                    key = "normal_play_speed",
                    title = "全局视频倍速",
                    entries = mapOf(
                        "0.1X" to "0.1",
                        "0.25X" to "0.25",
                        "0.5X" to "0.5",
                        "0.75X" to "0.75",
                        "1.0X" to "1.0",
                        "1.25X" to "1.25",
                        "1.5X" to "1.5",
                        "1.75X" to "1.75",
                        "2.0X" to "2.0",
                        "2.5X" to "2.5",
                        "3.0X" to "3.0"
                    ),
                    default = "1.0"
                ),
                ListItem(
                    key = "pressed_play_speed",
                    title = "长按视频倍速",
                    entries = mapOf(
                        "0.1X" to "0.1",
                        "0.25X" to "0.25",
                        "0.5X" to "0.5",
                        "0.75X" to "0.75",
                        "1.0X" to "1.0",
                        "1.25X" to "1.25",
                        "1.5X" to "1.5",
                        "1.75X" to "1.75",
                        "2.0X" to "2.0",
                        "2.5X" to "2.5",
                        "3.0X" to "3.0"
                    ),
                    default = "2.0"
                )
            )
        }
        Page.AUTO -> {
            listOf(
                SwitchItem(
                    key = "auto_browse",
                    title = "自动切换下一条"
                ),
                EditItem(
                    key = "auto_browse_frequency",
                    dependency = "auto_browse",
                    title = "频率设置(ms)",
                    default = "3000"
                ),
                SwitchItem(
                    key = "video_delay_handoff",
                    dependency = "auto_browse",
                    title = "视频播放完再切换"
                ),
                SwitchItem(
                    key = "auto_digg",
                    title = "自动点赞",
                    summary = "自动切换时赞成功不显示（实际已赞）"
                ),
                SwitchItem(
                    key = "digg_pause_after_frequent",
                    dependency = "auto_digg",
                    title = "点赞频繁后暂停自动切换"
                ),
                SwitchItem(
                    key = "auto_diss",
                    title = "自动点踩",
                    summary = "自动切换时踩成功不显示（实际已踩）"
                ),
                SwitchItem(
                    key = "diss_pause_after_frequent",
                    dependency = "auto_diss",
                    title = "点踩频繁后暂停自动切换",
                ),
                SwitchItem(
                    key = "auto_comment",
                    title = "自动评论"
                ),
                EditItem(
                    key = "comment_text",
                    dependency = "auto_comment",
                    title = "评论内容"
                ),
                ListItem(
                    key = "auto_comment_condition",
                    dependency = "auto_comment",
                    title = "评论条件",
                    entries = mapOf(
                        "无条件" to "0",
                        "为帖子点赞时" to "1",
                    ),
                    default = "1"
                ),
                SwitchItem(
                    key = "auto_comment_digg",
                    title = "自动评论点赞",
                    summary = "评论时为自己点赞"
                ),
                SwitchItem(
                    key = "auto_ward",
                    title = "自动插眼"
                ),
                ListItem(
                    key = "auto_ward_condition",
                    dependency = "auto_ward",
                    title = "插眼条件",
                    entries = mapOf(
                        "为帖子点赞时" to "0",
                        "为帖子评论时" to "1",
                        "为评论送神时" to "2"
                    )
                ),
                SwitchItem(
                    key = "auto_send_god",
                    dependency = "unlock_send_god_limit",
                    title = "自动为已送满的评论送神"
                ),
                ListItem(
                    key = "auto_send_god_time_limit",
                    dependency = "auto_send_god",
                    title = "评论时间",
                    entries = mapOf(
                        "半小时内" to "1800",
                        "1小时内" to "3600",
                        "6小时内" to "21600",
                        "12小时内" to "43200",
                        "一天内" to "86400",
                        "三天内" to "259200",
                        "七天内" to "604800",
                        "半个月内" to "1296000",
                        "一个月内" to "2592000",
                        "半年内" to "15552000",
                        "一年内" to "31104000"
                    ),
                    default = "86400"
                ),
                SwitchItem(
                    key = "modify_share_counts",
                    title = "自动刷转发次数",
                    summary = "转发一次后转发次数+100（每日上限）"
                )
            )
        }
        Page.MISC -> {
            listOf(
                SwitchItem(
                    key = "hide_icon",
                    title = "隐藏桌面图标",
                    onClick = ::hideIcon
                ),
                ItemDivider,
                SwitchItem(
                    key = "remove_bottom_view",
                    title = "去除底栏"
                ),
                SwitchItem(
                    key = "remove_publish_button",
                    title = "去除发布按钮"
                ),
                SwitchItem(
                    key = "remove_detail_bottom_view",
                    title = "去除评论页底栏"
                ),
                ItemDivider,
                SwitchItem(
                    key = "customize",
                    title = "开启自定义"
                ),
                SwitchItem(
                    key = "modify_message_counts",
                    dependency = "customize",
                    title = "消息99+"
                ),
                SwitchItem(
                    key = "enter_black_house",
                    dependency = "customize",
                    title = "进小黑屋"
                ),
                ListItem(
                    key = "certify_type",
                    dependency = "customize",
                    title = "认证类型",
                    entries = mapOf(
                        "无" to "0",
                        "黄V认证" to "1",
                        "官方认证" to "2",
                        "橙V认证" to "3"
                    )
                ),
                EditItem(
                    key = "certify_desc",
                    dependency = "customize",
                    title = "认证描述"
                ),
                EditItem(
                    key = "username",
                    dependency = "customize",
                    title = "用户名"
                ),
                EditItem(
                    key = "description",
                    dependency = "customize",
                    title = "个性签名"
                ),
                EditItem(
                    key = "like_count",
                    dependency = "customize",
                    title = "获赞"
                ),
                EditItem(
                    key = "followers_count",
                    dependency = "customize",
                    title = "粉丝"
                ),
                EditItem(
                    key = "following_count",
                    dependency = "customize",
                    title = "关注"
                ),
                EditItem(
                    key = "point",
                    dependency = "customize",
                    title = "积分"
                )
            )
        }
        else -> {
            listOf()
        }
    }
}


enum class Page(val index: Int) {
    PURITY(0),
    ASSIST(1),
    AUTO(2),
    MISC(3);

    companion object {
        fun fromIndex(index: Int) = values().find { page -> page.index == index }
    }
}

sealed interface PrefItem

class TextItem(
    val title: String,
    val summary: String? = null,
    val onClick: (Context) -> Unit = { }
) : PrefItem

class SwitchItem(
    val key: String,
    val title: String,
    val summary: String? = null,
    val dependency: String? = null,
    val onClick: (Context, Boolean) -> Unit = { _, _ -> }
) : PrefItem

class EditItem(
    val key: String,
    val title: String,
    val default: String = "",
    val multi: Boolean = false,
    val dependency: String? = null
) : PrefItem

class ListItem(
    val key: String,
    val title: String,
    val entries: Map<String, String>,
    val default: String = entries.values.first(),
    val dependency: String? = null
) : PrefItem

class CheckBoxListItem(
    val key: String,
    val title: String,
    val summary: String? = null,
    val items: List<CheckBoxItem>,
    val dependency: String? = null
) : PrefItem

class ChannelListItem(
    val key: String,
    val title: String,
    val dialogTitle: String = title,
    val summary: String? = null,
    val dependency: String? = null
) : PrefItem

object ItemDivider : PrefItem