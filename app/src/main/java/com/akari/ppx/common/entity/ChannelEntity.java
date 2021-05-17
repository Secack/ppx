package com.akari.ppx.common.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChannelEntity implements Serializable {
	@SerializedName("name")
	private String name;

	public String getPureName() {
		int index = name.indexOf('|');
		return index != -1 ? name.substring(0, index) : name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
