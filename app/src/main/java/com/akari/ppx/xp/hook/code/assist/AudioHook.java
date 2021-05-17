package com.akari.ppx.xp.hook.code.assist;

import android.app.Activity;

import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

import static com.akari.ppx.common.constant.Prefs.SAVE_AUDIO;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.getStaticObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;

public class AudioHook extends SuperbHook {
	private final String OptionActionType = "ACTION_LIVE_WALLPAPER";

	@Override
	protected void onHook(ClassLoader cl) {
		final String[] name = new String[1], path = new String[1];
		final boolean[] savedVideo = new boolean[1];
		if (!XSP.get(SAVE_AUDIO)) return;
		hookMethod("com.sup.superb.feedui.docker.part.m", "d", "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				param.setResult(callStaticMethod(findClass("kotlin.collections.ArraysKt___ArraysJvmKt", cl), "plus", param.getResult(), Enum.valueOf((Class) findClass("com.sup.android.i_sharecontroller.model.OptionAction$OptionActionType", cl), OptionActionType)));
			}
		});
		hookMethod("com.sup.android.detail.util.viewcontroller.b", "a", "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", boolean.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				param.setResult(callStaticMethod(findClass("kotlin.collections.ArraysKt___ArraysJvmKt", cl), "plus", param.getResult(), Enum.valueOf((Class) findClass("com.sup.android.i_sharecontroller.model.OptionAction$OptionActionType", cl), OptionActionType)));
			}
		});
		hookMethod("android.view.View", "setTag", Object.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				try {
					if (Enum.valueOf((Class) findClass("com.sup.android.i_sharecontroller.model.OptionAction$OptionActionType", cl), OptionActionType).equals(param.args[0])) {
						callMethod(callMethod(param.thisObject, "getChildAt", 1), "setText", "保存音频");
					}
				} catch (Error ignored) {
				}
			}
		});
		hookMethod("com.sup.android.m_wallpaper.WallPaperService", "setLiveWallpaper", Activity.class, "com.sup.android.mi.feed.repo.bean.cell.AbsFeedCell", Map.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				Object videoDownloadHelper = getStaticObjectField(findClass("com.sup.android.video.VideoDownloadHelper", cl), "INSTANCE");
				Object videoModel = callMethod(getStaticObjectField(findClass("com.sup.android.mi.feed.repo.utils.AbsFeedCellUtil", cl), "Companion"), "getVideoDownload", param.args[1]);
				if (videoModel != null) {
					Object videoDownLoadConfig = newInstance(findClass("com.sup.android.video.VideoDownLoadConfig", cl));
					callMethod(videoDownLoadConfig, "setItemId", -1L);
					savedVideo[0] = (Boolean) callMethod(videoDownloadHelper, "hasDownloadVideo", videoModel, null);
					callMethod(videoDownloadHelper, "doDownload", param.args[0], videoModel, videoDownLoadConfig, null, true, null);
				} else {
					Utils.showSystemToastXP(cl, "视频才能提取音频哦~");
				}
				return null;
			}
		});
		hookMethod("com.ss.android.socialbase.downloader.model.DownloadTask", "name", String.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				name[0] = (String) param.args[0];
			}
		});
		hookMethod("com.ss.android.socialbase.downloader.model.DownloadTask", "savePath", String.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				path[0] = (String) param.args[0];
			}
		});
		hookMethod("com.sup.android.video.VideoDownloadHelper$d", "onSuccessed", "com.ss.android.socialbase.downloader.model.DownloadInfo", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (-1L != (long) callMethod(getObjectField(param.thisObject, "f"), "getItemId"))
					return;
				try {
					String audioDir = path[0] + "/audio/";
					File file = new File(audioDir);
					if (!file.exists())
						file.mkdir();
					String videoPath = path[0] + "/" + name[0], audioPath = audioDir + System.currentTimeMillis() / 1000 + ".aac";
					Track track = null;
					for (Track t : MovieCreator.build(videoPath).getTracks())
						if ("soun".equals(t.getHandler()))
							track = t;
					Movie movie = new Movie();
					movie.addTrack(track);
					Container out = new DefaultMp4Builder().build(movie);
					FileOutputStream fos = new FileOutputStream(new File(audioPath));
					out.writeContainer(fos.getChannel());
					fos.close();
					if (!savedVideo[0])
						new File(videoPath).delete();
					Utils.showSystemToastXP(cl, String.format("已保存至DCIM/%s/audio文件夹", callStaticMethod(findClass("com.sup.android.business_utils.config.AppConfig", cl), "getDownloadDir")));
				} catch (Throwable t) {
					XposedBridge.log(t);
				}
			}
		});
	}
}
