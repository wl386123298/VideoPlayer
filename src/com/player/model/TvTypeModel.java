package com.player.model;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="tvType")
public class TvTypeModel {
	private int id;
	private String tv_type,tv_icon,tv_type_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTv_type() {
		return tv_type;
	}
	public void setTv_type(String tv_type) {
		this.tv_type = tv_type;
	}
	public String getTv_icon() {
		return tv_icon;
	}
	public void setTv_icon(String tv_icon) {
		this.tv_icon = tv_icon;
	}
	public String getTv_type_name() {
		return tv_type_name;
	}
	public void setTv_type_name(String tv_type_name) {
		this.tv_type_name = tv_type_name;
	}
	
	
	
}
