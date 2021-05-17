package com.akari.ppx.common.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

public class EditPreference extends EditTextPreference {
	private String defaultValue;

	public EditPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		Object result = super.onGetDefaultValue(a, index);
		defaultValue = (String) result;
		return result;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(Object value) {
		super.setDefaultValue(value);
		defaultValue = (String) value;
	}
}
