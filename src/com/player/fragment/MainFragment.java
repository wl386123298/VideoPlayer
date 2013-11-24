package com.player.fragment;

import java.util.List;

import net.tsz.afinal.FinalDb;

import com.actionbarsherlock.app.SherlockFragment;
import com.player.adapter.TypeAdapter;
import com.player.main.R;
import com.player.model.TvTypeModel;
import com.player.tool.DBUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainFragment extends SherlockFragment implements OnItemClickListener{

	private ListView channel_listView;
	private FinalDb db;
	private List<TvTypeModel> list;
	private TypeAdapter adpter;
	private int cur_pos = 0;// 当前显示的一行

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View  v = inflater.inflate(R.layout.common_main, null);
		channel_listView = (ListView)v.findViewById(R.id.changel_listView);
		
		db = FinalDb.create(getActivity(),DBUtil.DBNAME);
		list = db.findAllByWhere(TvTypeModel.class,"tv_type");
		if (list != null || !list.isEmpty()) {
			//adpter = new TypeAdapter(getActivity(), list,null);
			channel_listView.setAdapter(new MyAdapter());
			channel_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
		
		
		channel_listView.setOnItemClickListener(this);
		
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		cur_pos  = position;
		for(int i=0;i<parent.getCount();i++){
            View v=parent.getChildAt(parent.getCount()-1-i);
            if (position == i) {
                v.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
            	v.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
		
		
		//System.out.println("***" +list.get(position).getTv_type());
		Fragment fragment = new ChannelContentFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("type_id", list.get(position).getTv_type());
		//bundle.putInt("current_position", cur_pos);
		
		fragment.setArguments(bundle);
		
		ft.replace(R.id.channel_layout, fragment).commit();
	}
	
	protected class MyAdapter extends BaseAdapter{

		private TextView text;
		private TvTypeModel model;
		private LayoutInflater inflater;
		
		public MyAdapter() {
			inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			model = (TvTypeModel) list.get(position);
			convertView = inflater.inflate(R.layout.list_item, null);
			text = (TextView)convertView.findViewById(R.id.text);
			text.setText(model.getTv_type_name());

/*			if (cur_pos == position) {
				convertView.setBackgroundColor(getResources().getColor(R.color.grey));
				text.setTextColor(Color.WHITE);
			}
			*/
			
			return convertView;
		}
		
		
	}
}
