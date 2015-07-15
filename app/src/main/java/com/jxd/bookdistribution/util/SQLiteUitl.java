package com.jxd.bookdistribution.util;

import java.math.BigDecimal;
import java.security.AllPermission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jxd.bookdistribution.app.BookApplication;
import com.jxd.bookdistribution.bean.Book;
import com.jxd.bookdistribution.bean.Inventory;
import com.jxd.bookdistribution.bean.Log;
import com.jxd.bookdistribution.bean.Person;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.bean.UnusualRegister;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteUitl extends SQLiteOpenHelper {
	public static int SQLITE_VERSION = 1;
	public static String DB_Name = "bookD";
	public static String Table_site = "t_site";
    public static String Table_user="t_user";
    public static String Table_book ="t_book";
	public static String Table_log = "t_log";
    public static String Table_inventory = "t_inventory";
	public static String Table_unusualregister="t_unusualregister";

	public SQLiteUitl(Context context, String name, int version) {
		super(context, name, null, version);
	}

	public SQLiteUitl(Context context) {
		super(context, DB_Name, null, SQLITE_VERSION);
	}

	public SQLiteUitl(Context context, String name) {
		super(context, name, null, SQLITE_VERSION);
	}

	public SQLiteUitl(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	protected void insertDemoSiteData( SQLiteDatabase db ){
		List<Site> sites = new ArrayList<>();
		Site entity=new Site();
		entity.setSiteId(1);
		entity.setSiteName("国家图书馆");
		entity.setTelephone("88544114");
		sites.add(entity);
		entity=new Site();
		entity.setSiteId(2);
		entity.setSiteName("首都图书馆");
		entity.setTelephone("67358114");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(3);
		entity.setSiteName("东城区图书馆");
		entity.setTelephone("64068861");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(4);
		entity.setSiteName("崇文区图书馆");
		entity.setTelephone("67123704");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(5);
		entity.setSiteName("朝阳区图书馆");
		entity.setTelephone("010-85994531");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(6);
		entity.setSiteName("安定门街道图书馆");
		entity.setTelephone("84091021");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(7);
		entity.setSiteName("东四街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(8);
		entity.setSiteName("东华门街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(9);
		entity.setSiteName("东直门街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(10);
		entity.setSiteName("交道口街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(11);
		entity.setSiteName("和平里街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(12);
		entity.setSiteName("东四街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(13);
		entity.setSiteName("建国门街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(14);
		entity.setSiteName("景山街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(15);
		entity.setSiteName("北新桥街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(16);
		entity.setSiteName("朝阳门街道图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(17);
		entity.setSiteName("丰盛社区图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(18);
		entity.setSiteName("西长安街分馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(19);
		entity.setSiteName("月坛分馆");
		entity.setTelephone("64054151");
		sites.add(entity);
		entity = new Site();
		entity.setSiteId(20);
		entity.setSiteName("二龙路社区图书馆");
		entity.setTelephone("64054151");
		sites.add(entity);

		insertSites( db, sites);

	}

	protected void insertSites(SQLiteDatabase db , List<Site> sites){
		String sql_site="insert into "+ Table_site+"( sitename,address,telephone,remark)"+
				"values(?,?,?,?)";
		for( Site site : sites ) {
			Object[] params = new Object[4];
			params[0] = site.getSiteName();
			params[1] = site.getAddress();
			params[2] = site.getTelephone();
			params[3] = site.getRemark();
			db.execSQL(sql_site, params);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

        String sql_user = "create table if not exists " + Table_user
                + " ( userid integer primary key autoincrement,"
                + "username text not null , password text not null, sex text, telephone text,address text,enable text,"
                + "modifytime timestamp not null default CURRENT_TIMESTAMP ,"
                + "createtime timestamp not null default CURRENT_TIMESTAMP )";
        db.execSQL(sql_user);

        String sql_insertUser =" insert into "+ Table_user+ " ( username , password , sex )"
                +"values('admin','123456','男')";
        db.execSQL(sql_insertUser);

        String sql_site = "create table if not exists " + Table_site + " ( "
                + "siteid integer primary key autoincrement,sitename text not null, address text ,telephone text,remark text ,"
                + "modifytime timestamp not null default CURRENT_TIMESTAMP ,"
                + "createtime timestamp not null default CURRENT_TIMESTAMP)";
        db.execSQL(sql_site);


		insertDemoSiteData(db);



        String sql_book = "create table if not exists " + Table_book + " ( "
                + "bookid integer primary key autoincrement, bookname text not null,author text,"
                + "publish text,isbn text,publishdate text,status integer default 0,barcode text,"
                + "price decimal(6,2) default 0, siteid integer default 0, remark text,"
                + "modifytime timestamp not null default URRENT_TIMESTAMP,"
                + "createtime timestamp not null default CURRENT_TIMESTAMP)";
        db.execSQL(sql_book);

        String sql_log =" create table if not exists "+ Table_log+" ( "
                +"logid integer primary key autoincrement , userid integer , username text,operatetype text,"
                +"content text , "
                + "modifytime timestamp not null default CURRENT_TIMESTAMP,"
                + "createtime timestamp not null default CURRENT_TIMESTAMP)";
        db.execSQL(sql_log);

        String sql_inventory=" create table if not exists " + Table_inventory +" ( "
                + " id integer primary key autoincrement , name text, siteid integer default 0,"
                + " sitename text,status integer default 0,"
                + " modifytime timestamp not null default CURRENT_TIMESTAMP,"
                + " createtime timestamp not null default CURRENT_TIMESTAMP)";
        db.execSQL(sql_inventory);

		String sql_unusualregister="create table if not exists "+Table_unusualregister + " ( "
				+" id integer primary key autoincrement ,"
				+" bookid integer default 0,"
				+" bookname text,"
				+" author text,"
				+" publish text, "
				+" publishdate text,"
				+" isbn text,"
				+" price text,"
				+" barcode text,"
				+" siteid integer default 0,"
				+" status integer default 0,"
				+" remark text,"
				+" userid integer default 0,"
				+" username text,"
				+" modifytime timestamp not null default CURRENT_TIMESTAMP,"
				+" createtime timestamp not null default CURRENT_TIMESTAMP)";

		db.execSQL(sql_unusualregister);


    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// String sql = "Alter Table " +Table_archive+
		// " add body text , audit text, attachment text,attachsheet text,attachdiagram text";

		// db.execSQL(sql);

	}

	public void close() {
		this.close();
	}

	public int insertBooks(List<Book> books) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		int total = 0;
		try {
			for (int i = 0; i < books.size(); i++) {
				Book book = books.get(i);
				ContentValues cv = new ContentValues();
				cv.put("bookname", book.getBookName());
				cv.put("author", book.getAuthor());
				cv.put("publish",book.getPublish());
				cv.put("isbn", book.getIsbn());
				cv.put("publishdate", book.getPublishDate());
				cv.put("status", book.getStatus());
				cv.put("barcode", book.getBarcode());
				cv.put("remark",book.getRemark());
                cv.put("price",book.getPrice().toString());
                cv.put("siteid",book.getSiteId());

				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dt = format.format(calendar.getTime());

				cv.put("modifytime",  dt );
				cv.put("createtime", dt );
				long result = db.insert(Table_book, null, cv);
				if( result > 0 ) {
					total ++;
				}

				addLog(db,"新增图书","");

			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
		} finally {
			db.endTransaction();
			db.close();
		}
	return total;
	}

	public int deleteAllBooks() {
		SQLiteDatabase db = this.getWritableDatabase();
		int result = db.delete(Table_book , null, null);

		addLog(db,"删除图书","");

		db.close();
		return result;
	}

	public int deleteBooksByIds( List<Integer> ids){
		if( ids == null || ids.size()<1) return 0;

		SQLiteDatabase db = this.getWritableDatabase();
	    String where = "bookid in ";
	    String idString = "";
	    for( int id : ids ){
	    	if( idString.length()>0){
	    		idString+=",";
	    	}
	    	idString +=String.valueOf( id );
	    }
	    where += " ( "+ idString+" ) ";
	    int result = db.delete(Table_book , where, null);

		addLog(db,"删除图书","");

	    db.close();
	    return result;
	}

	public int deleteBookById(Integer bookid){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "bookid=?";
		String[] values = { bookid.toString() };
		int result = db.delete(Table_book , where, values);

		addLog(db,"删除图书","");

		db.close();

		return result;
	}

	public long UpdateBookByBarcode(String barcode , Integer status ,Integer siteid){
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("status", status);
			cv.put("siteid",siteid);
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String modifytime = format.format(calendar.getTime());
			cv.put("modifytime", modifytime);

			String where = "barcode=?";
			String[] args = {barcode};

			long result = db.update(Table_book, cv, where, args);

			addLog(db,"更新图书","");

			return result;
		} catch (Exception e) {
			return 0;
		} finally {
			db.close();
		}
	}

	public boolean ExistBookByBarcode(String barcode){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = null;
		try {
			boolean isok = false;
			cur = db.query(Table_book , null, "barcode=?",
					new String[] { barcode }, null, null, null);
			if (cur == null || cur.getCount() < 1)
				isok = false;
			else {
				isok = true;
			}
			return isok;
		} catch (Exception ex) {
			return false;
		} finally {
			if (cur != null) {
				cur.close();
			}
			db.close();
		}
	}

	public Book getBookByBarcode(String barcode) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "select * from " + Table_book + " where  barcode ='" + barcode + "'";
		Book entity = null;
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null || cur.getCount() < 1) {
		} else {
			cur.moveToFirst();
			entity = getBook(cur);
		}
		if(cur!=null){
			cur.close();
		}
		db.close();
		return entity;
	}

	public int queryBookCount(String bookname,
							  String author,
							  String publish,
							  String isbn ,
							  String publishDate ,
							  String barcode) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "select count(bookid) from " + Table_book + " where 1=1 ";

		String where = "";
		if (bookname != null && bookname.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += "  bookname ='" + bookname + "'";
		}
		if ( author != null && author.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += " author ='" + author + "'";
		}
		if ( isbn != null && isbn.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += " isbn ='" + isbn + "'";
		}
        if ( publish != null && publish.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += " publish ='" + publish + "'";
        }
        if ( publishDate != null && publishDate.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += " publishdate ='" + publishDate + "'";
        }
		if ( barcode != null && barcode.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += " barcode ='" + barcode + "'";
		}

		if (where.length() > 0) {
			sql += " and ( " + where + " ) ";
		}
		int count = 0;
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null || cur.getCount() < 1) {
			count = 0;
		} else {
			cur.moveToFirst();
			count = cur.getInt(0);
		}
		if (cur != null) {
			cur.close();
		}
		db.close();
		return count;
	}

	public List<Book> queryBookByPage( Integer pageId,
									   int pageSize,
									   String bookName,
									   String author ,
									   String isbn,
									   String publish ,
									   String publishDate,
									   String barcode ) {

        List<Book> list = null;
        SQLiteDatabase db = this.getWritableDatabase();
        int lines = pageId * pageSize;
        String sql = "select * from " + Table_book + " where 1=1 ";

        String where = "";
        if (bookName != null && bookName.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += "  bookname ='" + bookName + "'";
        }
        if (author != null && author.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += " author ='" + author + "'";
        }
        if (isbn != null && isbn.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += " isbn ='" + isbn + "'";
        }
        if (publish != null && publish.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += " publish ='" + publish + "'";
        }
        if (publishDate != null && publishDate.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += " publishdate ='" + publishDate + "'";
        }
		if (barcode != null && barcode.length() > 0) {

			if (where.length() > 0) {
				where += " or ";
			}
			where += " barcode ='" + barcode + "'";
		}

        if (where.length() > 0) {
            sql += " and ( " + where + " ) ";
        }

        sql += " order by modifytime desc ";
        sql += " limit " + pageSize + " offset " + lines;

        Cursor cur = db.rawQuery(sql, null);
        if (cur == null || cur.getCount() < 1) {
            list = null;
        } else {
            list = new ArrayList<Book>();
            cur.moveToFirst();
            do {
                Book entity = new Book();
                entity.setBookId(cur.getInt(cur.getColumnIndex("bookid")));
                entity.setBookName(cur.getString(cur.getColumnIndex("bookname")));
                entity.setAuthor(cur.getString(cur.getColumnIndex("author")));
                entity.setPublish(cur.getString(cur.getColumnIndex("publish")));
                entity.setIsbn(cur.getString(cur.getColumnIndex("isbn")));
                entity.setPublishDate(cur.getString(cur.getColumnIndex("publishdate")));
                entity.setStatus(cur.getInt(cur.getColumnIndex("status")));
                entity.setBarcode(cur.getString(cur.getColumnIndex("barcode")));
                entity.setRemark(cur.getString(cur.getColumnIndex("remark")));

                String priceStr = cur.getString(cur.getColumnIndex("price"));
                BigDecimal price = new BigDecimal(priceStr);
                entity.setPrice(price);

                entity.setSiteId( cur.getInt(cur.getColumnIndex("siteid")));

                entity.setModifytime(cur.getString(cur.getColumnIndex("modifytime")));
                entity.setCreatetime(cur.getString(cur.getColumnIndex("createtime")));
                list.add(entity);
            } while (cur.moveToNext());
        }
        cur.close();

		addLog(db, "查询图书", "");

        db.close();
        return list;
    }

//	protected String GetContractQueryWhereSql( ContractQueryForm queryForm){
//		String wheresql = " where 1=1 ";
//		String where = "";
//		if (  queryForm.getSeq()  != null && queryForm.getSeq().length() > 0) {
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += "  seq like '%" + queryForm.getSeq() + "%'";
//		}
//		if (  queryForm.getContractNo()  != null && queryForm.getContractNo().length() > 0) {
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += "  contractnum like '%" + queryForm.getContractNo() + "%'";
//		}
//
//		if (  queryForm.getProjectNo()  != null && queryForm.getProjectNo().length() > 0) {
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += "  projectnum like '%" + queryForm.getProjectNo() + "%'";
//		}
//
//		if ( queryForm.getProjectName() != null && queryForm.getProjectName().length() > 0) {
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += " projectname like '%" + queryForm.getProjectName() + "%'";
//		}
//		if ( queryForm.getContractRFID() != null && queryForm.getContractRFID().length() > 0) {
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += " contractrfid ='" + queryForm.getContractRFID() + "'";
//		}
//		if( queryForm.getbCompany() != null && queryForm.getbCompany().length()>0){
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += " bcompany like '%" + queryForm.getbCompany() + "%'";
//		}
//		if( queryForm.getContractPlace() != null && queryForm.getContractPlace().length()>0){
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += " contractplace ='" + queryForm.getContractPlace() + "'";
//		}
//		if( queryForm.getMoney() != null && queryForm.getMoney().length()>0){
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += " money like '%" + queryForm.getMoney() + "%'";
//		}
//		if( queryForm.getContractState() != null && queryForm.getContractState().length()>0
//				&& queryForm.getContractState().equals("0") == false ){
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where += " contractstate ='" + queryForm.getContractState() + "'";
//		}
//		if( queryForm.getOtherCondition()!=null && queryForm.getOtherCondition().length() >0){
//			if (where.length() > 0) {
//				where += " and ";
//			}
//			where +=queryForm.getOtherCondition();
//		}
//
//		if (where.length() > 0) {
//			wheresql += " and ( " + where + " ) ";
//		}
//		return wheresql;
//	}

//	public List<Contract> QueryContractByPage( ContractQueryForm queryForm) {
//		List<Contract> list = null;
//		String sql = "select * from " + Table_contract ;
//		int lines = queryForm.getPageidx()  * queryForm.getPageSize();
//		sql += GetContractQueryWhereSql(queryForm);
//		sql += " order by modifytime desc ";
//		sql += " limit " + queryForm.getPageSize() + " offset " + lines;
//
//		Cursor cur = null;
//		SQLiteDatabase db = this.getWritableDatabase();
//		try{
//			cur = db.rawQuery(sql, null);
//			if (cur == null || cur.getCount() < 1) {
//				list = null;
//			} else {
//			list = new ArrayList<Contract>();
//			cur.moveToFirst();
//			do {
//				Contract entity = GetContract(cur);
//				list.add(entity);
//			} while (cur.moveToNext());
//		}
//		return list;
//		}
//		catch (Exception e) {
//			return null;
//		}
//		finally{
//			if( cur!=null){
//				cur.close();
//			}
//			db.close();
//		}
//	}

//	public int QueryCount( ContractQueryForm queryForm ) {
//		String sql = "select count(contractid) from " + Table_contract ;
//		sql += GetContractQueryWhereSql(queryForm);
//
//		int count = 0;
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur =null;
//		try{
//			cur =db.rawQuery(sql, null);
//			if (cur == null || cur.getCount() < 1) {
//				count = 0;
//			} else {
//				cur.moveToFirst();
//				count = cur.getInt(0);
//			}
//			return count;
//		}catch(Exception ex){
//			return -1;
//		}
//		finally{
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

//	public long UpdateContractByProjectNumAndProjectName( Contract contract){
//		SQLiteDatabase db = this.getWritableDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//
//			cv.put("seq", contract.getSeq());
//			cv.put("contractnum", contract.getContractNo());
//			cv.put("projectnum", contract.getProjectNo());
//			//cv.put("contractname", contract.getContractName());
//			cv.put("projectName",contract.getProjectName());
//			cv.put("projectmanager", contract.getProjectManager());
//			cv.put("tel",contract.getTel());
//			cv.put("linker",contract.getLinker());
//			cv.put("depart",contract.getDepart());
//			cv.put("paymethod",contract.getPaymethod());
//			cv.put("money",contract.getMoney());
//			cv.put("bcompany", contract.getBCompany());
//			cv.put("signingdate",contract.getSigningDate());
//
//			cv.put("unshelve",contract.getUnShelve());
//			//cv.put("contractstate", contract.getContractState());
//			//cv.put("contractplace", contract.getContractPlace());
//
//			//cv.put("contractrfid", contract.getContractRFID());
//			cv.put("operator", contract.getOperator());
//
//			Calendar calendar = Calendar.getInstance();
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String modifytime = format.format(calendar.getTime());
//			cv.put("modifytime", modifytime );
//			//cv.put("createtime", contract.getCreatetime());
//
//			String where = "projectnum =? and projectname=?";
//			String[] args = { contract.getProjectNo().toString() , contract.getProjectName() };
//
//			long result = db.update(Table_contract , cv, where, args);
//			return result;
//		} catch (Exception e) {
//			return 0;
//		} finally {
//			db.close();
//		}
//	}

	public long updateBook (Book book ) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("bookname", book.getBookName());
            cv.put("author", book.getAuthor());
            cv.put("publish", book.getPublish());
            cv.put("isbn", book.getIsbn());
            cv.put("publishdate", book.getPublishDate());
            cv.put("status", book.getStatus());
            cv.put("price", book.getPrice().toString());
            cv.put("remark", book.getRemark());
            cv.put("barcode",book.getBarcode());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String modifytime = format.format(calendar.getTime());
            cv.put("modifytime", modifytime);

            cv.put("price", book.getPrice().toString());
            cv.put("siteid", book.getSiteId());

            String where = "bookid=?";
            String[] args = {book.getBookId().toString()};

            long result = db.update(Table_book, cv, where, args);

			addLog(db,"更新图书","");

            return result;
        } catch (Exception e) {
            return 0;
        } finally {
            db.close();
        }
    }

//	public boolean ExistBookByContractNum(String contractNum) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			boolean isok = false;
//			cur = db.query(Table_contract , null, "contractnum=?",
//					new String[] { contractNum }, null, null, null);
//			if (cur == null || cur.getCount() < 1)
//				isok = false;
//			else {
//				isok = true;
//			}
//			return isok;
//		} catch (Exception ex) {
//			return false;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

//	public boolean ExistContractByContractNumNotContractId( String contractNum , Integer contractId){
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			boolean isok = false;
//			cur = db.query(Table_contract , null, "contractnum=? and contractid != ?",
//					new String[] { contractNum , contractId.toString() }, null, null, null);
//			if (cur == null || cur.getCount() < 1)
//				isok = false;
//			else {
//				isok = true;
//			}
//			return isok;
//		} catch (Exception ex) {
//			return false;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

//	public boolean ExistContractByProjectNumAndProjectNameNotContrainSelf(
//			String projectNum,	String projectName , Integer contractId ){
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			boolean isok = false;
//			cur = db.query(Table_contract , null, "projectnum=? and projectname=? and contractid != ?",
//					new String[] { projectNum , projectName , contractId.toString() }, null, null, null);
//			if (cur == null || cur.getCount() < 1)
//				isok = false;
//			else {
//				isok = true;
//			}
//			return isok;
//		} catch (Exception ex) {
//			return false;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

//	public boolean ExistContractByProjectNumAndProjectName(String projectNum , String projectName){
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			boolean isok = false;
//			cur = db.query(Table_contract , null, "projectnum=? and projectname =?",
//					new String[] { projectNum, projectName  }, null, null, null);
//			if (cur == null || cur.getCount() < 1)
//				isok = false;
//			else {
//				isok = true;
//			}
//			return isok;
//		} catch (Exception ex) {
//			return false;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

//
//	public boolean ExistContractByContractNumAndContractName(String contractNum,
//			String contractName) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			boolean isok = false;
//			cur = db.query(Table_contract , null, "contractnum=? and contractname =?",
//					new String[] { contractNum, contractName }, null, null, null);
//			if (cur == null || cur.getCount() < 1)
//				isok = false;
//			else {
//				isok = true;
//			}
//			return isok;
//		} catch (Exception ex) {
//			return false;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

//	public boolean ExistByUID(String uid, String operatetime) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			boolean isok = false;
//			String where = "contractrfid=?";
//			if (operatetime.isEmpty() == false) {
//				where += " and modifytime>='" + operatetime + "'";
//			}
//			cur = db.query(Table_contract , null, where, new String[] { uid }, null, null, null);
//			if (cur == null || cur.getCount() < 1) {
//				isok = false;
//			} else {
//				isok = true;
//			}
//			return isok;
//		} catch (Exception ex) {
//			return false;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

	protected Book getBook(Cursor cur){
        Book entity = new Book();
		entity.setBookId(cur.getInt(cur.getColumnIndex("bookid")));
		entity.setBookName(cur.getString(cur.getColumnIndex("bookname")));
		entity.setAuthor(cur.getString(cur.getColumnIndex("author")));
		entity.setPublish(cur.getString(cur.getColumnIndex("publish")));
		entity.setIsbn(cur.getString(cur.getColumnIndex("isbn")));
		entity.setPublishDate(cur.getString(cur.getColumnIndex("publishdate")));

        String priceStr = cur.getString(cur.getColumnIndex("price"));
        BigDecimal price = new BigDecimal(priceStr);
        entity.setPrice( price);

		entity.setStatus(cur.getInt(cur.getColumnIndex("status")));
		entity.setBarcode(cur.getString(cur.getColumnIndex("barcode")));
		entity.setRemark(cur.getString(cur.getColumnIndex("remark")));

        entity.setSiteId(cur.getInt(cur.getColumnIndex("siteid")));

		entity.setModifytime(cur.getString(cur.getColumnIndex("modifytime")));
		entity.setCreatetime(cur.getString(cur.getColumnIndex("createtime")));
		return entity;
	}

//	public Contract GetContractByRFID(String rfid){
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			String where = "contractrfid=?";
//			cur = db.query(Table_contract , null, where, new String[] { rfid }, null, null, null);
//			if (cur == null || cur.getCount() < 1) {
//				return null;
//			} else {
//				cur.moveToFirst();
//				Contract entity =GetContract(cur);
//				return entity;
//			}
//		} catch (Exception ex) {
//			return null;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}

//	public Contract GetNextContract( String contractId , String modifyTime  , String otherCondition ){
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cur = null;
//		try {
//			String where = " contractid <> ? and modifytime <= ? ";
//			if( otherCondition!=null && otherCondition.length()>0 ){
//				where +=" and  ( " +otherCondition + " )";
//			}
//			String orderBy ="modifytime desc";
//			String limit = "1";
//			cur = db.query(Table_contract , null, where, new String[] { contractId ,  modifyTime }, null, null, orderBy , limit);
//			if (cur == null || cur.getCount() < 1) {
//				return null;
//			} else {
//				cur.moveToFirst();
//				Contract entity =GetContract(cur);
//				return entity;
//			}
//		} catch (Exception ex) {
//			return null;
//		} finally {
//			if (cur != null) {
//				cur.close();
//			}
//			db.close();
//		}
//	}


    public int insertSites( List<Site> sites){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        int total = 0;
        try {
            for (int i = 0; i < sites.size(); i++) {
                Site site = sites.get(i);
                ContentValues cv = new ContentValues();
                cv.put("siteid", site.getSiteId());
                cv.put("sitename", site.getSiteName());
				cv.put("address",site.getAddress());
				cv.put("telephone",site.getTelephone());
                cv.put("remark",site.getRemark());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dt = format.format(calendar.getTime());

                cv.put("modifytime",  dt );
                cv.put("createtime", dt );
                long result = db.insert(Table_site, null, cv);
                total += result;

				addLog(db,"新增站点","");
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
            db.close();
        }
        return total;
    }

    public long UpdateSite( Site site ) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();

            cv.put("sitename", site.getSiteName());
			cv.put("address",site.getAddress());
			cv.put("telephone",site.getTelephone());
            cv.put("remark", site.getRemark());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String modifytime = format.format(calendar.getTime());
            cv.put("modifytime", modifytime);

            String where = "siteid=?";
            String[] args = {site.getSiteId().toString()};

            long result = db.update(Table_site, cv, where, args);
            return result;
        } catch (Exception e) {
            return 0;
        } finally {
            db.close();
        }
    }

    public List<Site> querySites(){
        List<Site> list = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + Table_site + " where 1=1 ";

        sql += " order by modifytime desc ";

        Cursor cur = db.rawQuery(sql, null);
        if (cur == null || cur.getCount() < 1) {
            list = null;
        } else {
            list = new ArrayList<Site>();
            cur.moveToFirst();
            do {
                Site entity = new Site();
                entity.setSiteId(cur.getInt(cur.getColumnIndex("siteid")));
                entity.setSiteName(cur.getString(cur.getColumnIndex("sitename")));
				entity.setRemark(cur.getString(cur.getColumnIndex("remark")));
				entity.setAddress(cur.getString(cur.getColumnIndex("address")));
				entity.setTelephone(cur.getString(cur.getColumnIndex("telephone")));
                entity.setModifyTime(cur.getString(cur.getColumnIndex("modifytime")));
                entity.setCreateTime(cur.getString(cur.getColumnIndex("createtime")));
                list.add(entity);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return list;
    }


    public List<Person> queryUserByPage( Integer pageId, int pageSize, String userName) {

        List<Person> list = null;
        SQLiteDatabase db = this.getWritableDatabase();
        int lines = pageId * pageSize;
        String sql = "select * from " + Table_user + " where 1=1 ";

        String where = "";
        if (userName != null && userName.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += "  username ='" + userName + "'";
        }


        if (where.length() > 0) {
            sql += " and ( " + where + " ) ";
        }

        sql += " order by modifytime desc ";
        sql += " limit " + pageSize + " offset " + lines;

        Cursor cur = db.rawQuery(sql, null);
        if (cur == null || cur.getCount() < 1) {
            list = null;
        } else {
            list = new ArrayList<Person>();
            cur.moveToFirst();
            do {
                Person entity = new Person();
                entity.setUserId(cur.getInt(cur.getColumnIndex("userid")));
                entity.setUsername(cur.getString(cur.getColumnIndex("username")));
                entity.setTelephone(cur.getString(cur.getColumnIndex("telephone")));
                entity.setSex(cur.getString(cur.getColumnIndex("sex")));
                entity.setAddress(cur.getString(cur.getColumnIndex("address")));
                entity.setEnable(cur.getString(cur.getColumnIndex("enable")));

                entity.setModifytime(cur.getString(cur.getColumnIndex("modifytime")));
                entity.setCreatetime(cur.getString(cur.getColumnIndex("createtime")));
                list.add(entity);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return list;
    }

    public int queryUserCount(String username ) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select count(userid) from " + Table_user + " where 1=1 ";

        String where = "";
        if (username != null && username.length() > 0) {
            if (where.length() > 0) {
                where += " or ";
            }
            where += "  username ='" + username + "'";
        }

        if (where.length() > 0) {
            sql += " and ( " + where + " ) ";
        }
        int count = 0;
        Cursor cur = db.rawQuery(sql, null);
        if (cur == null || cur.getCount() < 1) {
            count = 0;
        } else {
            cur.moveToFirst();
            count = cur.getInt(0);
        }
        if (cur != null) {
            cur.close();
        }
        db.close();
        return count;
    }

    public int insertUser( List<Person> users){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        int total = 0;
        try {
            for (int i = 0; i < users.size(); i++) {
                Person user = users.get(i);
                ContentValues cv = new ContentValues();
                cv.put("username", user.getUsername());
                cv.put("telephone", user.getTelephone());
                cv.put("sex",user.getSex());
                cv.put("enable",user.getEnable());
                cv.put("password",user.getPassword());
                cv.put("address",user.getAddress());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dt = format.format(calendar.getTime());

                cv.put("modifytime",  dt );
                cv.put("createtime", dt );
                long result = db.insert(Table_user , null, cv);
                total += result;

				addLog(db,"新增用户","");

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
            db.close();
        }
        return total;
    }

    public List<Book> GetBooksOfSite(int siteId){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + Table_book + " where siteid= "+siteId;
        List<Book> list = null;
        Cursor cur = db.rawQuery(sql, null);
        if (cur == null || cur.getCount() < 1) {
            list = null;
        } else {
            list = new ArrayList<Book>();
            cur.moveToFirst();
            do {
                Book entity = new Book();
                entity.setAuthor(cur.getString(cur.getColumnIndex("author")));
                entity.setBarcode(cur.getString(cur.getColumnIndex("barcode")));
                entity.setBookId(cur.getInt(cur.getColumnIndex("bookid")));
                entity.setBookName(cur.getString(cur.getColumnIndex("bookname")));
                entity.setCreatetime(cur.getString(cur.getColumnIndex("createtime")));
                entity.setIsbn(cur.getString(cur.getColumnIndex("isbn")));
                entity.setModifytime(cur.getString(cur.getColumnIndex("modifytime")));

                String priceStr = cur.getString(cur.getColumnIndex("price"));
                BigDecimal price = new BigDecimal(priceStr);
                entity.setPrice( price);

                entity.setPublish(cur.getString(cur.getColumnIndex("publish")));
                entity.setPublishDate(cur.getString(cur.getColumnIndex("publishdate")));
                entity.setRemark(cur.getString(cur.getColumnIndex("remark")));
                entity.setSiteId(cur.getInt(cur.getColumnIndex("siteid")));
                entity.setStatus(cur.getInt(cur.getColumnIndex("status")));
                list.add(entity);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return list;
    }


    public long UpdateBookSiteId(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();

            cv.put("siteid", book.getSiteId());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String modifytime = format.format(calendar.getTime());
            cv.put("modifytime", modifytime);

            String where = "barcode=?";
            String[] args = {book.getBarcode()};

            long result = db.update(Table_book, cv, where, args);
            return result;
        } catch (Exception e) {
            return 0;
        } finally {
            db.close();
        }
    }

    public int addInventory(List<Inventory> list ){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        int total = 0;
        try {
            for (int i = 0; i < list.size(); i++) {
                Inventory user = list.get(i);
                ContentValues cv = new ContentValues();
                cv.put("name", user.getTaskName());
                cv.put("siteid", user.getSiteId());
                cv.put("sitename",user.getSiteName());
                cv.put("status",user.getStatus());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dt = format.format(calendar.getTime());

                cv.put("modifytime",  dt );
                cv.put("createtime", dt );
                long result = db.insert(Table_inventory , null, cv);
                total += result;

				addLog(db,"新增盘点","");
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
            db.close();
        }
        return total;
    }


    public List<Inventory> queryInventorys(){
        List<Inventory> list = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + Table_inventory + " where 1=1 ";

        sql += " order by modifytime desc ";

        Cursor cur = db.rawQuery(sql, null);
        if (cur == null || cur.getCount() < 1) {
            list = null;
        } else {
            list = new ArrayList<Inventory>();
            cur.moveToFirst();
            do {
                Inventory entity = new Inventory();
                entity.setSiteId(cur.getInt(cur.getColumnIndex("siteid")));
                entity.setSiteName(cur.getString(cur.getColumnIndex("sitename")));
                entity.setStatus(cur.getInt(cur.getColumnIndex("status")));
                entity.setId(cur.getInt(cur.getColumnIndex("id")));
                entity.setTaskName(cur.getString(cur.getColumnIndex("name")));
                entity.setModifytime(cur.getString(cur.getColumnIndex("modifytime")));
                entity.setCreatetime(cur.getString(cur.getColumnIndex("createtime")));
                list.add(entity);

            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return list;
    }

	public int insertUnusualRegister( List<UnusualRegister> list ){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		int total = 0;
		try {
			for (int i = 0; i < list.size(); i++) {
				UnusualRegister entity = list.get(i);
				ContentValues cv = new ContentValues();
				cv.put("bookname", entity.getBookName());
				cv.put("author", entity.getAuthor());
				cv.put("barcode",entity.getBarcode());
				cv.put("bookid",entity.getBookId());
				cv.put("isbn",entity.getIsbn());
				cv.put("price",entity.getPrice());
				cv.put("publish",entity.getPublish());
				cv.put("publishdate",entity.getPublishDate());
				cv.put("remark",entity.getRemark());
				cv.put("siteid",entity.getSiteId());
				cv.put("status",entity.getStatus());
				cv.put("userid",entity.getUserid());
				cv.put("username",entity.getUsername());

				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dt = format.format(calendar.getTime());

				cv.put("modifytime",  dt );
				cv.put("createtime", dt );
				long result = db.insert(Table_unusualregister , null, cv);
				total += result;

				addLog( db, "异常登记","");
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
		} finally {
			db.endTransaction();
			db.close();
		}
		return total;
	}

	public long addLog(String operateType ,String content){
		SQLiteDatabase db = this.getWritableDatabase();
		return addLog(db,operateType,content);
	}

	public long addLog( SQLiteDatabase db , String operateType , String content ) {
		Integer userid = BookApplication.mApp.mAccount.getUserId();
		String username = BookApplication.mApp.mAccount.getUsername();

		long total = 0;

		ContentValues cv = new ContentValues();
		cv.put("userid", userid);
		cv.put("username", username);
		cv.put("operatetype", operateType);
		cv.put("content", content);
		total = db.insert(Table_log, null, cv);


		return total;
	}


	public List<Log> queryLogByPage( Integer pageId, int pageSize, String starttime , String endtime ) {

		List<Log> list = null;
		SQLiteDatabase db = this.getWritableDatabase();
		int lines = pageId * pageSize;
		String sql = "select * from " + Table_log + " where 1=1 ";

		String where = "";
		if ( starttime != null && starttime.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += "  createtime  >='" +starttime + "'";
		}
		if ( endtime != null && endtime.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += "  createtime  <='" +endtime + "'";
		}

		if (where.length() > 0) {
			sql += " and ( " + where + " ) ";
		}

		sql += " order by modifytime desc ";
		sql += " limit " + pageSize + " offset " + lines;

		Cursor cur = db.rawQuery(sql, null);
		if (cur == null || cur.getCount() < 1) {
			list = null;
		} else {
			list = new ArrayList<Log>();
			cur.moveToFirst();
			do {
				Log entity = new Log();
				entity.setId(cur.getInt(cur.getColumnIndex("logid")));
				entity.setUserid(cur.getInt(cur.getColumnIndex("userid")));
				entity.setUsername(cur.getString(cur.getColumnIndex("username")));
				entity.setOperatetype(cur.getString(cur.getColumnIndex("operatetype")));
				entity.setContent(cur.getString(cur.getColumnIndex("content")));
				entity.setModifytime(cur.getString(cur.getColumnIndex("modifytime")));
				entity.setCreatetime(cur.getString(cur.getColumnIndex("createtime")));
				list.add(entity);
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public int queryLogCount(String starttime,String endtime ) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "select count(logid) from " + Table_log + " where 1=1 ";

		String where = "";
		if ( starttime != null && starttime.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += "  createtime  >='" +starttime + "'";
		}
		if ( endtime != null && endtime.length() > 0) {
			if (where.length() > 0) {
				where += " or ";
			}
			where += "  createtime  <='" +endtime + "'";
		}


		if (where.length() > 0) {
			sql += " and ( " + where + " ) ";
		}
		int count = 0;
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null || cur.getCount() < 1) {
			count = 0;
		} else {
			cur.moveToFirst();
			count = cur.getInt(0);
		}
		if (cur != null) {
			cur.close();
		}
		db.close();
		return count;
	}


}
