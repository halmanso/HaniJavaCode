package com.example.mymapapp;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;

import java.util.ArrayList;
import java.util.List;

import com.example.mymapapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.MotionEvent;


public class MainActivity extends FragmentActivity
implements OnCameraChangeListener,OnMarkerDragListener
,OnMarkerClickListener, OnMapClickListener {
	
	private GoogleMap mMap = null;
	private GoogleMap mMap2 = null;
	//private TextView mTap;
	private int inc = 0;
	private int selmkr = 0;
	private boolean hideT = true;
	private Polygon poly;
	private List<Marker> mkr = new ArrayList<Marker>();
	private Polyline line;
	private int endm = 1;
	private RelativeLayout rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	//mTap = (TextView) findViewById(R.id.tap_text);
	setUpMapIfNeeded();
	}
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}
	private void setUpMapIfNeeded() {
		if (mMap == null || mMap2 == null) {
			while(mMap == null || mMap2 == null){
				mMap = ((SupportMapFragment) getSupportFragmentManager()
			    		.findFragmentById(R.id.map))
			            .getMap();
			    mMap2 = ((SupportMapFragment) getSupportFragmentManager()
			    		.findFragmentById(R.id.map2))
			            .getMap();
			}
		  //this code below is for centering the camera at current location
		    LocationManager locationManager =
		    		(LocationManager) getSystemService(LOCATION_SERVICE);
		    Criteria criteria = new Criteria();
		    String provider = locationManager.getBestProvider(criteria, true);
		    Location myLocation = locationManager.getLastKnownLocation(provider);
		    LatLng latLng = new LatLng(40.4282,-86.91775);
		    if(myLocation != null)latLng = new LatLng(myLocation.getLatitude()
		    		, myLocation.getLongitude());    
		    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
		    ///////////////////////////////////////////////////////
		}
		
		if (mMap != null && mMap2 != null) setUpMap();
	}
	private void setUpMap() {
		mMap.setOnMapClickListener(this);
		mMap.setOnCameraChangeListener(this);
		mMap.setOnMarkerDragListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setMyLocationEnabled(true);
		UiSettings mU,mU2;
		
		mU = mMap.getUiSettings();
		mU.setZoomControlsEnabled(false);
		
		mU2 = mMap2.getUiSettings();
		mU2.setZoomControlsEnabled(false);
		mU2.setAllGesturesEnabled(false);
		mU2.setCompassEnabled(false);
		rl = (RelativeLayout) findViewById(R.id.lay);
		
	    mMap.setMapType(MAP_TYPE_HYBRID);
	    mMap2.setMapType(MAP_TYPE_SATELLITE);
		
		Button drg = (Button)findViewById(R.id.mdrg2);
		drg.setOnTouchListener(new View.OnTouchListener() {
			Point pmkr = new Point();
			Point pmkr2 = new Point();
			float  touchx;
			float  touchy;
		    @Override 
		    public boolean onTouch(View v, MotionEvent event) {
		    	if(inc>0){
		   
			        switch(event.getAction()) {
			        case MotionEvent.ACTION_DOWN:
		    			rl.setVisibility(View.VISIBLE);
		    			mvScreen();
			        	pmkr.set(mMap.getProjection().toScreenLocation(mkr.get(selmkr-1)
			        			.getPosition()).x,mMap.getProjection()
			        			.toScreenLocation(mkr.get(selmkr-1).getPosition()).y);
			        	pmkr2.set((int)event.getX(),(int)event.getY());
			        	break;
			        case MotionEvent.ACTION_MOVE:
			        	touchx = (event.getX() - pmkr2.x) + pmkr.x;
			    	    touchy = (event.getY() - pmkr2.y) + pmkr.y;
			    	    mkr.get(selmkr-1).setPosition(mMap.getProjection()
			    	    		.fromScreenLocation(new Point(Math.round(touchx)
			    	    				,Math.round(touchy))));
			    	    mvScreen();
			    	    doOption();
			    	    break;
			        case MotionEvent.ACTION_UP:
			    		rl.setVisibility(View.INVISIBLE);
			    		break;
			    	    
			        }
		    	}
		        return false;
		    }
		});
		if (poly == null) poly = mMap.addPolygon(new PolygonOptions().add(new LatLng(0,0))
				.strokeWidth(2)
                .strokeColor(Color.BLACK)
                .fillColor(0x7FB5B1E0));
		if (line == null) line = mMap.addPolyline(new PolylineOptions().add(new LatLng(0,0))
				.color(Color.WHITE)
				.width(2));
	}
	@Override
    protected void onPause() {
        super.onPause();
    }
	@Override
	public void onMapClick(LatLng point) {
		if (inc>0)flagRed(mkr.get(selmkr-1));
		mkr.add(selmkr, mMap.addMarker(new MarkerOptions()
		.position(point)
		.anchor((float)0.5, (float)0.5)
		.draggable(true)
		.visible(hideT)));
		selmkr++;
		inc++;
		flagGreen(mkr.get(selmkr-1));
	}
	@Override
	public void onMarkerDrag(Marker arg0) {
		if (arg0.equals(mkr.get(selmkr-1))) mvScreen();
		doOption();
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		rl.setVisibility(View.INVISIBLE);
		doOption();
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		if (arg0.equals(mkr.get(selmkr-1))){
			rl.setVisibility(View.VISIBLE);
			mvScreen();
		}
		doOption();
	}

	public void doDelete(View view) {
		if(inc > 0){
			mkr.get(selmkr-1).remove();
			mkr.remove(selmkr-1);
			inc--;
			selmkr--;
			if(selmkr == 0)selmkr = inc;
			if(selmkr > 0)flagGreen(mkr.get(selmkr-1));
			//mTap.setText(inc+" "+selmkr);
		}
	}
	public void doDis(View view) {
		for (int i = 0 ; i<inc ; i++){
			mkr.get(i).setVisible(!hideT);	
		}
		line.setVisible(!hideT);
		hideT = !hideT;
	}
	public void doOption(){
		PolygonOptions options = new PolygonOptions();
		PolylineOptions lineOptions = new PolylineOptions();
		for (int i = 0; i < inc; i++){
			options.add(mkr.get(i).getPosition());
		}
		lineOptions.add(mkr.get(selmkr-1).getPosition(),mkr.get(endm-1)
				.getPosition());
		poly.setPoints(options.getPoints());
		line.setPoints(lineOptions.getPoints());
		
	}
	@Override
	public boolean onMarkerClick(Marker arg0) {
		flagRed(mkr.get(selmkr-1));
		for (int i = 0;i<inc;i++){
			if(arg0.equals(mkr.get(i))){
				selmkr = i+1;
				flagGreen(arg0);
			}
		}
		return false;
	}
	public void flagGreen(Marker arg0){
		BitmapDescriptor sel = BitmapDescriptorFactory
				.fromResource(R.drawable.selected_vertex);
		arg0.setIcon(sel);
		if (inc > 0){
			if (selmkr == inc)endm = 1;
			if (selmkr != inc)endm = selmkr+1;
		}
		doOption();
	}
	public void flagRed(Marker arg0){
		BitmapDescriptor unsel = BitmapDescriptorFactory
				.fromResource(R.drawable.unselected_vertex);
		arg0.setIcon(unsel);
	}
	public void mvScreen(){
		mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(mkr.get(selmkr-1)
				.getPosition(),mMap.getCameraPosition().zoom + 2));
        rl.setX(mMap.getProjection().toScreenLocation(mkr.get(selmkr-1)
        		.getPosition()).x - 78 * Resources.getSystem().getDisplayMetrics().density);
        rl.setY(mMap.getProjection().toScreenLocation(mkr.get(selmkr-1)
        		.getPosition()).y - 170 * Resources.getSystem().getDisplayMetrics().density);
	}
	@Override
	public void onCameraChange(CameraPosition arg0) {
		if (mMap2.getCameraPosition().bearing != mMap.getCameraPosition().bearing){
			mMap2.moveCamera(CameraUpdateFactory.newCameraPosition(arg0));
		}
	}
	public void Plus(View v){
		if(inc>1){
			flagRed(mkr.get(selmkr-1));
			if(selmkr == inc)selmkr = 1;
			else if(selmkr != inc) selmkr++;
			flagGreen(mkr.get(selmkr-1));
		}
	}
	public void Minus(View v){
		if(inc>1){
			flagRed(mkr.get(selmkr-1));
			if(selmkr == 1) selmkr = inc;
			else if(selmkr != 1) selmkr--;
			flagGreen(mkr.get(selmkr-1));
		}
	}
}
