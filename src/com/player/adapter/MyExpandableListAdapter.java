package com.player.adapter;

import java.util.List;
import java.util.Map;

import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.model.TvTypeModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandableListAdapter extends android.widget.BaseExpandableListAdapter{
	private Context ctx;
    private List<TvTypeModel> listDataHeader;
    private Map<String, List<TvContentModel>> listDataChild;
	private TextView text;
	private TextView child_text;
	private ImageView icon_img;


    public MyExpandableListAdapter(Context ctx, List<TvTypeModel> listDataHeader,
            Map<String, List<TvContentModel>> listDataChild) {
        this.ctx = ctx;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return listDataChild.isEmpty()||listDataChild == null ?
				null:listDataChild.get(listDataHeader.get(groupPosition).getTv_type_name()).get(childPosition).getTv_name();
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return listDataChild.isEmpty()||listDataChild == null ?0:
		listDataChild.get(listDataHeader.get(groupPosition).getTv_type_name()).size();
	}


	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}
	
	/**
	 * 子元素的布局
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(R.layout.common_list_item, null);
		}
		
		child_text = (TextView)convertView.findViewById(R.id.text);
		child_text.setText(this.getChild(groupPosition, childPosition)+"");
		
		return convertView;
	}


	@Override
	public Object getGroup(int groupPosition) {
		return listDataHeader.isEmpty()||listDataHeader == null ? null:listDataHeader.get(groupPosition).getTv_type_name();
	}

	@Override
	public int getGroupCount() {
		return listDataHeader.isEmpty()||listDataHeader == null ? 0:listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	/**
	 * 父元素的布局
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(R.layout.expandable_group_list_item, null);
		}
		
		text = (TextView)convertView.findViewById(R.id.ListHeader);
		icon_img = (ImageView)convertView.findViewById(R.id.icon_img);
		text.setText(listDataHeader.get(groupPosition).getTv_type_name());
		
		if (isExpanded) {
			icon_img.setBackgroundResource(R.drawable.arrow_down);
		}else {
			icon_img.setBackgroundResource(R.drawable.arrow_right);
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
