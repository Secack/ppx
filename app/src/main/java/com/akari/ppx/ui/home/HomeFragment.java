package com.akari.ppx.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.akari.ppx.R;
import com.akari.ppx.common.constant.Const;
import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.preference.EditPreference;
import com.akari.ppx.common.preference.EditPreferenceDialogFragCompat;
import com.akari.ppx.common.preference.NeutralButton;
import com.akari.ppx.common.preference.NeutralEditPreference;
import com.akari.ppx.common.preference.NeutralEditPreferenceDialogFragCompat;
import com.akari.ppx.common.utils.ModuleUtils;
import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.ui.channel.ChannelActivity;

import java.util.Objects;

public class HomeFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
	private final HomeActivity context;

	public HomeFragment(HomeActivity context) {
		this.context = context;
	}

	@SuppressLint("ApplySharedPref")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (ModuleUtils.isModuleEnabled_Xposed() || ModuleUtils.isModuleEnabled_Taichi(context))
			return;
		ActionBar actionBar = ((HomeActivity) requireActivity()).getSupportActionBar();
		Objects.requireNonNull(actionBar).setTitle(String.format("%s [%s]", actionBar.getTitle(), "未激活"));
	}

	@Override
	public void onPause() {
		super.onPause();
		Utils.setPreferenceWorldWritable(context);
	}

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.settings);
		NeutralButton.OnClickListener listener = text -> Utils.showToast(getActivity(), Utils.checkTextValid(text));
		setSummaryPlusButton(Prefs.REMOVE_COMMENT_KEYWORDS, listener, true);
		setSummaryPlusButton(Prefs.REMOVE_COMMENT_USERS, listener, true);
		findPreference(Prefs.DIY_CATEGORY_LIST).setOnPreferenceChangeListener(this);
		findPreference(Prefs.REMOVE_BOTTOM_VIEW).setOnPreferenceChangeListener(this);
		findPreference(Prefs.DONATE).setOnPreferenceClickListener(this);
		findPreference(Prefs.DONATEEDIT).setOnPreferenceClickListener(this);
		findPreference(Prefs.JOIN_QQ_GROUP).setOnPreferenceClickListener(this);
		findPreference(Prefs.HIDE_LAUNCHER_ICON).setOnPreferenceChangeListener(this);
		findPreference(Prefs.SOURCE_CODE).setOnPreferenceClickListener(this);
		findPreference(Prefs.SOURCE_CODE_EDIT).setOnPreferenceClickListener(this);
		listener = text -> Utils.showToast(getActivity(), Utils.ts2date(System.currentTimeMillis(), text, true));
		setSummaryPlusButton(Prefs.TODAY_COMMENT_TIME_FORMAT, listener, false);
		setSummaryPlusButton(Prefs.EXACT_COMMENT_TIME_FORMAT, listener, false);
		setSummary(Prefs.AUTO_BROWSE_FREQUENCY);
		setSummary(Prefs.AUTO_COMMENT_TEXT);
		setSummary(Prefs.USER_NAME);
		setSummary(Prefs.CERTIFY_DESC);
		setSummary(Prefs.DESCRIPTION);
		listener = text -> Utils.showToast(getActivity(), Utils.checkLongValid(text));
		setSummaryPlusButton(Prefs.LIKE_COUNT, listener, false);
		setSummaryPlusButton(Prefs.FOLLOWERS_COUNT, listener, false);
		setSummaryPlusButton(Prefs.FOLLOWING_COUNT, listener, false);
		setSummaryPlusButton(Prefs.POINT, listener, false);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		switch (Prefs.fromString(preference.getKey())) {
			case DONATE:
				Utils.donateByAlipay(context,Const.ALIPAY_URI);
				break;
			case DONATEEDIT:
				Utils.donateByAlipay(context,Const.ALIPAY_EDIT_URI);
				break;
			case JOIN_QQ_GROUP:
				Utils.joinQQGroup(context);
				break;
			case SOURCE_CODE:
				Utils.showGitPage(context,Const.GITHUB_URI);
				break;
			case SOURCE_CODE_EDIT:
				Utils.showGitPage(context,Const.GITHUB_URI_EDIT);
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean checked = (Boolean) newValue;
		switch (Prefs.fromString(preference.getKey())) {
			case DIY_CATEGORY_LIST:
				if (checked)
					context.startActivity(new Intent(context, ChannelActivity.class));
				return true;
			case REMOVE_BOTTOM_VIEW: {
				if (checked)
					((SwitchPreference) findPreference(Prefs.REMOVE_PUBLISH_BUTTON)).setChecked(false);
				return true;
			}
			case HIDE_LAUNCHER_ICON: {
				Utils.hideIcon(context, checked);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDisplayPreferenceDialog(Preference preference) {
		boolean handled = false;
		if (preference instanceof EditPreference) {
			String key = preference.getKey();
			DialogFragment dialogFragment;
			if (preference instanceof NeutralEditPreference) {
				dialogFragment = NeutralEditPreferenceDialogFragCompat.newInstance(key, ((NeutralEditPreference) preference).getNeutralButton());
			} else dialogFragment = EditPreferenceDialogFragCompat.newInstance(key);
			dialogFragment.setTargetFragment(this, 0);
			dialogFragment.show(context.getSupportFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
			handled = true;
		}
		if (!handled)
			super.onDisplayPreferenceDialog(preference);
	}

	@NonNull
	private Preference findPreference(Prefs prefs) {
		return Objects.requireNonNull(findPreference(prefs.getKey()));
	}

	private void setSummary(Prefs prefs) throws NullPointerException {
		EditPreference pref = (EditPreference) findPreference(prefs);
		String text = pref.getText();
		pref.setSummary(pref instanceof NeutralEditPreference && ((NeutralEditPreference) pref).isArray() ? Utils.checkTextValid(text) : "".equals(text) ? "" : Const.NOW.concat(text));
	}

	private void setSummaryPlusButton(Prefs prefs, NeutralButton.OnClickListener onClickListener, boolean isArray) {
		EditPreference pref = (EditPreference) findPreference(prefs);
		if (pref instanceof NeutralEditPreference) {
			((NeutralEditPreference) pref).setIsArray(isArray);
			((NeutralEditPreference) pref).setNeutralButton(new NeutralButton(onClickListener));
		}
		setSummary(prefs);
	}
}
