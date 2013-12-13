package com.player.adapter;

import java.util.List;

import com.player.main.R;
import com.player.model.TvContentModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayHistoryAdapter extends BaseAdapter{
	private List<?> list;
	private Context context;
	public PlayHistoryAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return list.isEmpty()|| list == null ?0:list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.isEmpty()||list==null?null:list.get(position );
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setData(List<?> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		TvContentModel model = (TvContentModel) list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.play_history_list_item, null);
			holder.play_history_tv_name = (TextView)convertView.findViewById(R.id.play_history_tv_name);
			holder.play_history_tv_url = (TextView)convertView.findViewById(R.id.play_history_tv_url);
			holder.play_history_enPlay = (TextView)convertView.findViewById(R.id.play_history_enPlay);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.play_history_tv_name.setText(model.getTv_name());
		holder.play_history_tv_url.setText(model.getTv_url());
		if ("false".equals(model.getEnPlay())) {
			//holder.play_history_enPlay.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.text_view_red_style));
			holder.play_history_enPlay.setBackgroundResource(R.drawable.text_view_red_style);
		}else {
			//holder.play_history_enPlay.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.text_view_green_style));
			holder.play_history_enPlay.setBackgroundResource(R.drawable.text_view_green_style);
		}
		holder.play_history_enPlay.setText("false".equals(model.getEnPlay())?"N":"Y");
		
		return convertView;
	}
	
	class ViewHolder{
		TextView play_history_tv_name,play_history_tv_url,play_history_enPlay;
	}
}
