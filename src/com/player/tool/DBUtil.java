package com.player.tool;

import java.util.List;

import net.tsz.afinal.FinalDb;

import android.content.Context;

/**
 * 数据库工具类
 * @author Administrator
 *
 */
public class DBUtil{
	public final static String DBNAME = "tv_list";//数据库名称
	
	private Context context;
	private List<?> list;
	public DBUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * 获取类型
	 * @param clazz
	 * @return
	 */
	public List<?> getAllTvType(Class<?> clazz) {
		FinalDb db = FinalDb.create(context);
		list = db.findAll(clazz);
		
		return list;
	}
	
}
