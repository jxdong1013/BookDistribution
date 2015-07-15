package com.jxd.bookdistribution.adapter;

import java.util.List;
import com.jxd.bookdistribution.R;
//import com.hxcy.whzyxt.Entity.RoomEntity;
//import com.hxcy.whzyxt.Util.IinterfaceUtil;
import com.jxd.bookdistribution.bean.Site;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteAdapter extends BaseAdapter {
	List<Site> mSites=null;
	LayoutInflater mInflater=null;
	Context mContext=null;

	
	public SiteAdapter(Context context , List<Site> sites ){
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
		ViewHolder holder=null;
		if (mSites ==null) return null;
		if( convView ==null){
			convView= mInflater.inflate(R.layout.layoutsiteitem , null);
			holder = new ViewHolder();
			//holder.IvPic = (ImageView)convView.findViewById(R.id.roomitempic);
			//holder.IvState = (ImageView)convView.findViewById(R.id.roomitemstate);
			holder.Tvname =(TextView)convView.findViewById(R.id.tvSiteName);
			holder.TvTelephone =(TextView)convView.findViewById(R.id.tvSiteTelephone);
			//holder.TvSn =(TextView)convView.findViewById(R.id.roomitemsn);
			//holder.TvRegion = (TextView)convView.findViewById(R.id.roomitemregion);
			//holder.IbLocation  = (ImageButton)convView.findViewById(R.id.roomitemlocation);
			convView.setTag(holder);
		}else {
			holder= (ViewHolder)convView.getTag();
		} 
		Site entity = mSites.get(position);
		//holder.IvPic.setOnClickListener(new ListViewClickListener(entity));
		//holder.IbLocation .setOnClickListener(new ListViewClickListener(entity));
		holder.Tvname.setText( entity.getSiteName() );
		holder.TvTelephone.setText(entity.getTelephone());
		//String regionname = entity.getRegion()==null? "":entity.getRegion().getName();
		//holder.TvRegion.setText(regionname);
		//holder.TvSn.setText( entity.getSn());
		//if(entity.getInactive_collectors() > 0 ){
		//	holder.IvState.setImageResource(R.drawable.red);
		//}
		//else {
		//	holder.IvState.setImageResource(R.drawable.green);
		//}
		//if( entity.getImage() !=null){
		//	holder.IvPic.setImageBitmap(entity.getImage());
		//}
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
	
	class ViewHolder{
		//public ImageView IvPic=null;
		//public ImageView IvState=null
	    public TextView Tvname=null;
		public TextView TvTelephone=null;
	    //public	TextView TvRegion=null;
	    //public ImageButton IbLocation=null;
	}
}
