package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Inventory;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.thread.SiteThread;
import com.jxd.bookdistribution.util.SQLiteUitl;
import com.jxd.bookdistribution.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AddInventoryActivity extends Activity implements View.OnClickListener {
    EditText metTaskName=null;
    EditText metSiteName=null;
    Button mbtnSave=null;
    Button mbtnBack=null;
    List<Site> mSites=null;
    AddInventoryHandler mHandler=null;    int selectedIdx=0;
    TextView mTvtitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinventory);

        mTvtitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mTvtitle.setText("新增盘点");

        metTaskName=(EditText)this.findViewById(R.id.etInventoryName);
        metSiteName=(EditText)this.findViewById(R.id.etSiteName);
        metSiteName.setOnClickListener(this);

        mbtnBack=(Button)this.findViewById(R.id.btnBack);
        mbtnBack.setOnClickListener(this);
        mbtnSave=(Button)this.findViewById(R.id.btnSaveInventory);
        mbtnSave.setOnClickListener(this);

        mHandler =new AddInventoryHandler();
    }

    @Override
    public void onClick(View view) {
        if( view == mbtnBack){
            AddInventoryActivity.this.finish();
        }else if( view == metSiteName){
            getSite();
        }
        else if( view == mbtnSave){
            String taskname=metTaskName.getText().toString().trim();
            String sitename= metSiteName.getText().toString().trim();
            Integer siteId=0;

            if( taskname.isEmpty()){
                ToastUtil.Show("请输入盘点任务名称。");
                return ;
            }
            if( sitename.isEmpty()){
                ToastUtil.Show("请选择盘点站点。");
                return;
            }

            for(Site item : mSites){
                if(item.getSiteName().equals( sitename)){
                    siteId=item.getSiteId();
                    break;
                }
            }

            Inventory entity = new Inventory();
            entity.setSiteName( sitename);
            entity.setSiteId(siteId);
            entity.setStatus(0);
            entity.setTaskName(taskname);

            SQLiteUitl util =new SQLiteUitl(AddInventoryActivity.this);
            List<Inventory> list = new ArrayList<>();
            list.add(entity);
            int result = util.addInventory( list );
            if( result>0){
                ToastUtil.Show("添加成功。");
                setResult( RESULT_OK );
                AddInventoryActivity.this.finish();
            }else{
                ToastUtil.Show("添加失败。");
            }
        }
    }

    protected void getSite(){
        SiteThread thread = new SiteThread( AddInventoryActivity.this , mHandler);
        thread.start();
    }


    class AddInventoryHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if( msg.what == SiteThread.MSG_Success){
                mSites = (List<Site>)msg.obj;
                ShowSite();
            }
        }



        protected void ShowSite(){
            if( mSites==null || mSites.size()<1){
                ToastUtil.Show("请先新增站点，再操作。");
                return;
            }

            final String[]sites=new String[mSites.size()];

            int i=0;
            for(Site item : mSites){
                sites[i++]=item.getSiteName();
            }

            AlertDialog.Builder builder= new AlertDialog.Builder( AddInventoryActivity.this);
            builder.setTitle("选择站点");
            builder.setSingleChoiceItems( sites,0,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //metSiteName.setText( sites[i] );
                    selectedIdx=i;
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    metSiteName.setText( sites[selectedIdx] );
                }
            });
            builder.create().show();
        }
    }
}
