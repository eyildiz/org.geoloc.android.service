package com.horse.geoloc.widgetservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class WidgetBroadcastReceiver extends BroadcastReceiver{

	
	public static final String INTENT_ACTION= "TOGGLE_ACTION";
	private static boolean flag = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
				
		if(intent.getAction().toString().equals(INTENT_ACTION))
		{
			if(flag == false)
			{
				Toast.makeText(context, "Açıldı", Toast.LENGTH_SHORT).show();
				setFlag(true);
				//hop
			}
			else if(flag == true)
			{
				Toast.makeText(context, "Kapandı", Toast.LENGTH_SHORT).show();
				setFlag(false);
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
