package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
//import android.hardware.barcode.Scanner;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Book;
//import com.jxd.bookdistribution.util.NdefMessageParser;
import com.jxd.bookdistribution.util.ByteUtil;
import com.jxd.bookdistribution.util.SQLiteUitl;
import com.jxd.bookdistribution.util.ToastUtil;
//import com.jxd.bookdistribution.util.record.ParsedNdefRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class AddBookActivity extends Activity implements View.OnClickListener{
    EditText etBookName=null;
    EditText etAuthor=null;
    EditText etPublish=null;
    EditText etIsbn=null;
    EditText etPublishDate=null;
    EditText etPrice=null;
    EditText etBarcode=null;
    Button btnSave=null;
    Button btnBack=null;
    Button btnScan=null;
    //BookHandler mHandler=null;
    Book mBook=null;
    TextView mtvTitle=null;
    Intent mNFCIntent=null;
    //---------------------------------
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    //NdefMessage mNdefPushMessage;

    @Override
    protected void onStart() {
        super.onStart();

        //初始化扫描头
//        Scanner.m_handler=mHandler;
//        int result = Scanner.InitSCA();
//        if( result != 0){
//            ToastUtil.Show("扫描头初始化失败。");
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);

        etBookName = (EditText)this.findViewById(R.id.etBookName);
        etAuthor=(EditText)this.findViewById(R.id.etAuthor);
        etPublish=(EditText)this.findViewById(R.id.etPublish);
        etIsbn=(EditText)this.findViewById(R.id.etISBN);
        etPublishDate=(EditText)this.findViewById(R.id.etPublishDate);
        etPrice = (EditText)this.findViewById(R.id.etPrice);
        etBarcode= (EditText)this.findViewById(R.id.etBarcode);

        btnSave = (Button)this.findViewById(R.id.btnSaveBook);
        btnSave.setOnClickListener(this);
        btnBack = (Button)this.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnScan=(Button)this.findViewById(R.id.btnBookScan);
        btnScan.setOnClickListener(this);

        //mHandler = new BookHandler();

        Intent intent=getIntent();
        if( intent.hasExtra("book")){
            mBook=(Book)intent.getSerializableExtra("book");
            if( mBook!=null) {
                etBookName.setText(mBook.getBookName());
                etAuthor.setText(mBook.getAuthor());
                etPublish.setText(mBook.getPublish());
                etIsbn.setText(mBook.getIsbn());
                etPublishDate.setText(mBook.getPublishDate());
                etBarcode.setText(mBook.getBarcode());
                etPrice.setText(mBook.getPrice().toString());
                mtvTitle.setText("编辑书籍");
            }
        }else{
            mBook=null;
            mtvTitle.setText("添加书籍");
        }

        //processNFCItent( getIntent() );

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
            //mAdapter.disableForegroundNdefPush(this);
        }
    }

      @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);

        //processNFCItent(intent);

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
                String t = ByteUtil.Bytes2HexString(myNFCID,myNFCID.length);
                etBarcode.setText(t);
            }

            //Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            //NdefMessage[] msgs;
            //if (rawMsgs != null) {
            //    msgs = new NdefMessage[rawMsgs.length];
            //    for (int i = 0; i < rawMsgs.length; i++) {
            //        msgs[i] = (NdefMessage) rawMsgs[i];
            //    }
            //} else {
            //    ToastUtil.Show("无法识别的标签类型。");
            //    return;
                // Unknown tag type
                //byte[] empty = new byte[0];
                //byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                //Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                //byte[] payload = dumpTagData(tag).getBytes();
                //NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                //NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                //msgs = new NdefMessage[] { msg };
            }
            // Setup the views
            //buildTagViews(msgs);
        //}
    }

    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        //LinearLayout content = mTagContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        //Date now = new Date();
        //List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        //final int size = records.size();
        //for (int i = 0; i < size; i++) {
            //TextView timeView = new TextView(this);
            //timeView.setText(TIME_FORMAT.format(now));
            //content.addView(timeView, 0);
         //   ParsedNdefRecord record = records.get(i);
            //String lb=
            //etBarcode.setText();
            //content.addView(record.getView(this, inflater, content, i), 1 + i);
            //content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);
        //}
    }

    protected void processNFCItent( Intent intent){
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            mNFCIntent = intent;
            //ReadNFCCard();
        }else if( NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())){
            mNFCIntent=intent;
            //ReadNFCCard();
        }else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            mNFCIntent=intent;
            //ReadNFCCard();
        }else{
            mNFCIntent=null;
        }
    }

    @Override
    public void onClick(View view) {
        if( view == btnSave){
            saveBook();
        }else if( view == btnBack){
            AddBookActivity.this.finish();
        }else if( view == btnScan){
            //Scanner.Read();
            ReadNFCCard();
        }
    }

    protected void ReadNFCCard(){
        if( mNFCIntent==null){
            ToastUtil.Show("请把标签放到感应区。");
            return;
        }else {
            byte[] myNFCID = mNFCIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            if (myNFCID != null) {
                String t = ByteUtil.Bytes2HexString(myNFCID, myNFCID.length );
                etBarcode.setText(t);
            }
        }
    }

    //字符序列转换为16进制字符串
//    private String bytesToHexString(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder("0x");
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        char[] buffer = new char[2];
//        for (int i = 0; i < src.length; i++) {
//            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
//            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
//            System.out.println(buffer);
//            stringBuilder.append(buffer);
//        }
//        return stringBuilder.toString().toUpperCase();
//    }

    protected void clearText(){
        mBook=null;
        etBarcode.setText("");
        etAuthor.setText("");
        etBookName.setText("");
        etIsbn.setText("");
        etPublish.setText("");
        etPublishDate.setText("");
        etPrice.setText("");
    }

    protected void saveBook(){
        boolean isok =Check();
        if( isok==false )return;
        String operateType= "add";
        String sBarCode="";
        if( mBook ==null) {
            mBook = new Book();
            operateType="add";
        }else {
            sBarCode = mBook.getBarcode();
            operateType="update";
        }

            String bookname = etBookName.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String publish = etPublish.getText().toString().trim();
            String isbn = etIsbn.getText().toString().trim();
            String publishDate = etPublishDate.getText().toString().trim();
            String pricestr = etPrice.getText().toString().trim();
            String barcode = etBarcode.getText().toString().trim();

            BigDecimal price = BigDecimal.ZERO;
            try {
                price = new BigDecimal(pricestr);
            } catch (Exception ex) {
                price = BigDecimal.ZERO;
            }

            mBook.setBookName(bookname);
            mBook.setAuthor(author);
            mBook.setPublish(publish);
            mBook.setPublishDate(publishDate);
            mBook.setIsbn(isbn);
            mBook.setPrice(price);
            //mBook.setStatus(0);
            //mBook.setSiteId(0);
            //mBook.setRemark("");
            mBook.setBarcode(barcode);

            SQLiteUitl util = new SQLiteUitl(AddBookActivity.this);
            List<Book> books = new ArrayList<Book>();
            books.add(mBook);
        if( operateType.equals("add")) {
            String dBarcode = mBook.getBarcode();
            if( dBarcode.isEmpty()==false ){
                boolean isExist = util.ExistBookByBarcode(dBarcode);
                if( isExist){
                    ToastUtil.Show("标签已经被使用了。");
                    return;
                }
            }
            int result = util.insertBooks(books);
            if (result > 0) {
                clearText();
                ToastUtil.Show("添加成功。");
                this.finish();
            } else {
                ToastUtil.Show("添加失败。");
            }
        }else{
            String dBarcode = mBook.getBarcode();
            if( sBarCode.isEmpty()==false && dBarcode.isEmpty()==false &&
                    sBarCode.equals(dBarcode) ==false ){
                boolean isExist = util.ExistBookByBarcode(dBarcode);
                if( isExist ){
                    ToastUtil.Show("标签已经被使用了。");
                    return;
                }
            }

            long result = util.updateBook(mBook);
            if (result > 0) {
                ToastUtil.Show("更新成功。");
                this.finish();
            } else {
                ToastUtil.Show("更新失败。");
            }
        }
    }

    protected boolean Check(){
        String bookname = etBookName.getText().toString();
        if(bookname.isEmpty()){
            ToastUtil.Show("请输入书籍名称");
            return false;
        }
        String priceStr =etPrice.getText().toString();
        if( priceStr.isEmpty() == false ){
            try{
            BigDecimal price = new BigDecimal(priceStr);
            }catch (Exception ex){
                ToastUtil.Show("请输入正确的价格");
                return false;
            }
        }
        return true;
    }


    class BookHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);

            switch (msg.what) {

//                case Scanner.BARCODE_READ: {
//                    //显示读到的条码
//                    String barcode = (String)msg.obj;
//                    etAuthor.setText("金向东");
//                    etBookName.setText("小儿成长记");
//                    etIsbn.setText("978-7-111-213826");
//                    etPrice.setText("54.52");
//                    etPublish.setText("少儿出版社");
//                    etPublishDate.setText("2015-05-05");
//                    etBarcode.setText(barcode);
//
//                    break;
//                }
//                case Scanner.BARCODE_NOREAD:{
//                    ToastUtil.Show("没有扫描到条码。");
//                    break;
//                }

                default:
                    break;
            }
        }
    }
}
