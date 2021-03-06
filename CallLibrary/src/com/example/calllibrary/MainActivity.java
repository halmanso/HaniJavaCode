package com.example.calllibrary;


import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;


import com.example.librarytest.AtkPolygon;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends FragmentActivity {
	private GoogleMap map = null;
	private RelativeLayout rel = null;	
	private Boolean j = true;
	private AtkPolygon fr;
	private Polygon poly;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (map == null) {
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);
			Location myLocation = locationManager
					.getLastKnownLocation(provider);
			LatLng latLng = new LatLng(40.4282, -86.91775);
			if (myLocation != null)
				latLng = new LatLng(myLocation.getLatitude(),
						myLocation.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
		}
		map.setMapType(MAP_TYPE_HYBRID);
		map.setMyLocationEnabled(true);
		UiSettings mU;
		mU = map.getUiSettings();
		mU.setZoomControlsEnabled(false);
		rel = (RelativeLayout) findViewById(R.id.rel);
		fr = new AtkPolygon(map,rel,this);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	public void btn(View v) {
		if (j == true){
			fr.startDrawing();
			j = !j;
		} 
		else{
			poly = fr.getPolygon();
			fr.stopDrawing();
			j = !j;
		}
	}
	public void btn2(View v) {
	}
}
