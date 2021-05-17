package com.akari.ppx.xp.hook.code.purity;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.newInstance;

public class WebTask implements Callable<String> {
	private final int type;
	private final String param;
	private final ClassLoader cl;

	public WebTask(int type, String param, ClassLoader cl) {
		this.type = type;
		this.param = param;
		this.cl = cl;
	}

	@Override
	public String call() {
		switch (type) {
			case 1:
				Object request = callMethod(callMethod(newInstance(findClass("okhttp3.Request$Builder", cl)), "url", param), "build");
				Object response = callMethod(callMethod(newInstance(findClass("okhttp3.OkHttpClient", cl)), "newCall", request), "execute");
				return callMethod(callMethod(response, "body"), "contentType").toString();
			case 2:
				long ts = System.currentTimeMillis();
				String url = "https://i.snssdk.com/video/play/1/bds/" + ts + "/"
						+ callStaticMethod(findClass("com.bytedance.common.utility.DigestUtils", cl), "md5Hex", "ts" + ts + "userbdsversion1video" + param
						+ "vtypemp4f425df23905d4ee38685e276072faa0c")
						+ "/mp4/" + param;
				Object doGet = callMethod(newInstance(findClass("com.bytedance.apm.net.DefaultHttpServiceImpl", cl)), "doGet", url, null);
				String ret = new String((byte[]) callMethod(doGet, "b"));
				try {
					String mainUrl;
					JSONObject object = new JSONObject(ret).getJSONObject("video_info").getJSONObject("data").getJSONObject("video_list");
					try {
						mainUrl = object.getJSONObject("video_2").getString("main_url");
					} catch (Exception e) {
						mainUrl = object.getJSONObject("video_1").getString("main_url");
					}
					return new String(Base64.decode(mainUrl, 0));
				} catch (JSONException ignored) {
				}
		}
		return null;

	}
}
