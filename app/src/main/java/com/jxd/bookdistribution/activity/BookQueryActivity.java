package com.jxd.bookdistribution.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.util.ByteUtil;
import com.jxd.bookdistribution.util.ToastUtil;

public class BookQueryActivity extends Activity implements View.OnClickListener{
    EditText etBookname=null;
    EditText etauthor=null;
    EditText etpublish = null;
    EditText etisbn=null;
    EditText etPublisDate=null;
    EditText etBarcode=null;
    Button btnQuery=null;
    Button btnQueryBack=null;
    TextView tvTitle=null;
    //---------------------------------
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookquery);

        etBookname = (EditText)this.findViewById(R.id.etquerybookname);
        etauthor=(EditText)this.findViewById(R.id.etqueryauthor);
        etpublish = (EditText)this.findViewById(R.id.etquerypublish);
        etisbn =(EditText)this.findViewById(R.id.etqueryisbn);
        etPublisDate=(EditText)this.findViewById(R.id.etquerypublishdate);
        etBarcode=(EditText)this.findViewById(R.id.etquerybarcode);
        btnQuery =(Button)this.findViewById(R.id.btnBookQuery);
        btnQuery.setOnClickListener(this);
        btnQueryBack=(Button)this.findViewById(R.id.btnBookQueryBack);
        btnQueryBack.setOnClickListener(this);

        tvTitle= (TextView)this.findViewById(R.id.tvAppTitle);
        tvTitle.setText( getString(R.string.menuBookQuery));

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
                //showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            //mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
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
                etBarcode.setText(t);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if( view == btnQuery){
            Intent intent = new Intent();
            intent.setClass(BookQueryActivity.this, BookResultActivity.class);

            String bookname= etBookname.getText().toString().trim();
            String author= etauthor.getText().toString().trim();
            String publish = etpublish.getText().toString().trim();
            String isbn=etisbn.getText().toString().trim();
            String publishDate= etPublisDate.getText().toString().trim();
            String barcode=etBarcode.getText().toString().trim();
            intent.putExtra("bookname", bookname);
            intent.putExtra("author",author);
            intent.putExtra("publish",publish);
            intent.putExtra("isbn",isbn);
            intent.putExtra("publishdate",publishDate);
            intent.putExtra("barcode",barcode );

            BookQueryActivity.this.startActivity(intent);

        }else if(view==btnQueryBack){
            BookQueryActivity.this.finish();
        }
    }

}
