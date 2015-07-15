package com.jxd.bookdistribution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.util.ViewHolderUtil;

import org.w3c.dom.Text;

import java.util.List;

//import com.hxcy.whzyxt.Entity.RoomEntity;
//import com.hxcy.whzyxt.Util.IinterfaceUtil;

public class ExportSiteAdapter extends BaseAdapter {
	List<Site> mSites=null;
	LayoutInflater mInflater=null;
	Context mContext=null;

	public ExportSiteAdapter(Context context, List<Site> sites){
		mSites = sites;
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount(){
		return mSites==null? 0:mSites.size();
	}

	public Object getItem(int arg0) {
		return mSites==null?null:mSites.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View convView , ViewGroup arg2) {
		if (mSites ==null) return null;
		if( convView ==null){
			convView= mInflater.inflate(R.layout.layoutexportitem , null);
		}
		Site entity = mSites.get(position);

        RadioButton rdbSelected = ViewHolderUtil.get(convView, R.id.rdbSelected);
        TextView tvName = ViewHolderUtil.get(convView,R.id.tvSiteName);

        rdbSelected.setChecked( entity.getSelected());
        tvName.setText(entity.getSiteName());

		return convView;
	}
	
//	public void SetLocationInterface(IinterfaceUtil.ILocationInterface location ){
//		mLocationInterface = location;
//	}
	
//	class ListViewClickListener implements OnClickListener{
//		RoomEntity mRoom=null;
//		ListViewClickListener(RoomEntity room){
//			mRoom = room;
//		}
//		public void onClick(View v) {
//			if(v.getId() ==R.id.roomitemlocation){
//
//			}else if(v.getId() == R.id.roomitempic ){
//				if(mLocationInterface!=null){
//					mLocationInterface.Location(mRoom);
//				}
//			}
//		}
//	}

}
