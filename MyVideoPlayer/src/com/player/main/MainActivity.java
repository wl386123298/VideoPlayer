package com.player.main;

import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.player.model.TvContentModel;
import com.player.model.TvTypeModel;
import com.player.tool.CommonUtil;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	private Button clik_btn;
	private String tv_content_str , tv_type_str;
	private byte[] tv_content_btyes , tv_type_bytes;
	private TvContentModel tv_model ;
	private TvTypeModel type_model;
	private FinalDb db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		db = FinalDb.create(this,"tv_list");
		List<TvContentModel> tvContentList = db.findAll(TvContentModel.class);
		List<TvTypeModel> tvModelList = db.findAll(TvTypeModel.class);
		tv_content_btyes = new CommonUtil(getApplicationContext()).readJson(R.raw.tv_content);
		tv_type_bytes = new CommonUtil(getApplicationContext()).readJson(R.raw.tv_type);
		
		try {
			tv_content_str = new String(tv_content_btyes, "UTF-8");
			tv_type_str = new String(tv_type_bytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		if (tvContentList == null && tvModelList == null || tvContentList.isEmpty()&&tvModelList.isEmpty()) {
			MyTask task = new MyTask();
			task.execute(tv_content_str,tv_type_str);
		}
		
		clik_btn = (Button)findViewById(R.id.clickBtn);
		clik_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MainPlayerActivity.class));
			}
		});
		
	}
	
	class MyTask extends AsyncTask<String, integer, String>{
		@Override
		protected void onProgressUpdate(integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected String doInBackground(String... params) {
			String tvContentStr = params[0];
			String tvTypeStr = params[1];
			
			JSONObject obj,obj1;
			try {
				obj = new JSONObject(tvContentStr);
				JSONArray array = obj.getJSONArray("RECORDS");
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					tv_model  = new TvContentModel();
					tv_model.setTv_name(item.getString("tv_name"));
					tv_model.setTv_url(item.getString("tv_url"));
					tv_model.setTv_type(item.getString("tv_type"));
					db.save(tv_model);
				}
				
				obj1 = new JSONObject(tvTypeStr);
				JSONArray array1 = obj1.getJSONArray("RECORDS");
				for (int i = 0; i < array1.length(); i++) {
					JSONObject item = array1.getJSONObject(i);
					type_model  = new TvTypeModel();
					type_model.setTv_type(item.getString("tv_type"));
					
					type_model.setTv_type_name(item.getString("tv_type_name"));
					db.save(type_model);
				}
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
		
	}

}
