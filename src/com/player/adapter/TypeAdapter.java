package com.player.adapter;

import java.util.List;

import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.model.TvTypeModel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TypeAdapter extends BaseAdapter{
	private List<TvTypeModel> type_list;
	private List<TvContentModel> content_list;
	private Context context;
	private TextView text;
	private TvTypeModel model;
	private TvContentModel tvContentModel;
	public TypeAdapter(Context context , List<TvTypeModel> type_list 
			, List<TvContentModel> content_list ) {
		this.type_list  = type_list;
		this.content_list = content_list;
		this.context = context;
		}
	
	@Override
	public int getCount() {
//		if (type_list!=null) {
//			return type_list.size();
//		}else {
//			return content_list.size();
//		}
		
		return type_list != null ? type_list.size():content_list.size();
		
	}

	@Override
	public Object getItem(int position) {
		return type_list != null?type_list.get(position):content_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
		}
		text = (TextView)convertView.findViewById(R.id.text);
		
		if (type_list == null) {
			tvContentModel = (TvContentModel) content_list.get(position);
			text.setText(tvContentModel.getTv_name());
		}else {
			model = (TvTypeModel) type_list.get(position);
			text.setText(model.getTv_type_name());
/*		if (cur_pos == position) {
				convertView.setBackgroundColor(context.getResources().getColor(R.color.grey));
				text.setTextColor(Color.WHITE);
			}*/
		}

		return convertView;
	}
	
}
