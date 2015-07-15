package com.jxd.bookdistribution.util;

import com.jxd.bookdistribution.app.BookApplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

public class ToastUtil {
	private static final int BUTTON_POSITIVE = -1;
	private static final int BUTTON_NEGATIVE = -2;
	static Toast  mToast=null;
	public static void Show(  String msg){
		if( mToast ==null){
			mToast = Toast.makeText( BookApplication.mApp.getApplicationContext() , msg , Toast.LENGTH_SHORT);
		}
		else{
			mToast.setText(msg);
		}
		mToast.show();
	}

	public  static void Cancel(){
		if( mToast !=null){
			mToast.cancel();
		}
	}

	static AlertDialog mAlertDialog =null;
	public static AlertDialog InputDialog( View view, String message , String positive , String negative , DialogInterface.OnClickListener positiveListener , DialogInterface.OnClickListener negativeListener){
		if( mAlertDialog==null){
		 AlertDialog.Builder builder = new AlertDialog.Builder( BookApplication.mApp.getApplicationContext()).setMessage(message).setPositiveButton(  positive, positiveListener).setNegativeButton( negative , negativeListener).setView(view);
		 mAlertDialog = builder.create();
		}else{
			mAlertDialog.setView(view);
			mAlertDialog.setMessage(message);
			mAlertDialog.setButton( BUTTON_POSITIVE ,  positive , positiveListener);
			mAlertDialog.setButton(BUTTON_NEGATIVE   ,  negative , negativeListener);
		}
      return mAlertDialog;
	}

}
