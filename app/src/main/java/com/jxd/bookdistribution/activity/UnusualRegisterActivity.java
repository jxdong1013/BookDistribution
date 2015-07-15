package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Constant;
import com.jxd.bookdistribution.bean.UnusualRegister;
import com.jxd.bookdistribution.util.ByteUtil;
import com.jxd.bookdistribution.util.SQLiteUitl;
import com.jxd.bookdistribution.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class UnusualRegisterActivity extends Activity implements View.OnClickListener{
    TextView mtvTitle =null;
    EditText metbookname=null;
    EditText metauthor;
    EditText metpublish;
    EditText metisbn;
    EditText metprice;
    EditText metbarcode;
    EditText metpublishdate;
    EditText metremark;
    Button mbtnSave;
    Button mbtnBack;
    RadioButton mrdbMissing;
    RadioButton mrdbDefaced;
    RadioButton mrdbStolen;
    //----------------------
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unusualregister);

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mtvTitle.setText(getString(R.string.menuUnusualRegister));

        metbookname=(EditText)this.findViewById(R.id.etBookName);
        metauthor=(EditText)this.findViewById(R.id.etAuthor);
        metpublish=(EditText)this.findViewById(R.id.etPublish);
        metisbn=(EditText)this.findViewById(R.id.etISBN);
        metpublishdate=(EditText)this.findViewById(R.id.etPublishDate);
        metbarcode = (EditText)this.findViewById(R.id.etBarcode);
        metprice=(EditText)this.findViewById(R.id.etPrice);
        metremark=(EditText)this.findViewById(R.id.etRemark);

        mrdbMissing=(RadioButton)this.findViewById(R.id.rdbMissing);
        mrdbDefaced=(RadioButton)this.findViewById(R.id.rdbDefaced);
        mrdbStolen=(RadioButton)this.findViewById(R.id.rdbStolen);

        mbtnSave=(Button)this.findViewById(R.id.btnAddRegister);
        mbtnSave.setOnClickListener(this);
        mbtnBack=(Button)this.findViewById(R.id.btnBack);
        mbtnBack.setOnClickListener(this);

        initNFC();
    }

    /*

     */
    protected void initNFC(){
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            ToastUtil.Show("设备没有NFC功能。");
            //finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        //mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
        //        "Message from NFC Reader :-)", Locale.ENGLISH, true) });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                ToastUtil.Show("请打开NFC功能。");
                return;
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
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
                metbarcode.setText(t);
            }
        }
    }



    @Override
    public void onClick(View view) {
        if( view == mbtnBack){
            UnusualRegisterActivity.this.finish();
        }else if(view== mbtnSave){
            Save();
        }
    }

    protected void Save(){
        String bookname= metbookname.getText().toString().trim();
        String author =metauthor.getText().toString().trim();
        String publish=metpublish.getText().toString().trim();
        String isbn=metisbn.getText().toString().trim();
        String publishdate = metpublishdate.getText().toString().trim();
        String price = metprice.getText().toString().trim();
        String barcode= metbarcode.getText().toString().trim();
        String remark = metremark.getText().toString().trim();

        if( bookname.isEmpty()){
            ToastUtil.Show("请输入书籍名称。");
            return;
        }

        Integer status=0;
        if( mrdbMissing.isChecked() ){
            status = Constant.BookMissing;
      }else if( mrdbDefaced.isChecked()) {
            status=Constant.BookDefaced;
       }else if(mrdbStolen.isChecked()){
            status=Constant.BookStolen;
        }

        if(status==0){
            ToastUtil.Show("请选择图书异常类型。");
            return;
        }

        SQLiteUitl util=new SQLiteUitl(UnusualRegisterActivity.this);
        UnusualRegister entity=new UnusualRegister();
        List<UnusualRegister> list =new ArrayList<>();
        entity.setAuthor(author);
        entity.setBarcode(barcode);
        entity.setBookName(bookname);
        entity.setIsbn(isbn);
        entity.setPrice(price);
        entity.setPublish(publish);
        entity.setPublishDate(publishdate);
        entity.setRemark(remark);
        entity.setStatus(status);
        entity.setUsername("");

        list.add(entity);

       int result= util.insertUnusualRegister(list);
        if(result>0){
            ToastUtil.Show("登记成功。");
            UnusualRegisterActivity.this.finish();
        }else{
            ToastUtil.Show("登记失败。");
        }
    }
}
