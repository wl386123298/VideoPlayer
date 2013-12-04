package com.player.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;

import com.actionbarsherlock.app.SherlockFragment;

import com.player.adapter.MyExpandableListAdapter;
import com.player.main.MainPlayerActivity;
import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.model.TvTypeModel;
import com.player.tool.DBUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class MainFragment extends SherlockFragment implements OnGroupExpandListener , OnGroupCollapseListener,OnChildClickListener,OnGroupClickListener{
	private static MainFragment fragment = null;
	private ExpandableListView listView;
	private FinalDb db;
	private List<TvTypeModel> tvTypeList;
	private List<TvContentModel> tvContentList;
	private Map<String, List<TvContentModel>> listDataChild;
	private MyExpandableListAdapter adapter;	
	public static MainFragment newInstance(){
		if (fragment == null) {
			fragment = new MainFragment();
		}
		return fragment;		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View  v = inflater.inflate(R.layout.common_main, null);
		
		db = FinalDb.create(getActivity(), DBUtil.DBNAME);
		tvTypeList = db.findAllByWhere(TvTypeModel.class, null, "tv_type");
		tvContentList = new ArrayList<TvContentModel>();
		listDataChild = new HashMap<String, List<TvContentModel>>();
		
		listView = (ExpandableListView)v.findViewById(R.id.lvExpd);
		adapter = new MyExpandableListAdapter(getActivity(),tvTypeList , listDataChild);
		listView.setAdapter(adapter);
		
		int groupCount = listView.getCount();
		for (int i = 0; i < groupCount; i++) {
			listView.collapseGroup(i);
		}
		
		listView.setOnGroupClickListener(this);
		listView.setOnGroupCollapseListener(this);
		listView.setOnGroupExpandListener(this);
		listView.setOnChildClickListener(this);
		
		return v;
	}

	
	public void setELVGroup(ExpandableListView parent,int groupPosition){
		if(parent.isGroupExpanded(groupPosition)){
			listView.collapseGroup(groupPosition); 
		}else{
			AsyncGetChannelTask task = new AsyncGetChannelTask();
			task.execute(String.valueOf(groupPosition));
			/*listView.expandGroup(groupPosition);  */
			listView.setSelectedGroup(groupPosition);
		}
		
	}
	
	//子元素单击响应
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Intent intent = new Intent();
		intent.putExtra("tv_url", listDataChild.get(tvTypeList.get(groupPosition).getTv_type_name()).get(childPosition).getTv_url());
		intent.setClass(getActivity(), MainPlayerActivity.class);
		startActivity(intent);
		return true;
	}

	
	//组折叠响应
	@Override
	public void onGroupCollapse(int groupPosition) {	
	
	}

	//组展开响应
	@Override
	public void onGroupExpand(int groupPosition) {
		for(int i = 0;i < adapter.getGroupCount();i ++){
            if(i != groupPosition ){
                 listView.collapseGroup(i);
            }
		}
	}

	
	//父元素单击相应
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		AsyncGetChannelTask task = new AsyncGetChannelTask();
		if (tvContentList.isEmpty()) {
			task.execute(String.valueOf(groupPosition));
		}else {
			//task.execute(String.valueOf(groupPosition));
			setELVGroup(parent, groupPosition);
		}
		
		return true;
	}
	
	private class AsyncGetChannelTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			try {
				String type = tvTypeList.get(Integer.parseInt(params[0])).getTv_type();
				List<DbModel> list_model= db.findDbModelListBySQL("select * , count(DISTINCT tv_name) as count from tvContent where tv_type = '"
						+ type +"' GROUP BY tv_name ORDER BY id ");
				
				if (!tvContentList.isEmpty()) {
					tvContentList.clear();
				}
				
				for (DbModel dbModel : list_model) {
					TvContentModel model = new TvContentModel();
					model.setId(dbModel.getInt("id"));
					model.setTv_name(dbModel.getString("tv_name"));
					model.setTv_type(dbModel.getString("tv_type"));
					model.setTv_url(dbModel.getString("tv_url"));
					
					tvContentList.add(model);
				}
				
				for (TvTypeModel model : tvTypeList) {
					listDataChild.put(model.getTv_type_name(), tvContentList);
				}
				return params[0];
			} catch (Exception e) {
				e.printStackTrace();
				return "-1";
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (!"-1".equals(result)) {
				int tmpGroupPosition = Integer.parseInt(result);
				listView.expandGroup(tmpGroupPosition);  
                // 设置被选中的group置于顶端  
				listView.setSelectedGroup(tmpGroupPosition);
				adapter.notifyDataSetChanged();
			}
		}
	}
}
