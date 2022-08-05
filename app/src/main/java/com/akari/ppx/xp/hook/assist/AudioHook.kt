@file:Suppress("unused", "unchecked_cast", "type_mismatch_warning")

package com.akari.ppx.xp.hook.assist

import android.app.Activity
import android.view.View
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import de.robv.android.xposed.XC_MethodHook
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.absFeedCellClass
import com.akari.ppx.xp.Init.actionType1
import com.akari.ppx.xp.Init.actionType1Class
import com.akari.ppx.xp.Init.actionType2
import com.akari.ppx.xp.Init.actionType2Class
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.downConfig
import com.akari.ppx.xp.Init.downListenerClass
import com.akari.ppx.xp.hook.SwitchHook
import java.io.File
import java.io.FileOutputStream
import java.lang.Enum.valueOf

class AudioHook : SwitchHook("save_audio") {
    override fun onHook() {
        val actionType = valueOf(
            "com.sup.android.i_sharecontroller.model.OptionAction\$OptionActionType".findClass(cl) as Class<Enum<*>>,
            "ACTION_LIVE_WALLPAPER"
        )

        fun XC_MethodHook.MethodHookParam.addOption() {
            result = (result as Array<Any>).plus(actionType)
        }

        actionType1Class!!.hookAfterMethod(actionType1(), absFeedCellClass) { param ->
            param.addOption()
        }
        actionType2Class!!.hookAfterMethod(
            actionType2(),
            absFeedCellClass,
            Boolean::class.java
        ) { param ->
            param.addOption()
        }
        View::class.java.name.hookBeforeMethod(cl, "setTag", Object::class.java) { param ->
            runCatching {
                param.args[0].check(actionType) {
                    param.thisObject.callMethod("getChildAt", 1)?.callMethod("setText", "保存音频")
                }
            }
        }
        var hasSaved = false
        var (name, path) = arrayOfNulls<String>(2)
        "com.sup.android.m_wallpaper.WallPaperService".replaceMethod(
            cl,
            "setLiveWallpaper",
            Activity::class.java,
            "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell",
            MutableMap::class.java
        ) { param ->
            val downloadHelper = "com.sup.android.video.VideoDownloadHelper".findClass(cl)
                .getStaticObjectField("INSTANCE")!!
            val videoModel = "com.sup.android.mi.feed.repo.utils.AbsFeedCellUtil".findClass(cl)
                .getStaticObjectField("Companion")?.callMethod("getVideoDownload", param.args[1])
            videoModel ?: run {
                showStickyToast("视频才能提取音频哦~")
                return@replaceMethod null
            }
            val downLoadConfig = "com.sup.android.video.VideoDownLoadConfig".findClass(cl).new()
            downLoadConfig.callMethod("setItemId", -1L)
            hasSaved = downloadHelper.callMethodAs("hasDownloadVideo", videoModel, null)
            downloadHelper.callMethod(
                "doDownload",
                param.args[0],
                videoModel,
                downLoadConfig,
                null,
                true,
                null
            )
        }
        val downloadTaskClass =
            "com.ss.android.socialbase.downloader.model.DownloadTask".findClass(cl)
        downloadTaskClass.hookBeforeMethod("name", String::class.java) { param ->
            name = param.args[0] as String
        }
        downloadTaskClass.hookBeforeMethod("savePath", String::class.java) { param ->
            path = param.args[0] as String
        }
        downListenerClass!!.hookAfterMethod(
            "onSuccessed",
            "com.ss.android.socialbase.downloader.model.DownloadInfo"
        ) { param ->
            param.thisObject.getObjectField(downConfig())?.callMethodAs<Long>("getItemId")
                ?.checkUnless(-1L) { return@hookAfterMethod }
            val (audioDir, audioPath, videoPath) = listOf(
                "$path/audio/",
                "$path/audio/${System.currentTimeMillis() / 1000}.aac",
                "$path/$name"
            )
            runCatching {
                File(audioDir).checkIf({ !exists() }) { mkdir() }
                FileOutputStream(audioPath).use { fos ->
                    Movie().also { movie ->
                        MovieCreator.build(videoPath).tracks.find { "soun" == it.handler }
                            .let(movie::addTrack)
                    }.let(DefaultMp4Builder()::build).writeContainer(fos.channel)
                }
                hasSaved.check(false) { File(videoPath).delete() }
                showStickyToast(
                    "已保存至DCIM/" +
                            "com.sup.android.business_utils.config.AppConfig".findClass(cl)
                                .callStaticMethodAs<String>("getDownloadDir") +
                            "/audio文件夹"
                )
            }.getOrElse {
                File(audioPath).delete()
                showStickyToast("保存失败，请结束皮皮虾进程后重新启动")
            }
        }
    }
}