package com.example.mymapapp;

import com.example.mymapapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;


public class MainActivity extends FragmentActivity
implements OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener {

	private GoogleMap mMap;
	private TextView mTapTextView;
	private TextView mCameraTextView;
	private static final LatLng BRISBANE = new LatLng(0, 0);
	private static final LatLng MELBOURNE = new LatLng(40.44115394, -86.90162);
	private static final LatLng SYDNEY = new LatLng(40.4057592, -86.90162);
	private static final LatLng ADELAIDE = new LatLng(40.4057592, -86.96301);
	private static final LatLng PERTH = new LatLng(40.44115394, -86.96301);
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	
	mTapTextView = (TextView) findViewById(R.id.tap_text);
	mCameraTextView = (TextView) findViewById(R.id.camera_text);
	
	setUpMapIfNeeded();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}
	
	private void setUpMapIfNeeded() {
		if (mMap == null) {
		    mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		            .getMap();
		    
		    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		    Criteria criteria = new Criteria();
		    String provider = locationManager.getBestProvider(criteria, true);
		    Location myLocation = locationManager.getLastKnownLocation(provider);
		    double latitude = myLocation.getLatitude();
		    double longitude = myLocation.getLongitude();
		    LatLng latLng = new LatLng(latitude, longitude);      
		    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
		    
		    if (mMap != null) {
		        setUpMap();
		    }
		}
		
	}
	
	private void setUpMap() {
		mMap.setOnMapClickListener(this);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnCameraChangeListener(this);
		//mMap.setLocationSource(mLocationSource);
        //mMap.setOnMapLongClickListener(mLocationSource);        
		mMap.setMyLocationEnabled(true); //shows the location button and enables it
		//mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(100,100), 1));
		
		mMap.addPolyline((new PolylineOptions())
		        .add(MELBOURNE,SYDNEY, ADELAIDE, PERTH,MELBOURNE).width(5));
	}
	@Override
    protected void onPause() {
        super.onPause();
    }
	
	@Override
	public void onMapClick(LatLng point) {
		mTapTextView.setText("tapped, point=" + point);
		mMap.addMarker(new MarkerOptions()
		.position(point)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
		.draggable(true));
	
	}
	
	@Override
	public void onMapLongClick(LatLng point) {
		mTapTextView.setText("long pressed, point=" + point);
		mMap.clear();
		setUpMap();
	}
	
	@Override
	public void onCameraChange(final CameraPosition position) {
		mCameraTextView.setText(position.toString());
	}
}
