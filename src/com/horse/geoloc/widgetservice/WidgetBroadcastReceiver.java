package com.horse.geoloc.widgetservice;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetBroadcastReceiver extends BroadcastReceiver{

	RemoteViews remoteView ;
	AppWidgetManager appWidgetManager;
	LocationManager locationManager;
	
	public static final String INTENT_ACTION= "TOGGLE_ACTION";
	private static boolean flag = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		remoteView = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
		appWidgetManager = AppWidgetManager.getInstance(context);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) )
		{
			Toast.makeText(context,"Konum sağlayıcılarınız devre dışı, lütfen kontrol ediniz" ,Toast.LENGTH_SHORT);
			setWidgetİmage(intent, R.drawable.power_black);
		}
		else
		{
			if(intent.getAction().toString().equals(INTENT_ACTION))
			{
				if(flag == false)
				{
					Toast.makeText(context, "Açıldı", Toast.LENGTH_SHORT).show();
					setFlag(true);
					remoteView.setTextViewText(R.id.widgetTextView,"GPS ON");
					setWidgetİmage(intent, R.drawable.power_blue);
					Intent in = new Intent(context, WidgetService.class);
					context.startService(in);
					
				}
				
				else if(flag == true)
				{
					Toast.makeText(context, "Kapandı", Toast.LENGTH_SHORT).show();
					setFlag(false);
					remoteView.setTextViewText(R.id.widgetTextView,"GPS OFF");
					setWidgetİmage(intent,R.drawable.power_black);
					Intent in = new Intent(context, WidgetService.class);
					context.stopService(in);
				}
		    }
		}
		
	}

	public static boolean isFlag() {
		return flag;
	}
	
	public static void setFlag(boolean flag) {
		WidgetBroadcastReceiver.flag = flag;
	}

	private void setWidgetİmage(Intent intent , int id)
	{
		remoteView.setImageViewResource(R.id.widgetButton,id);
		appWidgetManager.updateAppWidget(intent.getExtras().getInt("widgetID"),remoteView);
	}


}
