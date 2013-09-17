package com.example.mymapapp;

import java.util.ArrayList;
import java.util.List;

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
import com.google.android.gms.maps.model.Polygon;
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
	private int selmkr = 0;
	private float zm;
	private double bm;
	private boolean j = true;
	private Polygon poly;
	private PolygonOptions options = new PolygonOptions();
	
	
	private List<Marker> mkr1 = new ArrayList<Marker>();
	
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
		options.add(new LatLng(0,0));
		poly = mMap.addPolygon(options
				.strokeWidth(2)
                .strokeColor(Color.BLACK)
                .fillColor(0x7FB5B1E0));
	}
	@Override
    protected void onPause() {
        super.onPause();
    }
	
	@Override
	public void onMapClick(LatLng point) {
		if (inc>0){
			mkr1.get(selmkr-1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		}
		mkr1.add(selmkr, mMap.addMarker(new MarkerOptions()
		.position(point)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
		.draggable(true)));
		selmkr++;
		inc++;
		mTap.setText("point #"+(selmkr)+" is selected");
		doOption();
		
	}
	
	@Override
	public void onMapLongClick(LatLng point) {
		
	}
	
	@Override
	public void onCameraChange(final CameraPosition position) {
		zm = position.zoom;
		bm = ((double)position.bearing)/180*Math.PI;
	}
	

	@Override
	public void onMarkerDrag(Marker arg0) {
		doOption();
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		mkr1.get(selmkr-1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		if(!arg0.equals(mkr1.get(selmkr-1))){
			mkr1.get(selmkr-1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		}
		for (int i = 0;i<inc;i++){
			if(arg0.equals(mkr1.get(i))){
				selmkr = i+1;
			}
		}
		mTap.setText("point #"+(selmkr)+" is selected");
	}
	public void moveUp(View view) {
		if (inc != 0){
			mvMkr(Math.PI/2);
		}
    }
	public void moveDn(View view) {
		if (inc != 0){
			mvMkr(-Math.PI/2);
		}
    }
	public void moveRt(View view) {
		if (inc != 0){
			mvMkr(0);
		}
	}
	public void moveLt(View view) {
		if (inc != 0){
			mvMkr(Math.PI);
		}
	}
	public void doDelete(View view) {
		if(inc > 0){
			mkr1.get(selmkr-1).remove();
			mkr1.remove(selmkr-1);
			inc--;
			selmkr--;
			mTap.setText("no point selected");
			if(selmkr == 0){
				selmkr = inc;
			}
			if(selmkr > 0){
				mkr1.get(selmkr-1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				doOption();
			}
			mTap.setText("point #"+(selmkr)+" is selected " + inc + selmkr);
			
		}
		
	}
	public void doDis(View view) {
		for (int i = 0 ; i<inc ; i++){
			mkr1.get(i).setVisible(!j);	
		}
		j = !j;
	}
	public void doOption(){
		options = new PolygonOptions();
		for (int i = 0; i < inc; i++){
			options.add(mkr1.get(i).getPosition());
		}
		poly.setPoints(options.getPoints());
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		mkr1.get(selmkr-1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		for (int i = 0;i<inc;i++){
			if(arg0.equals(mkr1.get(i))){
				selmkr = i+1;
				arg0.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			}
		}
		mTap.setText("point #"+(selmkr)+" is selected");
		
		
		return false;
	}
	public void mvMkr(double phase){
		double lat,lon;
		lat = mkr1.get(selmkr-1).getPosition().latitude;
		lon = mkr1.get(selmkr-1).getPosition().longitude;
        mkr1.get(selmkr-1).setPosition(new LatLng(lat+(0.000005*Math.pow(2, (21-zm)))*Math.sin(phase-bm),lon+(0.000005*Math.pow(2, (21-zm)))*Math.cos(phase-bm)));
		doOption();
	}
}
