package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.LogAdapter;
import com.jxd.bookdistribution.bean.Log;
import com.jxd.bookdistribution.bean.Person;
import com.jxd.bookdistribution.thread.LogListThread;
import com.jxd.bookdistribution.thread.UserListThread;
import com.jxd.bookdistribution.util.ToastUtil;
import com.jxd.bookdistribution.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogListActivity extends Activity {
    TextView mtvTitle;
    XListView mlv;
    LogAdapter mAdapter=null;
    List<Log> mList=null;
    int mPageIdx=0;
    boolean mIsRefresh=false;
    LogListHandler mHandler;
    LogXListViewListener mListener=null;
    String mStartTime="";
    String mEndTime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loglist);

        Intent intent = getIntent();
        if( intent.hasExtra("starttime")) {
            mStartTime = intent.getStringExtra("starttime");
        }
        if( intent.hasExtra("endtime")) {
            mEndTime = intent.getStringExtra("endtime");
        }

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mtvTitle.setText("操作日志结果");

        mHandler=new LogListHandler();

        mList= new ArrayList<>();
        mAdapter =new LogAdapter(LogListActivity.this,mList,false);
        mlv=(XListView)this.findViewById(R.id.lvLog);
        mListener=new LogXListViewListener(mStartTime,mEndTime);
        mlv.setXListViewListener(mListener);
        mlv.setPullLoadEnable(true);
        mlv.setPullRefreshEnable(true);
        mlv.setAdapter(mAdapter);

        AsynLoadLogs();
    }

    protected void AsynLoadLogs() {
        mIsRefresh = false;
        mPageIdx = -1;
        if(mList!=null) mList.clear();
        mlv.startLoadMore();
    }

    private void onLoad() {
        try {
            mlv.stopRefresh();
            mlv.stopLoadMore();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",
                    Locale.getDefault());
            String datestr = format.format(new Date());
            mlv.setRefreshTime(datestr);
        } catch (Exception e) {
            ToastUtil.Show("onload出错了!");
        }
    }

    private class LogListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == LogListThread.MSG_Loaded ) {
                    List<Log> temp = (List<Log>) msg.obj;
                    if (temp == null) {
                        mPageIdx--;
                        onLoad();
                        //mTvCount.setText(mContractList.size() + "/" + msg.arg1);
                        return;
                    }
                    mList.addAll(temp);
                    //mTvCount.setText(mContractList.size() + "/" + msg.arg1);

                    onLoad();
                    mAdapter.notifyDataSetChanged();
                } else if (msg.what == LogListThread.MSG_Refresh) {
                    List<Log> temp = (List<Log>) msg.obj;
                    if (temp == null) {
                        //mTvCount.setText("0/" + msg.arg1);
                        mPageIdx--;
                        onLoad();
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                    mList.addAll(temp);
                    //mTvCount.setText(mContractList.size() + "/" + msg.arg1);

                    // mAdapter = new ContractAdapter(ContractListActivity.this,
                    // mContractList , mDeleteMenuFlag );
                    //mchkAllSelect.setChecked(false);
                    // mlvContracts.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    onLoad();
                } else if (msg.what == LogListThread.MSG_Error) {
                    onLoad();
                }

            } catch (Exception e) {
                ToastUtil.Show("handlemessage出错了!");
            }
        }
    }


    class LogXListViewListener implements XListView.IXListViewListener {
        String starttime="";
        String endtime="";

        public LogXListViewListener( String starttime , String endtime ){
            this.starttime=starttime;
            this.endtime = endtime;

        }

        public void onRefresh() {
            try {

                mIsRefresh = true;
                if (mList != null)
                    mList.clear();
                mAdapter.notifyDataSetChanged();
                mPageIdx = 0;

                AsyncQuery();
            } catch (Exception ex) {
                ToastUtil.Show(ex.getMessage());
            }
        }

        protected void AsyncQuery() {
            mIsRefresh=false;
            LogListThread thread = new LogListThread( LogListActivity.this ,
                    mPageIdx , mHandler , mIsRefresh,  starttime,endtime );
            thread.start();
        }

        public void onLoadMore() {
            try {
                mPageIdx++;
                mIsRefresh = false;

                AsyncQuery();

            } catch (Exception ex) {

            }
        }
    }
}
