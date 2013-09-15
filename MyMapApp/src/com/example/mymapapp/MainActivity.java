package com.example.mymapapp;

import com.example.mymapapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;


public class MainActivity extends FragmentActivity
implements OnMarkerDragListener, OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener {

	private GoogleMap mMap;
	private TextView mTapTextView;
	private TextView mCameraTextView;
	private static final LatLng BRISBANE = new LatLng(0, 0);
	private static final LatLng MELBOURNE = new LatLng(40.44115394, -86.90162);
	private static final LatLng SYDNEY = new LatLng(40.4057592, -86.90162);
	private static final LatLng ADELAIDE = new LatLng(40.4057592, -86.96301);
	private static final LatLng PERTH = new LatLng(40.44115394, -86.96301);
	private Marker[] mkr= new Marker[20];
	private Polygon poly;
	private int inc = 0;
	
	
	
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
		    //this code below is for centering the camera at current location
		    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		    Criteria criteria = new Criteria();
		    String provider = locationManager.getBestProvider(criteria, true);
		    Location myLocation = locationManager.getLastKnownLocation(provider);
		    double latitude = myLocation.getLatitude();
		    double longitude = myLocation.getLongitude();
		    LatLng latLng = new LatLng(latitude, longitude);      
		    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
		    ///////////////////////////////////////////////////////
		    
		    if (mMap != null) {
		        setUpMap();
		    }
		}
		
	}
	
	private void setUpMap() {
		mMap.setOnMapClickListener(this);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnCameraChangeListener(this);
		mMap.setOnMarkerDragListener(this);
		//mMap.setLocationSource(mLocationSource);
        //mMap.setOnMapLongClickListener(mLocationSource);        
		mMap.setMyLocationEnabled(true); //shows the location button and enables it
		//mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(100,100), 1));
		
		//mMap.addPolyline((new PolylineOptions())
		//        .add(MELBOURNE,SYDNEY, ADELAIDE, PERTH,MELBOURNE).width(5));
	}
	@Override
    protected void onPause() {
        super.onPause();
    }
	
	@Override
	public void onMapClick(LatLng point) {
		mMap.clear();
		inc++;
		mTapTextView.setText("tapped, point=" + point);
		mkr[inc-1] = mMap.addMarker(new MarkerOptions()
		.position(point)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
		.draggable(true));
		mMap.clear();
		doPolygon();
	
	}
	
	@Override
	public void onMapLongClick(LatLng point) {
		//mTapTextView.setText("long pressed, point=" + point);
		mTapTextView.setText("long pressed, point=" + inc);
		mMap.clear();
		inc = 0;
	}
	
	@Override
	public void onCameraChange(final CameraPosition position) {
		mCameraTextView.setText(position.toString());
	}
	
	
	public void doPolygon(){
		PolygonOptions options = new PolygonOptions();
		for (int i = 0; i < inc; i++){
			mkr[i] = mMap.addMarker(new MarkerOptions()
			.position(mkr[i].getPosition())
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
			.draggable(true));
			options.add(mkr[i].getPosition());
		}
		poly = mMap.addPolygon(options
                .strokeWidth(5)
                .strokeColor(Color.BLACK));
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		mMap.clear();
		doPolygon();
		
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
}
