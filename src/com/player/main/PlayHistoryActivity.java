package com.player.main;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.player.fragment.PlayHistoryCommonFragment;
import com.player.main.R;
import com.viewpagerindicator.TabPageIndicator;


public class PlayHistoryActivity extends SherlockFragmentActivity{

	private ViewPager viewPager;
	private List<String> titles;
	private TabPageIndicator indicator;
	private FragmentPagerAdapter adapter;
	private ActionBar actionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_history);
		initActionBar();
		titles = new ArrayList<String>();
		titles.add("正常播放");
		titles.add("非正常播放");
		
		viewPager = (ViewPager)findViewById(R.id.play_history_viewpager);
		viewPager.setOffscreenPageLimit(0);
		indicator = (TabPageIndicator)findViewById(R.id.play_history_indicator);
		
		adapter = new MyAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		
		//viewPager.setOnTouchListener(this);
		indicator.setViewPager(viewPager);
	
	}
	
	
	/**
	 * 初始化ActionBar
	 */
	protected void initActionBar() {
		 actionBar = getSupportActionBar();
		 actionBar.setDisplayShowHomeEnabled(false);
		 actionBar.setDisplayHomeAsUpEnabled(true);
		 actionBar.setTitle("观看历史");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		
		return true;
	}
	
	/**
	 * ViewPager的适配器
	 * @author wl
	 *
	 */
	class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return PlayHistoryCommonFragment.newInstance(position%titles.size());
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position % titles.size());
		}
		
		@Override
		public int getCount() {
			return titles.size();
		}
		
	}
}
