package com.akari.ppx.xp.hook.code.purity;

import android.content.Context;

import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.SAVE_VIDEO;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class VideoHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(SAVE_VIDEO, "com.sup.android.video.VideoDownloadHelper", "doDownload", Context.class, "com.sup.android.base.model.VideoModel"
				, "com.sup.android.video.VideoDownLoadConfig", "com.ss.android.socialbase.downloader.depend.IDownloadListener", boolean.class, "kotlin.jvm.functions.Function1", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						Object videoModel = param.args[1];
						FutureTask<String> task = new FutureTask<>(new WebTask(2, (String) callMethod(videoModel, "getUri"), cl));
						new Thread(task).start();
						callMethod(((ArrayList) callMethod(videoModel, "getUrlList")).get(0), "setUrl", task.get());
					}
				});
	}
}
