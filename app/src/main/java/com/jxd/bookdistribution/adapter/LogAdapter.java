package com.jxd.bookdistribution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Log;
import com.jxd.bookdistribution.bean.Person;
import com.jxd.bookdistribution.util.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends BaseAdapter {
	List<Log> mlist = new ArrayList<>();
	protected LayoutInflater mInflater = null;
	protected boolean mSelectFlag=false;
	protected List<Integer>  mSelectedList=null;

	public LogAdapter(Context context, List<Log> list, boolean selectFlag) {
		mInflater = LayoutInflater.from(context);
		mlist = list;
		mSelectFlag = selectFlag;
		mSelectedList = new ArrayList<Integer>();
	}

	public void SetSelectedFlag( boolean flag){
		mSelectFlag= flag;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mlist ==null? 0: mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist ==null?null: mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Log item = mlist.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layoutlogitem, null);
		}

		TextView tvoperatetype= ViewHolderUtil.get(convertView,R.id.tvOperatetype);
		tvoperatetype.setText(item.getOperatetype());
		TextView tvusername = ViewHolderUtil.get(convertView,R.id.tvUsername);
		tvusername.setText(item.getUsername());

		return convertView;
	}
}
