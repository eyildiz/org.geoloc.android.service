package com.horse.geoloc.widgetservice;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetBroadcastReceiver extends BroadcastReceiver{

	
	public static final String INTENT_ACTION= "TOGGLE_ACTION";
	private static boolean flag = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
				
		RemoteViews v = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
	//	RemoteViews v2 = new RemoteViews(context.getPackageName(),R.id.wi)
		AppWidgetManager appmanager = AppWidgetManager.getInstance(context);
		
		if(intent.getAction().toString().equals(INTENT_ACTION))
		{
			if(flag == false)
			{
				Toast.makeText(context, "Açıldı", Toast.LENGTH_SHORT).show();
				setFlag(true);
				v.setTextViewText(R.id.widgetTextView,"GPS ON");
				v.setImageViewResource(R.id.widgetButton,R.drawable.power_blue);
				appmanager.updateAppWidget(intent.getExtras().getInt("widgetID"),v);
				
				Intent in = new Intent(context, WidgetService.class);
				context.startService(in);
				
			}
			
			else if(flag == true)
			{
				Toast.makeText(context, "Kapandı", Toast.LENGTH_SHORT).show();
				setFlag(false);
				v.setTextViewText(R.id.widgetTextView,"GPS OFF");
				v.setImageViewResource(R.id.widgetButton,R.drawable.power_black);
				appmanager.updateAppWidget(intent.getExtras().getInt("widgetID"),v);
				
				Intent in = new Intent(context, WidgetService.class);
				context.stopService(in);
			}
	    }
		
	}

	public static boolean isFlag() {
		return flag;
	}
	
	public static void setFlag(boolean flag) {
		WidgetBroadcastReceiver.flag = flag;
	}


}
