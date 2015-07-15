package com.jxd.bookdistribution.adapter;

import com.jxd.bookdistribution.R;
//import com.hxcy.whzyxt.Entity.GridViewEntity;
import com.jxd.bookdistribution.app.BookApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	//int statusHeight = 0;
	Context mContext;
	public GridViewAdapter( Context context ){
        mContext= context;
	}
	
	public int getCount() {
		return BookApplication.mApp.LeftMenuList.size();
	}

	public Object getItem(int position) {
		return BookApplication.mApp.LeftMenuList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {		
		convertView = View1(convertView,position);
		convertView.getBackground().setAlpha(90);
		return convertView;
	}
	
	View View1(View convertView , int position)
	{
		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(R.layout.layoutgridviewitem , null);
		ImageView iv =  (ImageView)convertView.findViewById(R.id.gv_imageView1);
		TextView tv = (TextView)convertView.findViewById(R.id.gv_textView1);
		iv.setImageResource( BookApplication.mApp.LeftMenuList.get(position).getImage());
		tv.setText( BookApplication.mApp.LeftMenuList.get(position).getMenuName());
		return convertView;		
	}
	
//	class ViewHolder
//	{
//		public ImageView iv;
//		public TextView tv;
//	}

//	public void Exchange(int position1 ,int position2){
////		System.out.println(position1 + "--" + position2);
//		Object endObject = getItem(position2);
//		Object startObject = getItem(position1);
//		GlobalVariable.GridViewLists[position1] = (GridViewEntity)endObject ;
//		GlobalVariable.GridViewLists[position2] = (GridViewEntity)startObject;
//		notifyDataSetChanged();
//	}
}
