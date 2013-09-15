package com.example.mymapapp;

import com.example.mymapapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends FragmentActivity
implements OnMarkerDragListener,OnMarkerClickListener, OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener {

	private GoogleMap mMap;
	private TextView mTap;
	private Marker[] mkr= new Marker[20];
	private int inc = 0;
	private int selmkr;
	private float zm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	
	mTap = (TextView) findViewById(R.id.tap_text);
	
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
		mMap.setOnMarkerClickListener(this);
		mMap.setMyLocationEnabled(true); //shows the location button and enables it		
	}
	@Override
    protected void onPause() {
        super.onPause();
    }
	
	@Override
	public void onMapClick(LatLng point) {
		inc++;
		//mTapTextView.setText("tapped, point=" + point);
		mkr[inc-1] = mMap.addMarker(new MarkerOptions()
		.position(point));
		selmkr = inc;
		mTap.setText("point #"+(selmkr)+" is selected");
		doPolygon();
	
	}
	
	@Override
	public void onMapLongClick(LatLng point) {
		//mTapTextView.setText("long pressed, point=" + point);
		//mTapTextView.setText("long pressed, point=" + inc);
		mTap.setText("no point selected");
		mMap.clear();
		inc = 0;
	}
	
	@Override
	public void onCameraChange(final CameraPosition position) {
		//mCameraTextView.setText(position.toString());
		zm = position.zoom;
		//mTap.setText("zoom"+zm);

	}
	
	
	public void doPolygon(){
		mMap.clear();
		PolygonOptions options = new PolygonOptions();
		for (int i = 0; i < inc; i++){
			mkr[i] = mMap.addMarker(new MarkerOptions()
			.position(mkr[i].getPosition())
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
			.draggable(true));
			options.add(mkr[i].getPosition());
		}
		mMap.addPolygon(options
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
		doPolygon();
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
	public void moveUp(View view) {
		mvMkr(Math.PI/2);
    }
	public void moveDn(View view) {
		mvMkr(-Math.PI/2);
    }
	public void moveRt(View view) {
		mvMkr(0);
	}
	public void moveLt(View view) {
		mvMkr(Math.PI);
	}
	public void doDelete(View view) {
		inc--;
		doPolygon();
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		for (int i = 0;i<inc;i++){
			if(arg0.equals(mkr[i])){
				selmkr = i+1;
			}
		}
		mTap.setText("point #"+(selmkr)+" is selected");
		return false;
	}
	public void mvMkr(double phase){
		double lat,lon;
		lat = mkr[selmkr-1].getPosition().latitude;
		lon = mkr[selmkr-1].getPosition().longitude;
        mkr[selmkr-1].setPosition(new LatLng(lat+(0.000003*Math.pow(2, (21-zm)))*Math.sin(phase),lon+(0.000003*Math.pow(2, (21-zm)))*Math.cos(phase)));
		doPolygon();
		
	}
}
