package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.common.entity.ChannelEntity;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Const.CATEGORY_LIST_NAME;
import static com.akari.ppx.common.constant.Const.CATEGORY_LIST_TYPE;
import static com.akari.ppx.common.constant.Prefs.DEFAULT_CHANNEL;
import static com.akari.ppx.common.constant.Prefs.DIY_CATEGORY_LIST;
import static com.akari.ppx.common.constant.Prefs.MY_CHANNEL;
import static com.akari.ppx.common.utils.ChannelUtils.getDataList;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.newInstance;

public class CategoryHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final List<ChannelEntity> targetList = getDataList(XSP.gets(MY_CHANNEL));
		final String defaultChannel = XSP.gets(DEFAULT_CHANNEL, "推荐");
		if (!XSP.get(DIY_CATEGORY_LIST)) return;
		hookMethod("com.sup.superb.feedui.bean.CategoryListModel", "setCategoryItems", List.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				List beforeList = (List) param.args[0], afterList = new ArrayList();
				//beforeList.add(createChannel("Test", "https://baidu.com", "https://baidu.com/channel.jpg", 174, 116));
				List<String> nameList = new ArrayList<>();
				for (Object item : beforeList)
					nameList.add((String) callMethod(item, "getListName"));
				if (targetList == null)
					afterList = beforeList;
				else {
					for (ChannelEntity t : targetList) {
						int i = -1;
						for (String s : nameList) {
							i++;
							if (t.getName().contains(s))
								break;
						}
						afterList.add(beforeList.get(i));
					}
				}
				param.args[0] = afterList;
			}

			private Object createChannel(String channelName, String webUrl, String imageUrl, int width, int height) {
				Object categoryItem = newInstance(findClass("com.sup.superb.feedui.bean.CategoryItem", cl));
				callMethod(categoryItem, "setListName", channelName);
				callMethod(categoryItem, "setPrimaryListId", 233);
				callMethod(categoryItem, "setEventName", channelName);
				callMethod(categoryItem, "setViewType", 1);
				Object webViewData = newInstance(findClass("com.sup.superb.feedui.bean.WebViewData", cl));
				callMethod(webViewData, "setUrl", webUrl);
				callMethod(webViewData, "setImmersive", Boolean.TRUE);
				callMethod(categoryItem, "setWebViewData", webViewData);
				Object categoryItemStyle = newInstance(findClass("com.sup.superb.feedui.bean.CategoryItemStyle", cl));
				callMethod(categoryItemStyle, "setCategoryName", channelName);
				callMethod(categoryItemStyle, "setSelectedStyle", 3);
				callMethod(categoryItemStyle, "setUnselectedStyle", 3);
				Object imageModel = newInstance(findClass("com.sup.android.base.model.ImageModel", cl));
				callMethod(imageModel, "setWidth", width);
				callMethod(imageModel, "setHeight", height);
				ArrayList urlList = new ArrayList();
				urlList.add(newInstance(findClass("com.sup.android.base.model.ImageUrlModel", cl), imageUrl));
				callMethod(imageModel, "setUrlList", urlList);
				callMethod(categoryItemStyle, "setSelectedImageInfo", imageModel);
				callMethod(categoryItem, "setCategoryStyle", categoryItemStyle);
				return categoryItem;
			}

		});
		hookMethod("com.sup.superb.feedui.bean.CategoryListModel", "getDefaultChannel", new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				int i = -1;
				for (String name : CATEGORY_LIST_NAME) {
					i++;
					if (name.contains(defaultChannel))
						break;
				}
				return CATEGORY_LIST_TYPE[i];
			}
		});
		//1.7.3 FeedTabFragment$a
		//s1.7.4 FeedTabFragment$CategoryPagerAdapter
		hookMethod("com.sup.superb.feedui.view.FeedTabFragment$CategoryPagerAdapter", "c", int.class, new XC_MethodHook() {
			private int i = 0;

			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				i++;
				if ((i & 1) != 0) {
					param.setResult(-1);
				}
			}
		});

	}
}
