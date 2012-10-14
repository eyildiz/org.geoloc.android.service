package com.horse.geoloc.widgetservice;

import java.security.Provider;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class WidgetService extends Service{


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
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			toastMessage("Hata in onCreate: " + e.toString());
		}
		
		if(isGPSEnabled || isNetworkEnabled){
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		location =locationManager.getLastKnownLocation(provider);
		
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
			
		 locationManager.requestLocationUpdates(provider, 0, 1, locationListener);
			
		} catch (Exception e) {
			// TODO: handle exception
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
				
			} catch (Exception e) {
				// TODO: handle exception
				
				toastMessage("Error in onLocationChanged" + e.toString());
			}

			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
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
	
	

	private void toastMessage(String msg){
		
		Toast.makeText(this, msg , Toast.LENGTH_SHORT).show();
		
	}

}
