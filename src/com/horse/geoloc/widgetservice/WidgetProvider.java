package com.horse.geoloc.widgetservice;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider{
	
	RemoteViews remoteView;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		final int N = appWidgetIds.length;
		
		for(int i = 0 ; i < N ; i++)
		{
			
			int widgetId = appWidgetIds[i];
			Intent intent = new Intent();
		    intent.setAction(WidgetBroadcastReceiver.INTENT_ACTION);
		    intent.putExtra("widgetID",widgetId);
		    intent.setClassName(WidgetBroadcastReceiver.class.getPackage().getName(), WidgetBroadcastReceiver.class.getName());
			
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
			
			remoteView = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
			remoteView.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);
			
			appWidgetManager.updateAppWidget(widgetId , remoteView);
			
		}
		
		
		
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		
		Toast.makeText(context, "Widget Silindi" , Toast.LENGTH_SHORT).show();
	}



	
}
