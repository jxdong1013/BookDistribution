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
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Inventory;
import com.jxd.bookdistribution.util.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

//import com.jxd.contractonlinems.bean.ContractStateEnum;

public class InventoryAdapter extends BaseAdapter {
	List<Inventory> mlist = new ArrayList<Inventory>();
	protected LayoutInflater mInflater = null;
	protected boolean mSelectFlag=false;//
	protected List<Integer>  mSelectedList=null;//

	public InventoryAdapter(Context context, List<Inventory> list, boolean selectFlag) {
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
		if( mlist==null || mlist.size()<1)return convertView;

        if(convertView==null){
            convertView=mInflater.inflate(R.layout.layoutinventoryitem,null);
        }
        Inventory model = mlist.get( position);
        TextView tv = ViewHolderUtil.get(convertView,R.id.tvInventoryName);
        tv.setText( model.getTaskName());

		TextView tvsitename= ViewHolderUtil.get(convertView,R.id.tvsitename);
		tvsitename.setText(model.getSiteName());

		return convertView;
	}
}
