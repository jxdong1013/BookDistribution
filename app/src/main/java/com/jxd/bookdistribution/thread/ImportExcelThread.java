package com.jxd.bookdistribution.thread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.jxd.bookdistribution.app.BookApplication;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Constant;
import com.jxd.bookdistribution.util.SQLiteUitl;
import com.jxd.bookdistribution.util.ExcelUtil;
import com.jxd.bookdistribution.util.ToastUtil;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ImportExcelThread extends Thread {
	Context _context = null;
	String _path = "";
	Handler _handler = null;
	public static int ImportOk = 36;
	public static int ImportFalse = 37;

	public ImportExcelThread(Context context, Handler handler, String path) {
		_context = context;
		_handler = handler;
		_path = path;
	}

	@Override
	public void run() {

		try {
			LinkedList<Book> list = ParseExcelData(_path);
			if (list == null || list.size() < 1)
			{
				Message msg=_handler.obtainMessage(ImportFalse);
				msg.obj="导入失败，Excel文件中没有数据。";
				_handler.sendMessage(msg);
				return;
			}

			SQLiteUitl util = new SQLiteUitl(_context);
			int count = list.size();
			//for (int i = 0; i < count; i++) {
				//Book c = list.get(i);
				//if (biz.ExistContractByProjectNumAndProjectName(
				//		c.getProjectNo(), c.getProjectName())) {
				//	biz.UpdateByProjectNumAndProjectName(c);
				//} else {
				//	biz.Insert(list.get(i));
				//}
			//}
			int result = util.insertBooks(list);

			Message msg = _handler.obtainMessage(ImportOk);
			msg.obj = "导入" + result + "条记录";
			_handler.sendMessage(msg);

			super.run();
		} catch (Exception ex) {
			Message msg = _handler.obtainMessage(ImportFalse);
			msg.obj = "导入失败";
			_handler.sendMessage(msg);
		}
	}

	protected boolean CheckEmptyLine(List<String> line) {
		int count = line.size();
		boolean isEmpty = true;
		for (int i = 0; i < count; i++) {
			String temp = line.get(i);
			if (temp != null && temp.length() > 0) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;
	}

	/*
	 *
	 */
//	protected Boolean CheckRepeatBook(List<Contract> list, Contract contract) {
//		if (list == null || list.size() < 1)
//			return false;
//		for (Contract item : list) {
//			if (item.getSeq().equals(contract.getSeq())
//					&& item.getContractNo().equals(contract.getContractNo())
//					&& item.getProjectNo().equals(contract.getProjectNo()))
//				return true;
//		}
//		return false;
//	}

	protected LinkedList<Book> ParseExcelData(String path) {
		List<LinkedList<String>> data = ExcelUtil.ReadExcel(path, 0);
		if (data == null || data.size() <= 1) {
			ToastUtil.Show("没有数据!");
			return null;
		}

		int count = data.size();
		LinkedList<Book> books = new LinkedList<Book>();
		for (int i = 1; i < count; i++) {
			LinkedList<String> line = data.get(i);

			if (CheckEmptyLine(line))
				continue;

			Book book = new Book();
			int cols = line.size();
			for (int k = 0; k < cols; k++) {
				if (BookApplication.ExcelMap.containsKey(k)) {
					String name = BookApplication.ExcelMap.get(k);
					String value = line.get(k);
					try {
						// Field field =
						// contract.getClass().getDeclaredField(name);
						String methodName = "set"
								+ name.substring(0, 1).toUpperCase(Locale.getDefault())
								+ name.substring(1);
						try {
							Method method =null;
							if( name.equals("price")){
								method= book.getClass().getDeclaredMethod(methodName,
										new Class[]{BigDecimal.class});
								BigDecimal tempv = BigDecimal.ZERO;
								try{
									tempv=new BigDecimal(value);
								}catch (Exception ex){
									tempv=BigDecimal.ZERO;
								}
								method.invoke(book,tempv);
							}else {
								method = book.getClass()
										.getDeclaredMethod(methodName,
												new Class[]{String.class});
								method.invoke(book, value);
							}


						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			book.setBookId(0);
			book.setStatus(Constant.BookIn);
			book.setSiteId(0);

			//if (CheckRepeatContract(books, book))
			//	continue;
			books.add(book);
		}
		return books;
	}

}
