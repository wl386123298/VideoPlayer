package com.player.adapter;

import com.player.main.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftMenuAdapter extends BaseAdapter{
	private String[] contents;
	private int[] icons;
	private Context context;
	private int curpos = 0;
	private TextView text;
	private ImageView icon;
	private int[] pre_icons;
	public LeftMenuAdapter(Context context) {
		this.context = context;
	}
	
	public void setData(String[] contents) {
		this.contents = contents;
	}
	
	public void setIcon(int[] icons) {
		this.icons = icons;
	}

	@Override
	public int getCount() {
		return contents.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contents[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void selectItem(int curpos) {
		this.curpos = curpos;
	}
	
	public void setPressIcon(int[] pre_icons) {
		this.pre_icons = pre_icons;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.expandable_group_list_item, null);
		}
		text = (TextView)convertView.findViewById(R.id.ListHeader);
		icon = (ImageView)convertView.findViewById(R.id.icon_img);
		
		text.setText(contents[position]);
		icon.setImageDrawable(context.getResources().getDrawable(icons[position]));

		convertView.setPadding(15, 15, 15, 15);
		
		if (curpos == position) {
			convertView.setBackgroundColor(Color.GRAY);
			text.setTextColor(Color.WHITE);
			icon.setImageDrawable(context.getResources().getDrawable(pre_icons[position]));
		}else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
			text.setTextColor(Color.GRAY);
			icon.setImageDrawable(context.getResources().getDrawable(icons[position]));
		}
		
		return convertView;
	}

}
