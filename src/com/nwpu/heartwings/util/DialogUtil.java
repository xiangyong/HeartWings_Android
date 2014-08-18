package com.nwpu.heartwings.util;
import android.app.AlertDialog;
import android.content.Context;

public class DialogUtil {

	public static void showDialog(final Context context,String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context).
				 setMessage(msg).setCancelable(false);
		builder.setPositiveButton("ȷ��", null);
		builder.create().show();
	}
}
