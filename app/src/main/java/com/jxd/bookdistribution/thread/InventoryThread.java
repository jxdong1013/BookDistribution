package com.jxd.bookdistribution.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jxd.bookdistribution.bean.Inventory;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.util.SQLiteUitl;

import java.util.List;

/**
 * Created by jinxiangdong on 2015/5/5.
 */
public class InventoryThread extends Thread {
    Handler _handler = null;
    Context _context = null;
    public static final int MSG_Success = 1;
    public static final int MSG_Error = 0;

    public InventoryThread(Context context, Handler handler) {
        _handler = handler;
        _context = context;
    }

    @Override
    public void run() {
        try {
            SQLiteUitl util = new SQLiteUitl(_context);
            List<Inventory> list = util.queryInventorys();
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
