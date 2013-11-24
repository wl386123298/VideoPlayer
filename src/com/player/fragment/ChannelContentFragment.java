package com.player.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.player.adapter.TypeAdapter;
import com.player.main.MainPlayerActivity;
import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.tool.DBUtil;

public class ChannelContentFragment extends SherlockFragment implements OnItemClickListener{
		private ListView listView;
		private String type = "1";
		private FinalDb db;
		private List<TvContentModel> list;
		private TypeAdapter adpter;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		savedInstanceState = getArguments();
		type = savedInstanceState.getString("type_id");
		//System.out.println(type);

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.channel_content, null);
			
			listView =(ListView) v.findViewById(R.id.channel_content_listView);		
			db = FinalDb.create(getActivity(),DBUtil.DBNAME);
			//list = db.findAllByWhere(TvContentModel.class, "tv_type = '" + type +"'", "tv_name");
			
			list = new ArrayList<TvContentModel>();

			List<DbModel> list_model= db.findDbModelListBySQL("select * , count(DISTINCT tv_name) as count from tvContent where tv_type = '"
					+ type+"' GROUP BY tv_name ORDER BY id ");
			
			for (DbModel dbModel : list_model) {
				TvContentModel model = new TvContentModel();
				model.setId(dbModel.getInt("id"));
				model.setTv_name(dbModel.getString("tv_name"));
				model.setTv_type(dbModel.getString("tv_type"));
				model.setTv_url(dbModel.getString("tv_url"));
				list.add(model);
			}
			
			if (list != null || !list.isEmpty()) {
				adpter = new TypeAdapter(getActivity(), null,list);
				listView.setAdapter(adpter);
				listView.setOnItemClickListener(this);
			}
			
			return v;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			String tv_url = list.get(position).getTv_url();
			Intent intent = new Intent(getActivity(), MainPlayerActivity.class);
			intent.putExtra("tv_url", tv_url);
			startActivity(intent);
			
		}

}
