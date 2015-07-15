package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.BookAdapter;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Inventory;
import com.jxd.bookdistribution.thread.BookListThread;
import com.jxd.bookdistribution.util.ToastUtil;
import com.jxd.bookdistribution.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    XListView mlv=null;
    BookAdapter mAdapter=null;
    Button mBtnAddBook=null;
    Button mBtnBack=null;
    int mPageIdx=0;
    boolean mIsRefresh=false;
    public static int RequestCodeUpdate = 1000;
    List<Book> mBooks=null;
    BookListHandler mHandler=null;
    BookXListViewListener mListener=null;
    TextView mTvTitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mBtnAddBook = (Button) this.findViewById(R.id.btnAddBook);
        mBtnAddBook.setOnClickListener(this);
        mBtnBack = (Button) this.findViewById(R.id.btnBookBack);
        mBtnBack.setOnClickListener(this);
        mHandler = new BookListHandler();
        mlv = (XListView) this.findViewById(R.id.lvBook);
        mListener = new BookXListViewListener("","","","","","");
        mlv.setXListViewListener(mListener);
        mlv.setPullLoadEnable(true);
        mlv.setPullRefreshEnable(true);
        mBooks = new ArrayList<Book>();
        mAdapter = new BookAdapter(BookActivity.this, mBooks, false);
        mlv.setAdapter(mAdapter);
        mlv.setOnItemClickListener(this);

        mTvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mTvTitle.setText(getString(R.string.menuBook));

        AsynLoadBooks();
    }


    protected void AsynLoadBooks() {
            mIsRefresh = false;
            mPageIdx = -1;
            mlv.startLoadMore();
    }

    @Override
    public void onClick(View view) {
        if( view == mBtnAddBook){
            Intent intent = new Intent();
            intent.setClass(BookActivity.this , AddBookActivity.class);
            BookActivity.this.startActivityForResult(intent, RequestCodeUpdate );
        }else if( view == mBtnBack){
            BookActivity.this.finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if( i < 1 || mBooks ==null || mBooks.size()<1 ) return;
        Intent intent = new Intent();
        intent.setClass(BookActivity.this,AddBookActivity.class);
        Book book = mBooks.get(i-1);
        Bundle bd = new Bundle();
        bd.putSerializable("book", book);
        intent.putExtras(bd);
        BookActivity.this.startActivityForResult(intent,RequestCodeUpdate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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


    private class BookListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == BookListThread.MSG_Loaded ) {
                    List<Book> temp = (List<Book>) msg.obj;
                    if (temp == null) {
                        mPageIdx--;
                        onLoad();
                        //mTvCount.setText(mContractList.size() + "/" + msg.arg1);
                        return;
                    }
                    mBooks.addAll(temp);
                    //mTvCount.setText(mContractList.size() + "/" + msg.arg1);

                    onLoad();
                    mAdapter.notifyDataSetChanged();
                } else if (msg.what == BookListThread.MSG_Refresh) {
                    List<Book> temp = (List<Book>) msg.obj;
                    if (temp == null) {
                        //mTvCount.setText("0/" + msg.arg1);
                        mPageIdx--;
                        onLoad();
                        mBooks.clear();
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                    mBooks.addAll(temp);
                    //mTvCount.setText(mContractList.size() + "/" + msg.arg1);

                    // mAdapter = new ContractAdapter(ContractListActivity.this,
                    // mContractList , mDeleteMenuFlag );
                    //mchkAllSelect.setChecked(false);
                    // mlvContracts.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    onLoad();
                } else if (msg.what == BookListThread.MSG_Error) {
                    onLoad();
                }
                // else if (msg.what == ExportTxtThread.MsgExportOK) {
                // CloseProgressDialog();
                // String message = msg.obj.toString();
                // ToastUtil.Show( message);
                // } else if (msg.what == ExportTxtThread.MsgExportFalse) {
                // CloseProgressDialog();
                // String message = msg.obj.toString();
                // ToastUtil.Show( message);
                // }
//                else if (msg.what == DeleteContractThread.msgDeleteOK) {
//                    CloseProgressDialog();
//                    CloseDeleteMenu();
//                    String message = msg.obj.toString();
//                    ToastUtil.Show(message);
//                    mlvContracts.startRefresh();
//
//                } else if (msg.what == DeleteContractThread.msgDeleteFalse) {
//                    CloseProgressDialog();
//                    String message = msg.obj.toString();
//                    ToastUtil.Show(message);
//                } else if (msg.what == FunKeyReceiver.MsgF1) {
//                    // ScanRFID();
//                } else if (msg.what == FunKeyReceiver.MsgF2) {
//                    mlvContracts.startRefresh();
//                } else if (msg.what == ScanRFIDThread.MsgScanOK) {
//                    CloseProgressDialog();
//                    String uid = msg.obj.toString();
//                    // mSearchText.setText(uid);
//                    mlvContracts.startRefresh();
//                } else if (msg.what == ScanRFIDThread.MsgScanFalse) {
//                    CloseProgressDialog();
//                    String message = msg.obj.toString();
//                    // mSearchText.setText("");
//                    ToastUtil.Show(message);
//                } else if (msg.what == DeleteAllThread.msgDeleteAllOK) {
//                    CloseProgressDialog();
//                    String message = msg.obj.toString();
//                    ToastUtil.Show(message);
//                    // mSearchText.setText("");
//                    mlvContracts.startRefresh();
//                } else if (msg.what == DeleteAllThread.msgDeleteAllFalse) {
//                    CloseProgressDialog();
//                    String message = msg.obj.toString();
//                    ToastUtil.Show(message);
//                } else if (msg.what == ImportExcelThread.ImportOk) {
//                    CloseProgressDialog();
//                    mlvContracts.startRefresh();
//                    ToastUtil.Show(msg.obj.toString());
//                } else if (msg.what == ImportExcelThread.ImportFalse) {
//                    CloseProgressDialog();
//                    ToastUtil.Show(msg.obj.toString());
//                }
                // else if ( msg.what == ImportTxtThread.ImportTxtOk){
                // CloseProgressDialog();
                // mlvContracts.startRefresh();
                // ToastUtil.Show(msg.obj.toString());
                // }
                // else if( msg.what == ImportTxtThread.ImportTxtFalse){
                // CloseProgressDialog();
                // ToastUtil.Show(msg.obj.toString());
                // }

            } catch (Exception e) {
                ToastUtil.Show( "handlemessage出错了!");
            }
        }
    }


    class BookXListViewListener implements XListView.IXListViewListener {
        String bookname="";
        String author="";
        String isbn="";
        String publish="";
        String publishdate="";
        String barcode="";

        public BookXListViewListener( String bookname ,
                                      String author,String isbn,
                                      String publish,String publishdate,String barcode ){
            this.bookname=bookname;
        this.author=author;
            this.isbn=isbn;
            this.publish=publish;
            this.publishdate=publishdate;
            this.barcode=barcode;
        }

        public void onRefresh() {
            try {

                mIsRefresh = true;
                if (mBooks != null)
                    mBooks.clear();
                mAdapter.notifyDataSetChanged();
                mPageIdx = 0;

                AsyncQuery();
            } catch (Exception ex) {
                ToastUtil.Show(ex.getMessage());
            }
        }

        protected void AsyncQuery() {
            mIsRefresh=false;
            BookListThread thread = new BookListThread( BookActivity.this ,
                    mPageIdx , mHandler , mIsRefresh,
                    bookname ,author,isbn,publish ,publishdate , barcode);
            thread.start();
       }

        public void onLoadMore() {
            try {
                mPageIdx++;
                mIsRefresh = false;

                AsyncQuery();

            } catch (Exception ex) {

            }
            // ContractQueryThread thread = new
            // ContractQueryThread(ContractListActivity.this, mHandler,
            // mQueryForm);
            // thread.start();
        }
    }

}
