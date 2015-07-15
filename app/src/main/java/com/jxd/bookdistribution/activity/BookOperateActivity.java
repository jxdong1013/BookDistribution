package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.hardware.barcode.Scanner;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.BookAdapter;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Constant;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.thread.BookOperateThread;
import com.jxd.bookdistribution.thread.SiteThread;
import com.jxd.bookdistribution.util.ByteUtil;
import com.jxd.bookdistribution.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BookOperateActivity extends Activity
        implements View.OnClickListener ,
        RadioGroup.OnCheckedChangeListener {

    RadioGroup rdgType=null;
    RadioButton rdbBorrow=null;
    RadioButton rdbReturn=null;
    ListView mlv=null;
    Button btnScan=null;
    List<Book> mBooks=null;
    BookAdapter mAdapter=null;
    BookOperateHandler mHandler=null;
    EditText metSiteName=null;
    LinearLayout mllSite=null;
    List<Site> mSites=null;
    int mSelectedSiteIdx=0;
    TextView mtvTitle=null;
    NfcAdapter mNFCAdapter;
    PendingIntent mNFCPendingIntent;
    ProgressDialog mProgressDlg=null;
    TextView tvStatistic=null;
    Button btnBack=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookoperate);

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mtvTitle.setText(getString(R.string.menuBookOperate));

        rdgType = (RadioGroup)this.findViewById(R.id.rdgType);
        rdgType.setOnCheckedChangeListener(this);
        rdbBorrow = (RadioButton)this.findViewById(R.id.rdbBorrow);
        rdbReturn =(RadioButton)this.findViewById(R.id.rdbReturn);

        mlv=(ListView)this.findViewById(R.id.lvBookList);
        btnScan = (Button)this.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        mBooks=new ArrayList<Book>();
        mAdapter =new BookAdapter(BookOperateActivity.this, mBooks,false);
        mlv.setAdapter(mAdapter);

        mHandler = new BookOperateHandler();

        metSiteName=(EditText)this.findViewById(R.id.etSiteName);
        metSiteName.setOnClickListener(this);
        mllSite=(LinearLayout)this.findViewById(R.id.llSite);
        tvStatistic=(TextView)this.findViewById(R.id.tvStatistic);
        tvStatistic.setText("");
        rdbBorrow.setChecked(true);

        btnBack=(Button)this.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        initNFC();
    }

    protected void initNFC(){
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNFCAdapter == null) {
            ToastUtil.Show("设备没有NFC功能。");
            //finish();
            return;
        }

        mNFCPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

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
            mNFCAdapter.enableForegroundDispatch(this, mNFCPendingIntent, null, null);
            //mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Scanner.m_handler = mHandler;
//        int result = Scanner.InitSCA();
//        if( result!=0){
//            ToastUtil.Show("扫描头初始化失败。");
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNFCAdapter != null) {
            mNFCAdapter.disableForegroundDispatch(this);
            //mAdapter.disableForegroundNdefPush(this);
        }
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
                Save(t);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        this.setIntent( intent);
        resolveIntent(intent);
    }


    @Override
    public void onCheckedChanged(RadioGroup compoundButton, int b) {
        RadioButton rdb = (RadioButton)findViewById( compoundButton.getCheckedRadioButtonId());
        if( rdb == rdbBorrow){
            mllSite.setVisibility(View.VISIBLE);
        }else{
            mllSite.setVisibility(View.GONE);
        }

        if( mBooks!=null){
            mBooks.clear();
            mAdapter.notifyDataSetChanged();
        }
        tvStatistic.setText("");
    }

    protected boolean CheckSame(String barcode){
        if( mBooks==null || mBooks.size()<1) return false;
        for(Book item : mBooks){
            if( item.getBarcode().equals( barcode)){
                return true;
            }
        }
        return false;
    }

    protected void Save(String barcode) {
        if( rdbReturn.isChecked()==false && rdbBorrow.isChecked()==false){
            ToastUtil.Show("请选择操作类型。");
            return;
        }
        if( rdbBorrow.isChecked()){
            if(metSiteName.getText().toString().isEmpty()){
                ToastUtil.Show("请选择站点");
                return;
            }
        }
        if( CheckSame(barcode)){
            ToastUtil.Show("图书已经被扫描过了。");
            return;
        }

        int siteid = 0;
        if( rdbBorrow.isChecked() ) {
            String sitename = metSiteName.getText().toString();
            for (Site item : mSites) {
                if (item.getSiteName().equals(sitename)) {
                    siteid = item.getSiteId();
                    break;
                }
            }
        }

        int status = 0;
        if (rdbBorrow.isChecked()) status = Constant.BookOut;
        if (rdbReturn.isChecked()) status = Constant.BookIn;


        if( mProgressDlg!=null){
            mProgressDlg.dismiss();
            mProgressDlg=null;
        }
        mProgressDlg=new ProgressDialog( BookOperateActivity.this);
        mProgressDlg.setMessage(getString(R.string.scantip));
        mProgressDlg.show();
        BookOperateThread thread = new
                BookOperateThread(BookOperateActivity.this,mHandler,barcode,status,siteid);
        thread.start();


//        Book book = new Book();
//        book.setBarcode(barcode);
//        book.setPublish("机械出版社");
//        book.setPrice(new BigDecimal("78.89"));
//        book.setAuthor("Bruee Eckel");
//        book.setBookName("中国近代史");
//        book.setIsbn("978-7-111-213826");
//        book.setStatus(status);
//        book.setSiteId(siteid);
//        if (mBooks == null) {
//            mBooks = new ArrayList<Book>();
//        }
//        mBooks.add(book);
//
//        SQLiteUitl util = new SQLiteUitl(BookOperateActivity.this);
//        util.UpdateBookSiteId(book);
//
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
       if( view == btnScan){
            if( rdbReturn.isChecked()==false && rdbBorrow.isChecked()==false){
                ToastUtil.Show("请选择操作类型。");
                return;
            }
           if( rdbBorrow.isChecked()){
               if(metSiteName.getText().toString().isEmpty()){
                   ToastUtil.Show("请选择站点");
                   return;
               }
           }

           //Scanner.Read();

//           int siteid = 0;
//           String sitename = metSiteName.getText().toString();
//           for(Site item : mSites){
//               if(item.getSiteName().equals( sitename)){
//                   siteid=item.getSiteId();
//                   break;
//               }
//           }

//            int status = 0;
//            if( rdbBorrow.isChecked()) status=1;
//            if( rdbReturn.isChecked()) status=2;
//            Book book = new Book();
//            book.setBarcode("123456789");
//            book.setPublish("机械出版社");
//            book.setPrice(new BigDecimal("78.89"));
//            book.setAuthor("Bruee Eckel");
//            book.setBookName("中国近代史");
//            book.setIsbn("978-7-111-213826");
//            book.setStatus(status);
//           book.setSiteId(siteid);
//            if( mBooks==null ){
//                mBooks=new ArrayList<Book>();
//            }
//            mBooks.add(book);

//           SQLiteUitl util =new SQLiteUitl(BookOperateActivity.this);
//           util.UpdateBookSiteId(book);
//
//            mAdapter.notifyDataSetChanged();
        }else if( view == metSiteName){
            getSite();
       }else if(view == btnBack){
           this.finish();
       }
    }

    protected void getSite(){
        SiteThread thread = new SiteThread( BookOperateActivity.this , mHandler);
        thread.start();
    }

    class BookOperateHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if( msg.what == SiteThread.MSG_Success){
                mSites = (List<Site>)msg.obj;
                ShowSite();
            }else if(msg.what==SiteThread.MSG_Error){
                ToastUtil.Show(msg.obj == null ? "查询失败" : msg.obj.toString());
                return;
            }
//            else if( msg.what == Scanner.BARCODE_READ) {
//                //显示读到的条码
//                Save(msg.obj.toString());
//            }else if( msg.what == Scanner.BARCODE_NOREAD){
//                ToastUtil.Show("没有扫描到条码。");
//            }
            else if( msg.what== BookOperateThread.MSG_Error){
                if(mProgressDlg!=null){
                    mProgressDlg.dismiss();
                    mProgressDlg=null;
                }
                String tip = msg.obj==null?"操作失败":msg.obj.toString();
                ToastUtil.Show(tip);
                return;
            }else if(msg.what== BookOperateThread.MSG_Success){
                if(mProgressDlg!=null){
                    mProgressDlg.dismiss();
                    mProgressDlg=null;
                }

                Book entity = (Book)msg.obj;
                if( entity!=null){
                    mBooks.add(entity);
                }
                mAdapter.notifyDataSetChanged();

                tvStatistic.setText(""+mBooks.size()+"条");
            }
        }



        protected void ShowSite(){
            if( mSites==null||mSites.size()<1){
                ToastUtil.Show("查无记录。");
                return;
            }
            final String[]sites=new String[mSites.size()];
            int i=0;
            for(Site item : mSites){
                sites[i++]=item.getSiteName();
            }

            AlertDialog.Builder builder= new AlertDialog.Builder(BookOperateActivity.this);
            builder.setTitle("选择站点");
            builder.setSingleChoiceItems( sites,0,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //metSiteName.setText( sites[i] );
                    mSelectedSiteIdx=i;
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    metSiteName.setText(sites[mSelectedSiteIdx]);
                }
            });
            builder.create().show();
        }
    }
}
