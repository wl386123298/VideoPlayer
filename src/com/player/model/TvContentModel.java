package com.player.model;

import com.lidroid.xutils.db.annotation.Table;

@Table(name= "tvContent")
public class TvContentModel {
	
	private int id;
	private String tv_name,tv_url,tv_type,enPlay;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTv_name() {
		return tv_name;
	}
	
	public void setTv_name(String tv_name) {
		this.tv_name = tv_name;
	}
	
	
	public String getTv_url() {
		return tv_url;
	}
	public void setTv_url(String tv_url) {
		this.tv_url = tv_url;
	}
	
	public String getTv_type() {
		return tv_type;
	}
	
	public void setTv_type(String tv_type) {
		this.tv_type = tv_type;
	}
	
	public String getEnPlay() {
		return enPlay;
	}
	
	public void setEnPlay(String enPlay) {
		this.enPlay = enPlay;
	}
}
