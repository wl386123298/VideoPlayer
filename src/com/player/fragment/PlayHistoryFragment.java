package com.player.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.player.adapter.PlayHistoryAdapter;
import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.tool.DBUtil;

public class PlayHistoryFragment extends SherlockFragment{
	private ListView list_view;
	private DbUtils db;
	private List<TvContentModel> tvContent_list;
	private PlayHistoryAdapter adapter;
	private ActionBar mActionBar;
	private ViewPager viewPager;
	private ImageView imageView;
	private TextView enPlayText;
	private TextView disPlayText;
	private View view;
	private int bmpW;
	private List<Fragment> views;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view= inflater.inflate(R.layout.play_history, null);
			
		InitViewPage();
		InitImageView();
		InitImageView();

		return view;
	}
	
	protected void InitViewPage(){
		viewPager = (ViewPager)view.findViewById(R.id.play_history_view_pager);
		views = new ArrayList<Fragment>();
		Fragment enableFragment = PlayHistoryCommonFragment.newInstance(0);
		Fragment disableFragment = PlayHistoryCommonFragment.newInstance(1);
		views.add(enableFragment);
		views.add(disableFragment);
		
	
		viewPager.setAdapter(new MyViewPagerAdapter(getSherlockActivity().getSupportFragmentManager(),views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	protected void InitTextView(){
		enPlayText = (TextView)view.findViewById(R.id.play_history_enable);
		disPlayText = (TextView)view.findViewById(R.id.play_history_disable);
		
		enPlayText.setOnClickListener(new MyOnClickListener(0));
		disPlayText.setOnClickListener(new MyOnClickListener(1));
	}
	
	protected void InitImageView() {
		imageView = (ImageView)view.findViewById(R.id.play_history_underline);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_sliding_icon).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
		
	}
	
	/** 
	 *     
	 * 头标点击监听 3 */
	private class MyOnClickListener implements OnClickListener{
        private int index=0;
        public MyOnClickListener(int i){
        	index=i;
        }
		public void onClick(View v) {
			viewPager.setCurrentItem(index);			
		}
		
	}
	
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{
		private List<Fragment> mListViews;
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public MyViewPagerAdapter (FragmentManager fm, List<Fragment> mListViews) {
			super(fm);
			this.mListViews = mListViews;
		}
		

		@Override
		public Fragment getItem(int arg0) {
			return mListViews.get(arg0);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}
		
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener{

    	int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			Toast.makeText(getSherlockActivity(), "您选择了"+ viewPager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();
		}
    	
    }

}
