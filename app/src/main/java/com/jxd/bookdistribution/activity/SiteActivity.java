package com.jxd.bookdistribution.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.SiteAdapter;
import com.jxd.bookdistribution.bean.Site;
import com.jxd.bookdistribution.util.SQLiteUitl;

import java.util.ArrayList;
import java.util.List;

public class SiteActivity extends Activity implements View.OnClickListener{
    ListView mlv=null;
    SiteAdapter mAdapter=null;
    List<Site> mList=null;
    Button mBtnAdd=null;
    Button mBtnBack=null;
    TextView mtvTitle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mtvTitle.setText(getString(R.string.menuSite));

        mBtnAdd = (Button)this.findViewById(R.id.btnAddSite);
        mBtnAdd.setOnClickListener(this);
        mBtnBack=(Button)this.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(this);
        mlv = (ListView)this.findViewById(R.id.lvsite);

        loadSites();
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnAdd) {
            final EditText etSite = new EditText(SiteActivity.this);
            new AlertDialog.Builder(SiteActivity.this)
                    .setView(etSite)
                    .setTitle("添加站点")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String sitename = etSite.getText().toString();
                            if (sitename.isEmpty()) {
                                return;
                            }
                            SQLiteUitl util = new SQLiteUitl(SiteActivity.this);
                            List<Site> sites = new ArrayList<Site>();
                            Site item = new Site();
                            item.setSiteName(sitename);
                            sites.add(item);
                            util.insertSites(sites);
                            loadSites();

                        }
                    }).show();
        }else if( view ==mBtnBack){
            SiteActivity.this.finish();
        }
    }

    protected void loadSites(){
        SQLiteUitl util =new SQLiteUitl(SiteActivity.this);
        mList = util.querySites();
        mAdapter = new SiteAdapter(this , mList);
        mlv.setAdapter(mAdapter);
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
