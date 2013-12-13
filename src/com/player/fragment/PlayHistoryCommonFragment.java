package com.player.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.player.adapter.PlayHistoryAdapter;
import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.tool.DBUtil;

public class PlayHistoryCommonFragment extends SherlockFragment{
	private int rec_data = -1;
	private DbUtils db;
	private List<Object>     tvContent_list;
	private View view;
	private PlayHistoryAdapter adapter;
	private ListView list_view;
	static PlayHistoryCommonFragment newInstance(int value){
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
		try {
		    tvContent_list = db.findAll(Selector.from(TvContentModel.class).where("play_history", "=", "true"));
		} catch (DbException e) {
			e.printStackTrace();
		}
		
		Bundle bundle = getArguments();
		rec_data = bundle.getInt("data");
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
		}
		System.out.println("***********" + rec_data);
		
		return view;
	}
}
