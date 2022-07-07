@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook
import kotlin.math.abs

class ImageHook : SwitchHook("save_image") {
    override fun onHook() {
        "com.sup.android.m_gallery.NewGalleryActivity".replaceMethod(cl, "onDownload") { param ->
            CoroutineScope(Dispatchers.Main).launch {
                with(param.thisObject as Activity) {
                    val index = callMethod("getVpGallery")?.callMethodAs<Int>("getCurrentItem")!!
                    val current = callMethodAs<ArrayList<*>>("getImages")[index]
                    val thumbs = callMethodAs<ArrayList<*>?>("getThumbs")
                    var useCdn = false
                    var url = ""

                    fun isThumb(img1: Any, img2: Any) =
                        abs(img1.callMethodAs<Int>("getWidth") - img2.callMethodAs<Int>("getWidth")) < 5
                                && abs(
                            img1.callMethodAs<Int>("getHeight") - img2.callMethodAs<Int>(
                                "getHeight"
                            )
                        ) < 5

                    if (thumbs == null || !isThumb(current, thumbs[index])) {
                        withContext(Dispatchers.IO) {
                            url =
                                "https://sf1-ttcdn-tos.pstatp.com/obj/${current.callMethod("getUri")}"
                            val req = "okhttp3.Request\$Builder".findClass(cl).new()
                                .callMethod("url", url)?.callMethod("build")
                            val resp = "okhttp3.OkHttpClient".findClass(cl).new()
                                .callMethod("newCall", req)?.callMethod("execute")
                            useCdn = resp?.callMethod("body")?.callMethod("contentType")
                                .toString() != "image/webp"
                        }
                    }
                    val urlList = current.callMethodAs<ArrayList<*>>("getUrlList")
                    val isGif = current.callMethodAs<Boolean>("isGif")
                    if (!useCdn) {
                        url = urlList[0].callMethodAs("getUrl")
                        url = url.substring(0, url.lastIndexOf('.')) + if (isGif) ".gif" else ".png"
                    }
                    urlList[0].callMethod("setUrl", url)
                    callMethod("downloadImage", urlList, isGif)
                }
            }
        }
    }
}