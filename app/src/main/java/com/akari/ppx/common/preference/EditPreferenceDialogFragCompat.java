package com.akari.ppx.common.preference;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.akari.ppx.common.constant.Const;
import com.akari.ppx.common.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class EditPreferenceDialogFragCompat extends PreferenceDialogFragmentCompat {
	private static final String SAVE_STATE_TEXT = "EditPreferenceDialogFragCompat.text";
	private EditText editText;
	private CharSequence text;
	private int whichClicked;

	public EditPreferenceDialogFragCompat() {
		whichClicked = DialogInterface.BUTTON_NEUTRAL;
	}

	public static EditPreferenceDialogFragCompat newInstance(String key) {
		EditPreferenceDialogFragCompat fragment = new EditPreferenceDialogFragCompat();
		Bundle bundle = new Bundle(1);
		bundle.putString("key", key);
		fragment.setArguments(bundle);
		return fragment;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		text = savedInstanceState == null ? getEditPreference().getText() : savedInstanceState.getCharSequence(SAVE_STATE_TEXT);
	}

	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putCharSequence(SAVE_STATE_TEXT, text);
	}

	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		editText = view.findViewById(android.R.id.edit);
		if (editText != null) {
			editText.requestFocus();
			editText.setText(text);
			editText.setSelection(editText.getText().length());
		}
	}

	protected boolean needInputMethod() {
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		whichClicked = which;
	}

	@Override
	public void onDismiss(@NotNull DialogInterface dialog) {
		super.onDismiss(dialog);
		onDialogClosed();
	}

	@Override
	public void onDialogClosed(boolean positiveResult) {
	}

	private EditPreference getEditPreference() {
		return (EditPreference) getPreference();
	}

	protected void onDialogClosed() {
		if (whichClicked == DialogInterface.BUTTON_NEUTRAL) {
			return;
		}
		EditPreference pref = getEditPreference();
		String value;
		if (whichClicked == DialogInterface.BUTTON_POSITIVE) {
			value = editText.getText().toString();
		} else {
			value = pref.getDefaultValue();
		}
		if (pref.callChangeListener(value)) {
			pref.setText(value);
			pref.setSummary(pref instanceof NeutralEditPreference && ((NeutralEditPreference) pref).isArray() ? Utils.checkTextValid(value) : "".equals(value) ? "" : Const.NOW.concat(value));
		}
	}
}
