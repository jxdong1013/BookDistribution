package com.jxd.bookdistribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.GridViewAdapter;
import com.jxd.bookdistribution.app.BookApplication;


public class MainActivity extends Activity {
    GridView mGv =null;
    GridViewAdapter mAdapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGv = (GridView)this.findViewById(R.id.gridViewMenu);
        mAdapter = new GridViewAdapter(this );
        mGv.setAdapter(mAdapter);
        mGv.setOnItemClickListener( new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int arg2 , long l) {
                //String title = BookApplication.mApp.LeftMenuList.get(arg2).getMenuName();
                Integer tid= BookApplication.mApp.LeftMenuList.get(arg2).getMenuId();

                if(tid.equals( R.string.menuSite)){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SiteActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if(tid.equals( R.string.menuBook)){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, BookActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals( R.string.menuBookQuery)){
                    Intent intent =new Intent();
                    intent.setClass(MainActivity.this,BookQueryActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals(R.string.menuBookOperate)){
                    Intent intent =new Intent();
                    intent.setClass(MainActivity.this,BookOperateActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals( R.string.menuUser )){
                    Intent intent =new Intent();
                    intent.setClass(MainActivity.this,UserActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals( R.string.menuUnusualRegister)){
                    Intent intent =new Intent();
                    intent.setClass(MainActivity.this,UnusualRegisterActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals( R.string.menuInventory)){
                    Intent intent =new Intent();
                    intent.setClass(MainActivity.this,InventoryActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals(R.string.menuBookExport)){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,ExportActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals(R.string.menuLog)){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,LogListActivity.class);
                    MainActivity.this.startActivity(intent);
                }else if( tid.equals(R.string.menuImportBook)){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,ImportBookActivity.class);
                    MainActivity.this.startActivity(intent);
                }

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //仅屏蔽back键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
