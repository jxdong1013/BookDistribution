package com.jxd.bookdistribution.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jxd.bookdistribution.bean.Log;
import com.jxd.bookdistribution.bean.Person;
import com.jxd.bookdistribution.util.SQLiteUitl;

import java.util.List;


public class LogListThread extends Thread {

		int _pageIdx = 0;
		boolean _isRefresh = false;
		Handler _handler = null;
		int PageSize = 50;
		public static final int MSG_Refresh = 1;
		public static final int MSG_Loaded = 2;
		public static final int MSG_Error = 0;
		Context _context = null;
		//String _userName = "";
		String _starttime="";
	String _endtime="";

		public LogListThread(Context context, int pageIdx,
							 Handler handler,
							 boolean isRefresh,
							 String starttime,
							 String endtime ) {

			_pageIdx = pageIdx;
			_isRefresh = isRefresh;
			_handler = handler;
			_context = context;

			_starttime = starttime;
			_endtime= endtime;
		}

		@Override
		public void run() {

			try {
				SQLiteUitl util = new SQLiteUitl(_context);
				List<Log> list = util.queryLogByPage(_pageIdx, PageSize, _starttime, _endtime);
				int count = util.queryLogCount(_starttime, _endtime);

				if (_isRefresh) {
					Message msg = _handler.obtainMessage(MSG_Refresh);
					msg.obj = list;
					msg.arg1=count;
					_handler.sendMessage(msg);
				} else {
					Message msg = _handler.obtainMessage(MSG_Loaded);
					msg.obj = list;
					msg.arg1 = count;
					_handler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = _handler.obtainMessage(MSG_Error);
				msg.obj = e.getMessage() == null ? "查询失败" : e.getMessage();
				msg.arg1=0;
				_handler.sendMessage(msg);
				return;
			}
		}

}
