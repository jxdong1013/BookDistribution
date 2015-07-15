package com.jxd.bookdistribution.activity;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jxd.bookdistribution.R;
import com.jxd.bookdistribution.adapter.InventoryAdapter;
import com.jxd.bookdistribution.bean.Inventory;
import com.jxd.bookdistribution.thread.InventoryThread;
import com.jxd.bookdistribution.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends Activity implements AdapterView.OnItemClickListener , View.OnClickListener{
    ListView mlv=null;
    List<Inventory> mlists=null;
    InventoryAdapter mAdapter=null;
    TextView mtvTitle=null;
    Button mbtnAdd=null;
    InventoryHandler mHandler=null;
    public static int RequestCodeRefresh=1008;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mtvTitle=(TextView)this.findViewById(R.id.tvAppTitle);
        mtvTitle.setText(getString(R.string.menuInventory));
        mlv=(ListView)this.findViewById(R.id.lvInventory);

        mlists=new ArrayList<Inventory>();
//        Inventory item  =new Inventory();
//        item.setTaskName("盘点计划1");
//        item.setSiteId(1);
//        item.setSiteName("");
//        item.setStatus(0);
//        mlists.add(item);

//        item  =new Inventory();
//        item.setTaskName("盘点计划2");
//        item.setSiteId(1);
//        item.setSiteName("");
//        item.setStatus(0);
//        mlists.add(item);

//        item  =new Inventory();
//        item.setTaskName("盘点计划3");
//        item.setSiteId(1);
//        item.setSiteName("");
//        item.setStatus(0);
//        mlists.add(item);

//        item  =new Inventory();
//        item.setTaskName("盘点计划4");
//        item.setSiteId(1);
//        item.setSiteName("");
//        item.setStatus(0);
//        mlists.add(item);

//        item  =new Inventory();
//        item.setTaskName("盘点计划5");
//        item.setSiteId(1);
//        item.setSiteName("");
//        item.setStatus(0);
//        mlists.add(item);

        mAdapter = new InventoryAdapter(InventoryActivity.this,mlists,false);
        mlv.setAdapter(mAdapter);
        mlv.setOnItemClickListener(this);

        mbtnAdd = (Button)this.findViewById(R.id.btnAddInventory);
        mbtnAdd.setOnClickListener(this);

        mHandler=new InventoryHandler();

        AsyncLoadInventory();
    }

    void AsyncLoadInventory(){
        InventoryThread thread=new InventoryThread(InventoryActivity.this, mHandler);
        thread.start();
    }

    @Override
    public void onClick(View view) {
        if( view == mbtnAdd ){
            Intent intent = new Intent();
            intent.setClass(InventoryActivity.this,AddInventoryActivity.class);
            InventoryActivity.this.startActivityForResult(intent, RequestCodeRefresh);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK && requestCode == RequestCodeRefresh){
            AsyncLoadInventory();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if( i < 0 || mlists ==null || mlists.size()<1 ) return;
        Intent intent = new Intent();
        intent.setClass(InventoryActivity.this,InventoryListActivity.class);
        Inventory inventory = mlists.get(i);
        Bundle bd = new Bundle();
        bd.putSerializable("inventory", inventory);
        intent.putExtras(bd);
        InventoryActivity.this.startActivity(intent);
    }

    class InventoryHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if( msg.what == InventoryThread.MSG_Success){
                //mlists = (List<Inventory>)msg.obj;
                mlists.clear();
                List<Inventory> temp = (List<Inventory>) msg.obj;
                if( temp !=null) {
                    mlists.addAll(temp);
                }
                mAdapter.notifyDataSetChanged();
            }else if( msg.what == InventoryThread.MSG_Error){
                String error = msg.obj==null?"查询失败": msg.obj.toString();
                ToastUtil.Show(error);
            }
        }
    }
}
