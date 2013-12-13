package com.player.main;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.player.adapter.LeftMenuAdapter;
import com.player.fragment.AddFragment;
import com.player.fragment.MainFragment;
import com.player.fragment.PlayHistoryFragment;
import com.player.model.TvContentModel;
import com.player.model.TvTypeModel;
import com.player.tool.CommonUtil;
import com.player.tool.DBUtil;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class MainActivity extends SherlockFragmentActivity implements DrawerListener , OnItemClickListener{
	private String tv_content_str , tv_type_str;
	private byte[] tv_content_btyes , tv_type_bytes;
	private TvContentModel tv_model ;
	private TvTypeModel type_model;
	private DbUtils db;
	private List<TvContentModel> tvContentList;
	private List<TvTypeModel> tvModelList;
	private DrawerLayout drawer;
	private ListView left_listView;
	private LeftMenuAdapter adapter;
	private ActionBar mActionBar;
	private SherlockActionBarDrawerToggle mDrawerToggle;
	private String[] menu_data ={"频道","我的最爱","观看历史","添加","设置"};
	private int[] menu_icon  = {R.drawable.channel,R.drawable.love,R.drawable.play_history,R.drawable.add,R.drawable.setting};
	private int[] pre_icon = {R.drawable.pre_channel,R.drawable.pre_love,R.drawable.pre_play_history,R.drawable.pre_add,R.drawable.pre_setting};
	private Fragment fragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initActionBar();
		
		drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		left_listView = (ListView)findViewById(R.id.left_drawer_listView);		
		mDrawerToggle = new SherlockActionBarDrawerToggle(this,drawer, R.drawable.ic_drawer_light, R.string.drawer_open, R.string.drawer_close);
		drawer.setDrawerListener(this);
		drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		adapter = new LeftMenuAdapter(this);
		adapter.setData(menu_data);
		adapter.setIcon(menu_icon);
		adapter.setPressIcon(pre_icon);
		left_listView.setAdapter(adapter);
		left_listView.setOnItemClickListener(this);
		
		db = DbUtils.create(this,DBUtil.DBNAME);
		db.configDebug(true);
		db.configAllowTransaction(true);
		try {
			tvContentList = db.findAll(Selector.from(TvContentModel.class));
			tvModelList = db.findAll(Selector.from(TvTypeModel.class));
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
		}else {
			fragment = MainFragment.newInstance();		
			replaceFragment(R.id.main, fragment, false);
		}
	}
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {	
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	/**
	 * 异步初始化后台数据
	 * @author Administrator
	 *
	 */
	class MyTask extends AsyncTask<String, integer, String>{
		
		@Override
		protected void onProgressUpdate(integer... values) {
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
					try {
						db.save(tv_model);
					} catch (DbException e) {
						e.printStackTrace();
						return null;
					}
				}
			
				
				obj1 = new JSONObject(tvTypeStr);
				JSONArray array1 = obj1.getJSONArray("RECORDS");
				for (int i = 0; i < array1.length(); i++) {
					JSONObject item = array1.getJSONObject(i);
					type_model  = new TvTypeModel();
					type_model.setTv_type(item.getString("tv_type"));
					
					type_model.setTv_type_name(item.getString("tv_type_name"));
					
					try {
						db.save(type_model);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}

			return "success";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result !=null&&"success".equals(result)) {
				fragment = MainFragment.newInstance();		
				replaceFragment(R.id.main, fragment, false);
			}
		}
	}

	/**
	 * 初始化ActionBar
	 */
	 public void initActionBar(){
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标 
		mActionBar.setDisplayUseLogoEnabled(false); 
		mActionBar.setIcon(getResources().getDrawable(R.drawable.tv));
		//mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setHomeButtonEnabled(true);//决定左上角的图标是否可以点击。没有向左的小图标
		
	 }

	@Override
	public void onDrawerClosed(View drawerView) {
		mDrawerToggle.onDrawerClosed(drawerView);
	}

	@Override
	public void onDrawerOpened(View drawerView) {
		mDrawerToggle.onDrawerOpened(drawerView);
		
	}

	@Override
	public void onDrawerSlide(View drawerView, float slideOffset) {
		mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
	}

	@Override
	public void onDrawerStateChanged(int newState) {
		mDrawerToggle.onDrawerStateChanged(newState);
	}
	
	/**
	 * 关闭左边的侧滑
	 */
	protected void closeLeftMenu() {
		if (drawer !=null) {
			drawer.closeDrawer(left_listView);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.selectItem(position);
		adapter.notifyDataSetInvalidated();
		switch (position) {
		case 0:
			Fragment main_fraFragment = MainFragment.newInstance();
			replaceFragment(R.id.main, main_fraFragment, false);
			closeLeftMenu();
			break;
		case 1:
			break;
		case 2:
			Fragment history_fragment = new PlayHistoryFragment();
			this.replaceFragment(R.id.main, history_fragment, false);
			this.closeLeftMenu();
			break;
		case 3:
			Fragment add_fragment = new AddFragment();
			replaceFragment(R.id.main, add_fragment, false);
			closeLeftMenu();
			break;
		case 4:
			break;
		default:
			break;
		}
	}
	
	/**
	 * 往布局文件中添加fragment
	 * @param layout_id 布局文件的ID
	 * @param fragment 要添加的fragment
	 * @param isAddToBackStack 是否添加到isAddToBackStack
	 */
	protected void replaceFragment(int layout_id, Fragment fragment,boolean isAddToBackStack){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(layout_id, fragment);
		if (isAddToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	
	
}
