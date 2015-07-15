package com.jxd.bookdistribution.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Constant;
import com.jxd.bookdistribution.util.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

//import com.jxd.contractonlinems.bean.ContractStateEnum;

public class InventoryListAdapter extends BaseAdapter {
	List<Book> mlist = new ArrayList<Book>();
	protected LayoutInflater mInflater = null;
	protected boolean mSelectFlag=false;//
	protected List<Integer>  mSelectedList=null;//

	public InventoryListAdapter(Context context, List<Book> list, boolean selectFlag) {
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

        if(convertView==null){
            convertView=mInflater.inflate(R.layout.layoutinventorylistitem,null);
        }
        Book model = mlist.get( position);
        TextView tv = ViewHolderUtil.get(convertView,R.id.tvbookname);
        tv.setText( model.getBookName());

		TextView tvauthor=ViewHolderUtil.get(convertView,R.id.tvauthor);
		tvauthor.setText(model.getAuthor());

		TextView tvpublish=ViewHolderUtil.get(convertView,R.id.tvpublish);
		tvpublish.setText(model.getPublish());

		TextView tvbarcode= ViewHolderUtil.get(convertView,R.id.tvbarcode);
		tvbarcode.setText(model.getBarcode());

		TextView tvstatus = ViewHolderUtil.get(convertView,R.id.tvstatus);
		//Integer status= model.getStatus();
		//String statusName="";
		//if( status == Constant.BookIn){
		//	statusName="在库";
		//}else if( status==Constant.BookOut){
		//	statusName="出库";
		//}
		if(model.getSelected()==true){
			tvstatus.setBackgroundResource(R.drawable.tag_selected);
		}

		//tvstatus.setText( statusName );

		return convertView;
	}
}
