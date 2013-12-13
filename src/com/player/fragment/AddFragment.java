package com.player.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.player.main.R;
import com.player.model.TvContentModel;
import com.player.model.TvTypeModel;
import com.player.tool.DBUtil;

public class AddFragment extends SherlockFragment implements OnClickListener{
	private EditText tv_name;
	private EditText tv_url;
	private AutoCompleteTextView tv_type;
	private DbUtils db;
	private List<DbModel> model;
	private List<TvTypeModel> tv_type_list;
	private String[] city;
	private Button save_btn;
	private String tvName,tvUrl,tvType;
	private TvContentModel tvContentModel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSherlockActivity().getSupportActionBar().setTitle("添加");
		db = DbUtils.create(getActivity(), DBUtil.DBNAME);
		try {
			model = db.findDbModelAll(new SqlInfo("Select tv_type_name from tvType order by tv_type"));
		} catch (DbException e) {
			e.printStackTrace();
		}
		city = new String[model.size()];
		
		for (int i = 0; i < city.length; i++) {
			DbModel dbModel = model.get(i);
			city[i] = dbModel.getString("tv_type_name");			
		}
	}
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.add, null);
		tv_name = (EditText)v.findViewById(R.id.tv_name);
		tv_url = (EditText)v.findViewById(R.id.tv_url);
		tv_type = (AutoCompleteTextView)v.findViewById(R.id.tv_type);
		save_btn = (Button)v.findViewById(R.id.save);
		save_btn.setOnClickListener(this);
		
		tv_type.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, city));	
		
		return v;
	}

	@Override
	public void onClick(View v) {
		System.out.println("click****");
		tvName = tv_name.getText().toString();
		tvUrl = tv_url.getText().toString();
		tvType = tv_type.getText().toString();
		tvContentModel = new TvContentModel();
		//String tv_type_data = "";
		if (tvName.length()==0|| tvUrl.length()==0|| tv_type.length() ==0) {
			Toast.makeText(getActivity(), "所填字段不能为空！", Toast.LENGTH_SHORT).show();
		}else{
			tvType = getType(tvType);
			tvContentModel.setTv_name(tvName);
			tvContentModel.setTv_url(tvUrl);
			tvContentModel.setTv_type(tvType);
			try {
				db.save(tvContentModel);
			} catch (DbException e) {
				e.printStackTrace();
			}
			Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
			tv_name.setText("");
			tv_url.setText("");
			tv_type.setText("");
		}
	}
	
	/**
	 * 获取tv_type
	 * @param tv_type_name 
	 * @return
	 */
	protected String getType(String tv_type_name) {
		String tv_type = "";
		try {
			tv_type_list = db.findAll(Selector.from(TvTypeModel.class).where("tv_type_name", "=", tv_type_name));
		} catch (DbException e) {
			e.printStackTrace();
		}
		for (TvTypeModel model : tv_type_list) {
			tv_type = model.getTv_type();
		}
		return tv_type;
	}
}
