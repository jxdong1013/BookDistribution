package com.jxd.bookdistribution.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jxd.bookdistribution.activity.LoginActivity;

/**
 * Created by 向东 on 2015/5/12.
 */
public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //String action = intent.getAction();
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setClass(context, LoginActivity.class);
        context.startActivity(i);
    }
}
