package com.horse.geoloc.widgetservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetBroadcastReceiver extends BroadcastReceiver{

	RemoteViews remoteView ;
	AppWidgetManager appWidgetManager;
	LocationManager locationManager;
    public static String IMEI;

	
	private static String URL = "http://107.20.156.81:8080/GeoLocWebServices-0.1/ws/";
	private static int TIMEOUT = 10000;
	
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
				if(flag == false && checkUser(context))
				{
					Toast.makeText(context, "Açıldı", Toast.LENGTH_SHORT).show();
					setFlag(true);
					setWidgetİmage(intent, R.drawable.power_blue);
					Intent in = new Intent(context, WidgetService.class);
					context.startService(in);
					
				}
				
				else if(flag == true)
				{
					Toast.makeText(context, "Kapandı", Toast.LENGTH_SHORT).show();
					setFlag(false);
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

	
	private boolean checkUser(Context context){

		try{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		Toast.makeText(context, "imei: " + IMEI , Toast.LENGTH_SHORT).show();
		}
		catch(Exception ex){
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}
		
		
		JSONObject IMEIobj = new JSONObject();
		try {
			IMEIobj.put("IMEI", IMEI);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String JsonString = IMEIobj.toString();
		
		String methodName = "authenticateIMEI";
		String responseString = "false";
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT);
		
		HttpClient client = new DefaultHttpClient(httpParameters);
		
		HttpPost request = new HttpPost(URL+methodName);
		
		
		try{
			request.setEntity(new ByteArrayEntity(JsonString.getBytes("UTF8")));
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		try {
			HttpResponse response = client.execute(request);
			if(response != null)
			{
				if(response.getEntity()!=null)
					responseString = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(responseString.equals("-1"))
		{
			Toast.makeText(context,"Kayıt olmanız gerekli!" , Toast.LENGTH_SHORT).show();
			return false;
		}
		else if(responseString.equals("-2"))
		{
			Toast.makeText(context,"Telefonunuzun başka bir kaydı bulunuyor." , Toast.LENGTH_SHORT).show();
			return false;
		}
		else
			return true;
		
	}
	

}
