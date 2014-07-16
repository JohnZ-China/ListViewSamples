package com.example.listviewsamples.util;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

	public static void show(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	public static void show(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
}
