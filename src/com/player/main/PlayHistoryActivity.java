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
import com.player.fragment.PlayHistoryCommonFragment;
import com.player.main.R;
import com.viewpagerindicator.TabPageIndicator;


public class PlayHistoryActivity extends SherlockFragmentActivity{

	private ViewPager viewPager;
	private List<String> titles;
	private TabPageIndicator indicator;
	private FragmentPagerAdapter adapter;
	private ActionBar actionBar;
	private float startX;
	private float startY;
	
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
	
	
	
	protected void initActionBar() {
		 actionBar = getSupportActionBar();
		 actionBar.setDisplayShowHomeEnabled(false);
		 actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
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

	/*@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_SCROLL) {
			CommonUtil util  = new CommonUtil(getApplicationContext());
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//记录开始坐标
				startX = event.getX();
				startY = event.getY();
				//Toast.makeText(this, "初始坐标为："+"初始坐标："+startX+"::"+startY, Toast.LENGTH_SHORT).show();
				
				break;
			case MotionEvent.ACTION_UP:
				
				if (startX - event.getX() > 100) {
					//mVideoView.setVolume(leftVolume, rightVolume)
					System.out.println("左滑！");
				}else if (event.getX()-startX > 100) {
					System.out.println("右滑！");
				}else if (startY - event.getY() >100 ) {
					
				}else if (event.getY()- startY > 100 ) {
					//System.out.println("下滑！");
				
				}
				break;
				
			default:
				break;
			}
		}
		return false;
	}*/

}
