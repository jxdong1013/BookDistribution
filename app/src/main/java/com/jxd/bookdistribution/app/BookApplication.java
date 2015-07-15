package com.jxd.bookdistribution.app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.bean.Menu;
import com.jxd.bookdistribution.bean.Person;
//import com.jxd.bookdistribution.util.NFCUtil;
import com.jxd.bookdistribution.util.PreferencesUtils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author 向东
 *
 */
public class BookApplication extends Application{
	public static BookApplication mApp;
	public static int CurrentVersionCode=0;
	public static String CurrentVersioname="";
	public static String SessionId = "";
	public Person mAccount=null;
	public List<Menu> LeftMenuList=null;
	public static LinkedHashMap<Integer, String> ExcelMap=null;
	public static String CookieId="cid";
	public static String CookieValue = "";
	public static String Host="";
	//public static String Port="80";

	/**
	 *
	 */
	public String mPassword="";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mApp = this;
		GetVersionInfo();

		Host = PreferencesUtils.getString(mApp, "Url");
		if( Host ==null || Host.isEmpty() || Host =="" )
		{
			Host="http://htgl.bistu.edu.cn/contractWeb/";
		}

		//Port=PreferencesUtils.getString(mApp, "Port");

		InitMenu();

		InitExcelMaps();
	}


	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		//NFCUtil.Close();
		super.onTerminate();
	}
	/*
	 * 获得APK的版本信息
	 */
	protected void GetVersionInfo(){
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo pkgInfo = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			CurrentVersionCode = pkgInfo.versionCode;
			CurrentVersioname = pkgInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void InitMenu() {
        LeftMenuList = new ArrayList<Menu>();
        Menu m = new Menu(R.string.menuSite, this.getString(R.string.menuSite), R.drawable.navigation_site, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuBook, this.getString(R.string.menuBook), R.drawable.navigation_book, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuBookQuery, this.getString(R.string.menuBookQuery), R.drawable.navigation_bookquery, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuBookOperate, this.getString(R.string.menuBookOperate), R.drawable.navigation_bookoperate, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuInventory, this.getString(R.string.menuInventory), R.drawable.navigation_inventory, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuBookExport, this.getString(R.string.menuBookExport), R.drawable.navigation_bookexport, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuUnusualRegister, this.getString(R.string.menuUnusualRegister), R.drawable.navigation_unusalregister, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuUser, this.getString(R.string.menuUser), R.drawable.navigation_user, false);
        LeftMenuList.add(m);
        m = new Menu(R.string.menuLog, this.getString(R.string.menuLog), R.drawable.navigation_log, false);
		LeftMenuList.add(m);
		m = new Menu(R.string.menuImportBook, this.getString(R.string.menuImportBook), R.drawable.navigation_import, false);
		LeftMenuList.add(m);
    }

	protected void InitExcelMaps(){
		ExcelMap = new LinkedHashMap<Integer, String>();

		ExcelMap.put(0, "bookName" );
		ExcelMap.put(1, "author");
		ExcelMap.put(2, "publish");
		ExcelMap.put(3, "isbn");
		ExcelMap.put(4, "publishDate");
		ExcelMap.put(5, "price");
		ExcelMap.put(6, "barcode");
	}
}
