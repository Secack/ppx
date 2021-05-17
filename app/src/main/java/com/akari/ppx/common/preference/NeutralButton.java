package com.akari.ppx.common.preference;

import com.akari.ppx.common.constant.Const;

public class NeutralButton {
	private final CharSequence text;
	private final OnClickListener listener;

	public NeutralButton(final OnClickListener listener) {
		this(Const.TEST, listener);
	}

	public NeutralButton(CharSequence text, final OnClickListener listener) {
		this.text = text;
		this.listener = listener;
	}

	public CharSequence getText() {
		return text;
	}

	public void onClick(String text) {
		if (listener != null)
			listener.onClick(text);
	}

	public interface OnClickListener {
		void onClick(String text);
	}
}
