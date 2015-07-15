package com.jxd.bookdistribution.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.InventoryListAdapter;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Inventory;
import com.jxd.bookdistribution.thread.InventoryListThread;
import com.jxd.bookdistribution.util.ByteUtil;
import com.jxd.bookdistribution.util.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryListActivity extends Activity implements View.OnClickListener{
    List<Book> mlist=null;
    ListView mlv=null;
    Button mbtnFinish=null;
    Button mbtnBack=null;
    //Button mbtnScan= null;
    Inventory mInventory=null;
    TextView mtvTitle=null;
    ProgressDialog mProgressDlg=null;
    InventoryListHandler mHandler=null;
    InventoryListAdapter mAdapter=null;
    NfcAdapter mNFCAdapter;
    PendingIntent mPendingIntent;
    TextView mTvTipInfo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventorylist);

        mbtnFinish=(Button)this.findViewById(R.id.btnfinish);
        mbtnFinish.setOnClickListener(this);
        mbtnBack=(Button)this.findViewById(R.id.btnInventoryBack);
        mbtnBack.setOnClickListener(this);
        mlv=(ListView)this.findViewById(R.id.lvlist);
        //mbtnScan=(Button)this.findViewById(R.id.btnscan);
        //mbtnScan.setOnClickListener(this);


        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mTvTipInfo=(TextView)this.findViewById(R.id.tvTipInfo);

        Intent intent = this.getIntent();
        if( intent.hasExtra("inventory")){
            mInventory =(Inventory) intent.getSerializableExtra("inventory");
            mtvTitle.setText(mInventory.getTaskName());
        }

        mlist=new ArrayList<Book>();
        mHandler=new InventoryListHandler();
        mAdapter=new InventoryListAdapter(InventoryListActivity.this, mlist , false );
        mlv.setAdapter(mAdapter);

        initNFC();

        AsynLoadBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNFCAdapter != null) {
            if (!mNFCAdapter.isEnabled()) {
                ToastUtil.Show("请打开NFC功能。");
                return;
                //showWirelessSettingsDialog();
            }
            mNFCAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            //mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNFCAdapter != null) {
            mNFCAdapter.disableForegroundDispatch(this);
            //mAdapter.disableForegroundNdefPush(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        this.setIntent( intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            if (myNFCID != null) {
                String t = ByteUtil.Bytes2HexString(myNFCID, myNFCID.length);
                //etBarcode.setText(t);
                //ToastUtil.Show(t);
                setInventoryBook(t);
            }
        }
    }

    private void setInventoryBook(String barcode){
        if( mlist==null )return;
        for( Book item : mlist){
            if( item.getBarcode().equals( barcode )){
                item.setSelected(true);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

        protected void initNFC(){
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNFCAdapter == null) {
            ToastUtil.Show("设备没有NFC功能。");
            //finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        //mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
        //        "Message from NFC Reader :-)", Locale.ENGLISH, true) });
    }

    private void AsynLoadBooks(){
        if(mProgressDlg!=null){
            mProgressDlg.dismiss();
            mProgressDlg=null;
        }
        mProgressDlg=new ProgressDialog(InventoryListActivity.this);
        mProgressDlg.setMessage(getString(R.string.querytip));
        mProgressDlg.show();

        InventoryListThread thread=new InventoryListThread(InventoryListActivity.this,mHandler, mInventory.getSiteId());
        thread.start();
    }

    @Override
    public void onClick(View view) {
        if( view == mbtnBack){
            InventoryListActivity.this.finish();
        }else if( view == mbtnFinish){
            finishInventory();
        }
    }

    private void finishInventory(){
        if(mlist==null || mlist.size()<1) return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                InventoryListActivity.this);
        dialog.setTitle("盘点操作");
        int count= 0;
        for(Book item : mlist){
            if(item.getSelected()==true)count++;
        }
        int uncount = mlist.size()-count;
        String msg = "本次盘点共"+ mlist.size()+",完成"+ count+" 未完成"+ uncount;
        dialog.setMessage(msg);
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        //new ExchangeAsyncTask().execute(flowTargets
                        //        .get(position));
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    class InventoryListHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if( msg.what == InventoryListThread.MSG_Error){
                if( mProgressDlg!=null){
                    mProgressDlg.dismiss();
                    mProgressDlg=null;
                }
                String tip = msg.obj ==null? "查询失败": msg.obj.toString();
                ToastUtil.Show(tip);
                return;
            }else if( msg.what == InventoryListThread.MSG_Success){
                if(mProgressDlg!=null){
                    mProgressDlg.dismiss();
                    mProgressDlg=null;
                }

                if(mlist==null) mlist=new ArrayList<Book>();
                mlist.clear();
                List<Book> temp = (List<Book>) msg.obj;
                if(temp !=null){
                    mlist.addAll(temp);
                }

                //---------------test
//                Book b = new Book();
//                b.setAuthor("a");
//                b.setBarcode("1");
//                b.setBookId(1);
//                b.setBookName("a");
//                b.setIsbn("1");
//                b.setPrice(new BigDecimal(11));
//                b.setPublish("1");
//                mlist.add(b);
//                b = new Book();
//                b.setAuthor("b");
//                b.setBarcode("2");
//                b.setBookId(2);
//                b.setBookName("b");
//                b.setIsbn("2");
//                b.setPrice(new BigDecimal(22));
//                b.setPublish("2");
//                mlist.add(b);
//                b = new Book();
//                b.setAuthor("c");
//                b.setBarcode("3");
//                b.setBookId(3);
//                b.setBookName("c");
//                b.setIsbn("3");
//                b.setPrice(new BigDecimal(33));
//                b.setPublish("3");
//                mlist.add(b);
                //-----------------------------

                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
