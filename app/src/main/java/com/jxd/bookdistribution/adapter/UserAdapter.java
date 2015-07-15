package com.jxd.bookdistribution.adapter;

import java.util.ArrayList;
import java.util.List;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {
	List<Person> mlist = new ArrayList<Person>();
	protected LayoutInflater mInflater = null;
	protected boolean mSelectFlag=false;
	protected List<Integer>  mSelectedList=null;

	public UserAdapter(Context context, List<Person> list , boolean selectFlag ) {
		mInflater = LayoutInflater.from(context);
		mlist = list;
		mSelectFlag = selectFlag;
		mSelectedList = new ArrayList<Integer>();
	}

	public void setUserList(List<Person> list){
		mlist = list;
		this.notifyDataSetChanged();
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
		ViewHolder holder = new ViewHolder();
		final Person item = mlist.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layoutuseritem , null);

			//holder.chkSelected = (CheckBox)convertView.findViewById(R.id.liUserSelect);

			holder.tvusername = (TextView) convertView
					.findViewById(R.id.liUserName);

            holder.tvtelephone = (TextView) convertView
                    .findViewById(R.id.liTelephone);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		final Integer idx = position;
//		holder.chkSelected.setVisibility( mSelectFlag ? View.VISIBLE : View.GONE);
//		holder.chkSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				try{
//				if( mSelectedList == null) mSelectedList = new ArrayList<Integer>();
//				if( isChecked){
//					 if( mSelectedList.contains(idx) == false) mSelectedList.add( idx );
//				}
//				else {
//					if( mSelectedList.contains(idx)) mSelectedList.remove(idx);
//				}
//				item.setSelected(isChecked);
//				}catch(Exception ex){
//				}
//			}
//		});

		//holder.chkSelected.setChecked( item.getSelected() );
		holder.tvusername.setText(item.getUsername());
		holder.tvtelephone.setText(item.getTelephone());

		return convertView;
	}

	class ViewHolder {
		//public CheckBox chkSelected=null;
		public TextView tvusername= null;
		public TextView tvtelephone = null;
	}
}
