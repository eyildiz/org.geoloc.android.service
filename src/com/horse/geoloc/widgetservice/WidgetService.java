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

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class WidgetService extends Service{


	private static String URL = "http://107.20.156.81:8080/GeoLocWebServices-0.1/ws/";
	private static int TIMEOUT = 10000;
	
	
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	
	Location location;
	private double latitude;
	private double longitude;
	
	LocationManager locationManager;
	GPSListener locationListener;
	String provider;
		
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		try {	
			locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
			locationListener = new GPSListener();
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				
		}catch (Exception e) {
			toastMessage("Hata in onCreate: " + e.toString());
		}
		
		if(isGPSEnabled || isNetworkEnabled){
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		
		toastMessage("Using " + provider);
		toastMessage("network " + String.valueOf(isNetworkEnabled) + " gps: " + String.valueOf(isGPSEnabled));
		}
		else 
			toastMessage("Error: GPS and Network are probably close!");
		
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		
		try {
		locationManager.requestLocationUpdates(provider, 5 , 0 , locationListener);
		} catch (Exception e) {
			toastMessage("Error in onStartCommand " + e.toString());
		}
					
			
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private class GPSListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			try {
				
				latitude = location.getLatitude();
				longitude = location.getLongitude();	
				
				toastMessage("latitude: " + String.valueOf(latitude));
				toastMessage("longitude: " + String.valueOf(longitude));
				
				sendData(WidgetBroadcastReceiver.IMEI, latitude, longitude);
				
			} catch (Exception e){
				toastMessage("Error in onLocationChanged" + e.toString());
			}

			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			toastMessage("Konum Sağlayıcı devre dışı");
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
					
	}
	
	
	private void sendData(String IMEI, double latitude, double longitude)
	{
		JSONObject sendDataObj = new JSONObject();
		try {
			sendDataObj.put("IMEI", IMEI);
			sendDataObj.put("latitude", latitude);
			sendDataObj.put("longitude", longitude);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String JsonString = sendDataObj.toString();
		
		String methodName = "updateUserLocation";
		
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
			client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void toastMessage(String msg){
		
		Toast.makeText(this, msg , Toast.LENGTH_SHORT).show();
	}

}
