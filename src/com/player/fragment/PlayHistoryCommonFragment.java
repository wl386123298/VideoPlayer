package com.player.fragment;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.player.adapter.PlayHistoryAdapter;
import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.tool.DBUtil;

public class PlayHistoryCommonFragment extends SherlockFragment implements OnScrollListener{
	private int rec_data;
	private DbUtils db;
	private List<TvContentModel> tvContent_list,list,afterRefreshList;
	private View view;
	private PlayHistoryAdapter adapter;
	private ListView list_view;
	private int page_index;
	private static final int PAGESIZE = 6;
	private boolean hasMore =true;
	
	public static PlayHistoryCommonFragment newInstance(int value){
		PlayHistoryCommonFragment fragment = new PlayHistoryCommonFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("data", value);
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = DbUtils.create(getSherlockActivity(), DBUtil.DBNAME);
		
		Bundle bundle = getArguments();
		rec_data = bundle.getInt("data");
		
		tvContent_list = getTvContentList(PAGESIZE, page_index);
	}
	
	/**
	 *分页显示 
	 * @param pageSize 一页显示多少行
	 * @param pageIndex 第几页
	 * @return
	 */
	protected List<TvContentModel> getTvContentList(int pageSize,int pageIndex){
		try {
		    list = db.findAll(Selector.from(TvContentModel.class)
		    		.where("play_history", "=", "true")
		    		.and(WhereBuilder.b("enPlay","=" , rec_data == 0?"true":"false"))
		    		.orderBy("id")
		    		.limit(pageSize)
		    		.offset(pageSize * pageIndex));

		} catch (DbException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (tvContent_list == null || tvContent_list.isEmpty()) {
			view = inflater.inflate(R.layout.no_play_history, null);
		}else {
			view= inflater.inflate(R.layout.play_history_list_view, null);
				
			list_view = (ListView)view.findViewById(R.id.play_history_listView);
			adapter = new PlayHistoryAdapter(getSherlockActivity());
			adapter.setData(tvContent_list);
			list_view.setAdapter(adapter);
			list_view.setOnScrollListener(this);
		}
		return view;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			getSherlockActivity().finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 判断是否滚动到底部
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				LogUtils.d("滑到底部");
				page_index++;
				
				if (hasMore) {
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							afterRefreshList = getTvContentList(PAGESIZE,
									page_index);
							if (afterRefreshList != null
									|| !afterRefreshList.isEmpty()) {
								tvContent_list.addAll(afterRefreshList);
								adapter.notifyDataSetChanged();
							}else {
								hasMore = false;
							}
						}
					});
				}else {
					return;
				}
				
			}
		}
	}
}
