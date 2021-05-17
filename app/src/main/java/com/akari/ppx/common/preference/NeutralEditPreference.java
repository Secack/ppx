package com.akari.ppx.common.preference;

import android.content.Context;
import android.util.AttributeSet;

public class NeutralEditPreference extends EditPreference {
	public NeutralButton neutralButton;
	private boolean isArray;

	public NeutralEditPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NeutralButton getNeutralButton() {
		return neutralButton;
	}

	public void setNeutralButton(NeutralButton neutralButton) {
		this.neutralButton = neutralButton;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setIsArray(boolean isArray) {
		this.isArray = isArray;
	}
}
