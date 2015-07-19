package com.jxd.bookdistribution.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.BookAdapter;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.thread.BookListThread;
import com.jxd.bookdistribution.util.ToastUtil;
import com.jxd.bookdistribution.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookResultActivity extends Activity implements AdapterView.OnItemClickListener{
    TextView tvSummary=null;
    XListView mlv=null;
    BookResultXListViewListener mListener=null;
    List<Book> mBooks=null;
    BookAdapter mAdapter=null;
    BookResultListHandler mHandler=null;
    int mPageIdx=0;
    boolean mIsRefresh=false;
    String bookname="";
    String author="";
    String isbn="";
    String publish="";
    String publishDate="";
    String barcode="";
    TextView tvTitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookresult);

        tvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        tvTitle.setText("查询结果");

        tvSummary = (TextView)this.findViewById(R.id.tvQuerySummary);
        mlv= (XListView)this.findViewById(R.id.lvBookResult);

        Intent intent = this.getIntent();
        if(intent !=null){
            bookname=intent.getStringExtra("bookname");
            author=intent.getStringExtra("author");
            isbn=intent.getStringExtra("isbn");
            publish=intent.getStringExtra("publish");
            publishDate=intent.getStringExtra("publishdate");
            barcode=intent.getStringExtra("barcode");
        }

        mHandler = new BookResultListHandler();
        mlv = (XListView) this.findViewById(R.id.lvBookResult);
        mListener = new BookResultXListViewListener( bookname,author,isbn,publish,publishDate,barcode);
        mlv.setXListViewListener(mListener);
        mlv.setPullLoadEnable(true);
        mlv.setPullRefreshEnable(true);
        mBooks = new ArrayList<>();
        mAdapter = new BookAdapter(BookResultActivity.this, mBooks, false);
        mlv.setAdapter(mAdapter);
        mlv.setOnItemClickListener(this);

        AsynLoadBooks();

    }

    protected void AsynLoadBooks() {
        mIsRefresh = false;
        mPageIdx = -1;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if( i < 1 || mBooks ==null || mBooks.size()<1 ) return;
        Intent intent = new Intent();
        intent.setClass(BookResultActivity.this,AddBookActivity.class);
        Book book = mBooks.get(i-1);
        Bundle bd = new Bundle();
        bd.putSerializable("book", book);
        intent.putExtras(bd);
        BookResultActivity.this.startActivity(intent);
    }

    private class BookResultListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == BookListThread.MSG_Loaded ) {
                    List<Book> temp = (List<Book>) msg.obj;
                    if (temp == null) {
                        mPageIdx--;
                        onLoad();
                        tvSummary.setText(mBooks.size() + "/" + msg.arg1);
                        return;
                    }
                    mBooks.addAll(temp);
                    tvSummary.setText(mBooks.size() + "/" + msg.arg1);

                    onLoad();
                    mAdapter.notifyDataSetChanged();
                } else if (msg.what == BookListThread.MSG_Refresh) {
                    List<Book> temp = (List<Book>) msg.obj;
                    if (temp == null) {
                        tvSummary.setText("0/" + msg.arg1);
                        mPageIdx--;
                        onLoad();
                        mBooks.clear();
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                    mBooks.addAll(temp);
                    tvSummary.setText(mBooks.size() + "/" + msg.arg1);
                    mAdapter.notifyDataSetChanged();
                    onLoad();
                } else if (msg.what == BookListThread.MSG_Error) {
                    onLoad();
                }

            } catch (Exception e) {
                ToastUtil.Show( "handlemessage出错了!");
            }
        }
    }

    class BookResultXListViewListener implements XListView.IXListViewListener {
        String bookname="";
        String author="";
        String isbn="";
        String publish="";
        String publishdate="";
        String barcode="";

        public BookResultXListViewListener( String bookname , String author,
                                            String isbn, String publish,
                                            String publishdate,String barcode ){
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
            BookListThread thread = new BookListThread( BookResultActivity.this ,
                    mPageIdx , mHandler , mIsRefresh, bookname ,author,isbn,publish,publishdate,barcode );
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
