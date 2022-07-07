@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import android.content.Context
import android.util.Base64
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class VideoHook : SwitchHook("save_video") {
    override fun onHook() {
        "com.sup.android.video.VideoDownloadHelper".findClass(cl).apply {
            replaceMethod(
                "doDownload",
                Context::class.java,
                "com.sup.android.base.model.VideoModel",
                "com.sup.android.video.VideoDownLoadConfig",
                "com.ss.android.socialbase.downloader.depend.IDownloadListener",
                Boolean::class.java,
                "kotlin.jvm.functions.Function1"
            ) { param ->
                CoroutineScope(Dispatchers.Main).launch {
                    val videoModel = param.args[1]
                    var resp: String

                    fun String.get() = String(
                        "com.bytedance.apm.net.DefaultHttpServiceImpl".findClass(cl).new()
                            .callMethod("doGet", this, null)?.callMethodAs<ByteArray>("b")!!
                    )

                    withContext(Dispatchers.IO) {
                        var uri = videoModel.callMethodAs<String>("getUri")
                        uri.check("") {
                            uri = "https://h5.pipix.com/bds/webapi/item/detail/?item_id=${
                                param.args[2].callMethodAs<Long>("getItemId")
                            }".get()
                                .fromJsonElement()["data"]["item"]["origin_video_id"].asString
                        }
                        val ts = System.currentTimeMillis()
                        resp = "https://i.snssdk.com/video/play/1/bds/$ts/${
                            "com.bytedance.common.utility.DigestUtils".findClass(cl)
                                .callStaticMethod(
                                    "md5Hex",
                                    "ts${ts}userbdsversion1video${uri}vtypemp4f425df23905d4ee38685e276072faa0c"
                                )
                        }/mp4/$uri".get()
                    }

                    fun JsonElement.getMaxQualityVideo(i: Int = 5): String =
                        runCatching {
                            this["video_$i"]["main_url"].asString
                        }.getOrElse { getMaxQualityVideo(i - 1) }

                    String(
                        Base64.decode(
                            resp.fromJsonElement()["video_info"]["data"]["video_list"].getMaxQualityVideo(),
                            0
                        )
                    ).let { url ->
                        videoModel.callMethodAs<ArrayList<*>>("getUrlList").forEach {
                            it.callMethod("setUrl", url)
                        }
                    }
                    param.invokeOriginalMethod()
                }
            }
            replaceMethod("isEnableDownloadGodVideo") { false }
        }
    }
}