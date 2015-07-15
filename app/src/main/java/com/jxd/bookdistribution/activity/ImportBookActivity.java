package com.jxd.bookdistribution.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.thread.ImportExcelThread;
import com.jxd.bookdistribution.util.ToastUtil;

public class ImportBookActivity extends Activity implements View.OnClickListener{
    Button mbtnSelect;
    TextView mtvTitle;
    ProgressDialog mProgressDlg=null;
    ImportBookHandler mHandler=null;
    public  static int EXCEL_SELECT_CODE=1033;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importbook);

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mtvTitle.setText(getString(R.string.menuImportBook));

        mbtnSelect=(Button)this.findViewById(R.id.btnImport);
        mbtnSelect.setOnClickListener(this);

        mHandler=new ImportBookHandler();
    }

    @Override
    public void onClick(View view) {
        if(view == mbtnSelect){
            Intent intent =new Intent( ImportBookActivity.this, FileManagerActivity.class);
            ImportBookActivity.this.startActivityForResult(intent, EXCEL_SELECT_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EXCEL_SELECT_CODE) {
            String path = data.getStringExtra("file");
            importExcel(path);
        }
    }

    protected void importExcel(String path){
        if (mProgressDlg != null) {
            mProgressDlg.dismiss();
            mProgressDlg = null;
        }
        mProgressDlg = new ProgressDialog(ImportBookActivity.this);
        mProgressDlg.setMessage(getString(R.string.importexceltip));
        mProgressDlg.show();

        new ImportExcelThread(ImportBookActivity.this , mHandler , path).start();
    }

    private class ImportBookHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                if( msg.what == ImportExcelThread.ImportOk){
                    if(mProgressDlg!=null){
                        mProgressDlg.dismiss();
                        mProgressDlg=null;
                    }
                    ToastUtil.Show(msg.obj.toString());
                }else if( msg.what == ImportExcelThread.ImportFalse) {
                    if(mProgressDlg!=null){
                        mProgressDlg.dismiss();
                        mProgressDlg=null;
                    }
                    ToastUtil.Show(msg.obj.toString());
                }
            }catch (Exception e) {
                ToastUtil.Show( "handlemessage出错了!");
            }
        }
    }
}
