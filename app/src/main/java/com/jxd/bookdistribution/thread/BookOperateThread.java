package com.jxd.bookdistribution.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.util.SQLiteUitl;

import java.util.List;


public class BookOperateThread extends Thread {
	Handler _handler = null;
	public static final int MSG_Success = 31;
	public static final int MSG_Error = 30;
	Context _context = null;
	String _barcode = "";
	Integer _status = 0;
	Integer _siteId = 0;

	public BookOperateThread(Context context,
							 Handler handler,
							 String barcode,
							 Integer status,
							 Integer siteId) {
		_handler = handler;
		_context = context;
		_barcode = barcode;
		_status = status;
		_siteId = siteId;
	}

	@Override
	public void run() {
		try {

			SQLiteUitl util = new SQLiteUitl(_context);

			if(util.ExistBookByBarcode(_barcode)==false){
				Message msg =_handler.obtainMessage(MSG_Error);
				msg.obj="标签对应的图书不存在。";
				_handler.sendMessage(msg);
				return;
			}

			long result = util.UpdateBookByBarcode(_barcode, _status, _siteId);

			Book entity = util.getBookByBarcode(_barcode);

			Message msg = _handler.obtainMessage(MSG_Success);
			msg.obj=entity;
			_handler.sendMessage(msg);
		} catch (Exception e) {
			Message msg = _handler.obtainMessage(MSG_Error);
			msg.obj = e.getMessage() == null ? "操作失败" : e.getMessage();
			_handler.sendMessage(msg);
			return;
		}
	}

}
