package com.jxd.bookdistribution.thread;

import java.util.List;

import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Person;
import com.jxd.bookdistribution.util.SQLiteUitl;


import android.content.Context;
import android.os.Handler;
import android.os.Message;


public class UserListThread extends Thread {

		int _pageIdx = 0;
		boolean _isRefresh = false;
		Handler _handler = null;
		int PageSize = 50;
		public static final int MSG_Refresh = 1;
		public static final int MSG_Loaded = 2;
		public static final int MSG_Error = 0;
		Context _context = null;
		String _userName = "";

		public UserListThread(Context context, int pageIdx,
                              Handler handler,
				boolean isRefresh,
                String username) {

			_pageIdx = pageIdx;
			_isRefresh = isRefresh;
			_handler = handler;
			_context = context;

			_userName = username;
		}

		@Override
		public void run() {

			try {
				SQLiteUitl util = new SQLiteUitl(_context);
				List<Person> list = util.queryUserByPage(_pageIdx, PageSize, _userName);
				int count = util.queryUserCount(_userName);

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
