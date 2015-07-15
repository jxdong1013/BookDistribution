package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.UserAdapter;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Person;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.thread.BookListThread;
import com.jxd.bookdistribution.thread.UserListThread;
import com.jxd.bookdistribution.util.SQLiteUitl;
import com.jxd.bookdistribution.util.ToastUtil;
import com.jxd.bookdistribution.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserActivity extends Activity implements View.OnClickListener{
    XListView mlv=null;
    List<Person> mUsers=null;
    UserAdapter mAdapter=null;
    int mPageIdx=0;
    boolean mIsRefresh=false;
    UserListHandler mHandler=null;
    UserXListViewListener mListener=null;
    Button mbtnAddUser =null;
    Button mbtnBack=null;
    TextView mtvTitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mtvTitle.setText(getString(R.string.menuUser));

        mlv= (XListView)this.findViewById(R.id.lvUser);
        mUsers=new ArrayList<Person>();
        mAdapter = new UserAdapter(UserActivity.this,mUsers,false);
        mHandler=new UserListHandler();
        mListener = new UserXListViewListener("");
        mlv.setXListViewListener(mListener);
        mlv.setPullLoadEnable(true);
        mlv.setPullRefreshEnable(true);
        mlv.setAdapter(mAdapter);

        mbtnAddUser = (Button)this.findViewById(R.id.btnAddUser);
        mbtnAddUser.setOnClickListener(this);
        mbtnBack=(Button)this.findViewById(R.id.btnUserBack);
        mbtnBack.setOnClickListener(this);

        AsynLoadUsers();
    }

    @Override
    public void onClick(View view) {
        if(view == mbtnAddUser){
            LayoutInflater inflater = LayoutInflater.from(UserActivity.this);
            final View rrview = inflater.inflate(R.layout.layoutadduser,null);
            new AlertDialog.Builder(UserActivity.this)
                    .setView(rrview)
                    .setTitle("添加用户")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText etUserName = (EditText)rrview.findViewById(R.id.etUserName);
                            String userName = etUserName.getText().toString();
                            EditText etTelephone = (EditText)rrview.findViewById(R.id.etTelephone);
                            String telephone = etTelephone.getText().toString();
                            if (userName.isEmpty()) {
                                return;
                            }
                            SQLiteUitl util = new SQLiteUitl(UserActivity.this);
                            List<Person> users = new ArrayList<Person>();
                            Person item = new Person();
                            item.setUsername(userName);
                            item.setTelephone(telephone);
                            item.setSex("男");
                            item.setEnable("1");
                            item.setPassword("123456");
                            item.setAddress("杭州");
                            users.add(item);
                            util.insertUser(users);

                            AsynLoadUsers();

                        }
                    }).show();


        }else if(view == mbtnBack){
            UserActivity.this.finish();
        }
    }

    protected void AsynLoadUsers() {
        mIsRefresh = false;
        mPageIdx = -1;
        if(mUsers!=null) mUsers.clear();
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

    private class UserListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == UserListThread.MSG_Loaded ) {
                    List<Person> temp = (List<Person>) msg.obj;
                    if (temp == null) {
                        mPageIdx--;
                        onLoad();
                        //mTvCount.setText(mContractList.size() + "/" + msg.arg1);
                        return;
                    }
                    mUsers.addAll(temp);
                    //mTvCount.setText(mContractList.size() + "/" + msg.arg1);

                    onLoad();
                    mAdapter.notifyDataSetChanged();
                } else if (msg.what == UserListThread.MSG_Refresh) {
                    List<Person> temp = (List<Person>) msg.obj;
                    if (temp == null) {
                        //mTvCount.setText("0/" + msg.arg1);
                        mPageIdx--;
                        onLoad();
                        mUsers.clear();
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                    mUsers.addAll(temp);
                    //mTvCount.setText(mContractList.size() + "/" + msg.arg1);

                    // mAdapter = new ContractAdapter(ContractListActivity.this,
                    // mContractList , mDeleteMenuFlag );
                    //mchkAllSelect.setChecked(false);
                    // mlvContracts.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    onLoad();
                } else if (msg.what == UserListThread.MSG_Error) {
                    onLoad();
                }

            } catch (Exception e) {
                ToastUtil.Show( "handlemessage出错了!");
            }
        }
    }


    class UserXListViewListener implements XListView.IXListViewListener {
        String username="";

        public UserXListViewListener( String username ){
            this.username=username;
        }

        public void onRefresh() {
            try {

                mIsRefresh = true;
                if (mUsers != null)
                    mUsers.clear();
                mAdapter.notifyDataSetChanged();
                mPageIdx = 0;

                AsyncQuery();
            } catch (Exception ex) {
                ToastUtil.Show(ex.getMessage());
            }
        }

        protected void AsyncQuery() {
            mIsRefresh=false;
            UserListThread thread = new UserListThread( UserActivity.this ,
                    mPageIdx , mHandler , mIsRefresh, username );
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
