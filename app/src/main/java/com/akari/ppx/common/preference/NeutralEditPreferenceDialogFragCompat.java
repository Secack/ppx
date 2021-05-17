package com.akari.ppx.common.preference;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

public class NeutralEditPreferenceDialogFragCompat extends EditPreferenceDialogFragCompat {
	private EditText editText;
	private NeutralButton neutralButton;

	public static EditPreferenceDialogFragCompat newInstance(String key, NeutralButton neutralButton) {
		NeutralEditPreferenceDialogFragCompat fragment = new NeutralEditPreferenceDialogFragCompat();
		Bundle bundle = new Bundle(1);
		bundle.putString("key", key);
		fragment.setArguments(bundle);
		fragment.neutralButton = neutralButton;
		return fragment;
	}

	@Override
	public @NonNull
	Dialog onCreateDialog(Bundle savedInstanceState) {
		androidx.appcompat.app.AlertDialog dialog = (androidx.appcompat.app.AlertDialog) super.onCreateDialog(savedInstanceState);
		if (neutralButton == null)
			neutralButton = new NeutralButton(null);
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, neutralButton.getText(), this);
		return dialog;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		editText = view.findViewById(android.R.id.edit);
	}

	private void setAutoDismiss(DialogInterface dialog, boolean autoDismiss) {
		try {
			Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, autoDismiss);
		} catch (Exception ignored) {
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		boolean isNeutral = which == DialogInterface.BUTTON_NEUTRAL;
		setAutoDismiss(dialog, !isNeutral);
		if (isNeutral) {
			neutralButton.onClick(editText.getText().toString());
		}
	}
}
