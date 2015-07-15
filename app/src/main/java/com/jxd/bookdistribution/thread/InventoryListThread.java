package com.jxd.bookdistribution.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.util.SQLiteUitl;

import java.util.List;

/**
 * Created by jinxiangdong on 2015/5/5.
 */
public class InventoryListThread extends Thread {
    Handler _handler = null;
    Context _context = null;
    public static final int MSG_Success = 41;
    public static final int MSG_Error = 40;
    Integer _siteid=0;

    public InventoryListThread(Context context, Handler handler,Integer siteid) {
        _handler = handler;
        _context = context;
        _siteid=siteid;
    }

    @Override
    public void run() {
        try {
            SQLiteUitl util = new SQLiteUitl(_context);
            List<Book> list = util.GetBooksOfSite(_siteid);
            Message msg = _handler.obtainMessage(MSG_Success);
            msg.obj = list;
            _handler.sendMessage(msg);

        } catch (Exception e) {
            Message msg = _handler.obtainMessage(MSG_Error);
            msg.obj = e.getMessage() == null ? "查询失败" : e.getMessage();
            _handler.sendMessage(msg);
            return;
        }
    }
}
