package com.jxd.bookdistribution.thread;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.util.ExcelUtil;
import com.jxd.bookdistribution.util.SQLiteUitl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by 向东 on 2015/5/5.
 */
public class ExportExcelThread extends Thread{
    public static final int MsgExportOK = 12;
    public static final int MsgExportFalse = 13;
    private Context mContext = null;
    private Handler mHandler = null;
    private String mSiteName="";
    private Integer mSiteId=0;

    public ExportExcelThread(Context context, Handler handler, Integer siteId , String siteName) {
        mContext = context;
        mHandler = handler;
        mSiteId=siteId;
        mSiteName = siteName;
    }

    /**
     *
     * 在SD卡上创建文件
     *
     *
     *
     * @throws IOException
     */
    public String getSDFilePath(  String fileName ) throws IOException {

        String SDPATH = Environment.getExternalStorageDirectory().getPath();
        String SDFolder = "书籍数据文件";
        File file = new File(SDPATH + "/"+ SDFolder  );

        if( file.exists() == false && file.isDirectory() == false){
            file.mkdirs();
        }

        String path = SDPATH+"/"+ SDFolder+"/"+fileName;
        //file  =new File(SDPATH+"/"+ SDFolder+"/"+fileName);
        //if (!file.exists()) {
        //    file.createNewFile();
        //}
        return path;
    }

    protected LinkedList<String> getHeader() {
        LinkedList<String> maps = new LinkedList<String>();
	    maps.add("书籍名称");
	    maps.add("作者");
	    maps.add("出版社");
	    maps.add("ISBN");
	    maps.add("出版时间");
	    maps.add("价格");
        maps.add("标签");
	    return maps;
    }

    protected LinkedList<String> getFields() {
        LinkedList<String> maps = new LinkedList<String>();
        maps.add("bookName");
        maps.add("author");
        maps.add("publish");
        maps.add("isbn");
        maps.add("publishDate");
        maps.add("price");
        maps.add("barcode");
        return maps;
    }

    @Override
    public void run() {
        SQLiteUitl util = new SQLiteUitl(mContext);
        try {
            String filename = "";
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHHmmss");
            filename = mSiteName + format.format(currentTime) +".xls";
            List books = util.GetBooksOfSite( mSiteId );
            Boolean result = true;
            if( books == null || books.size() < 1 ) result=false;
            Message msg = result ? mHandler.obtainMessage(MsgExportOK)
                    : mHandler.obtainMessage(MsgExportFalse);
            String path = "";
            if( result){


                LinkedList<String> header = getHeader();
                LinkedList<String> fields= getFields();
                path = getSDFilePath(filename);

                ExcelUtil.WriteExcelData(path, books,fields, header);

                util.addLog( "导出数据","");
            }

            msg.obj = result ? "导出文件" + path : "无数据导出";
            mHandler.sendMessage(msg);
        } catch (IOException e) {
            Message msg = mHandler.obtainMessage(MsgExportFalse);
            msg.obj = e.getMessage();
            mHandler.sendMessage(msg);
            e.printStackTrace();
        }
    }
}
