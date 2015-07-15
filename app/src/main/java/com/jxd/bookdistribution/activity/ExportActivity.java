package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.ExportSiteAdapter;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.thread.ExportExcelThread;
import com.jxd.bookdistribution.thread.SiteThread;
import com.jxd.bookdistribution.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    List<Site> mList=null;
    ListView mlv=null;
    ExportSiteAdapter mAdapter=null;
    ExportHandler mHandler=null;
    Button mBtnExport=null;
    Button mBtnBack=null;
    public static int EXCEL_SELECT_CODE=1010;
    ProgressDialog mProgressDlg = null;
    TextView mTvTitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        mTvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mTvTitle.setText(getString(R.string.menuBookExport));

        mlv=(ListView)this.findViewById(R.id.lvSites);
        mList=new ArrayList<Site>();
        mAdapter=new ExportSiteAdapter( ExportActivity.this, mList);
        mlv.setAdapter(mAdapter);
        mHandler = new ExportHandler();
        mlv.setOnItemClickListener(this);
        mBtnBack=(Button)this.findViewById(R.id.btnExportBack);
        mBtnBack.setOnClickListener(this);
        mBtnExport=(Button)this.findViewById(R.id.btnExport);
        mBtnExport.setOnClickListener(this);

        AsynLoadSite();
    }

    void AsynLoadSite(){
        SiteThread thread=new SiteThread(ExportActivity.this, mHandler );
        thread.start();
    }

    @Override
    public void onClick(View view) {
        if(view == mBtnBack){
            ExportActivity.this.finish();
        }else if( view==mBtnExport){
            Export();
        }
    }

    void Export(){
        boolean isSelected=false;
        Site entity=null;
        for(Site item : mList){
            if(item.getSelected()==true){
                entity=item;
                isSelected=true;
                break;
            }
        }
        if(isSelected==false){
            ToastUtil.Show("请选择要导出的站点");
            return;
        }

        if (mProgressDlg != null) {
            mProgressDlg.dismiss();
            mProgressDlg = null;
        }
        mProgressDlg = new ProgressDialog(ExportActivity.this);
        mProgressDlg.setMessage(getString(R.string.exportmsg));
        mProgressDlg.show();

        ExportExcelThread thread=new ExportExcelThread( ExportActivity.this , mHandler , entity.getSiteId() , entity.getSiteName());
        thread.start();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if( i < 0 || mList ==null || mList.size()<1 ) return;
        for( Site item : mList){
            item.setSelected(false);
        }
        Site site = mList.get(i);
        site.setSelected( !site.getSelected() );
        mAdapter.notifyDataSetChanged();
    }

    class ExportHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if( msg.what == SiteThread.MSG_Success){
                if(mList==null)mList=new ArrayList<Site>();
                mList.clear();
                List<Site> temp = (List<Site>)msg.obj;
                if( temp != null){
                mList.addAll(temp);
                }
                mAdapter.notifyDataSetChanged();
            }else if(msg.what == SiteThread.MSG_Error) {
                String error = msg.obj ==null? "查询失败":msg.obj.toString();
                ToastUtil.Show(error);
            }else if( msg.what == ExportExcelThread.MsgExportOK){
                if( mProgressDlg !=null){
                    mProgressDlg.dismiss();
                    mProgressDlg=null;
                }
                ToastUtil.Show( msg.obj.toString() );
            }else if( msg.what == ExportExcelThread.MsgExportFalse){
                if( mProgressDlg !=null){
                    mProgressDlg.dismiss();
                    mProgressDlg=null;
                }
                ToastUtil.Show( msg.obj.toString() );
            }
        }
    }
}
