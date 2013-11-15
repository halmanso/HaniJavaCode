package com.example.pdrawing;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;

import java.util.ArrayList;
import java.util.List;


import com.example.librarytest.AtkMapWidget;
import com.example.librarytest.AtkMapWidget.AtkMapWidgetListener;
import com.example.pdrawing.TouchableWrapper;
import com.example.pdrawing.MySupportMapFragment;
import com.example.pdrawing.TouchableWrapper.UpdateMapAfterUserInterection;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.MapFragment;

import android.os.Bundle;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements 
  UpdateMapAfterUserInterection,AtkMapWidgetListener {

  //private GoogleMap mMap;
	
 /* @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
  */

	private GoogleMap mMap;
	private TextView mTapTextView;
    private TextView mCameraTextView;
	private List<LatLng> marker = new ArrayList<LatLng>();
	private List<LatLng> movPoint = new ArrayList<LatLng>();
	private int num=0;
	private int cnt=0;
	private Marker markerTmp;
	private double delta = 5;
	private double away = 10;
    private double dis = 0;
    private double perp;
    private double k = 0;
	private double b = 0;
	private double x1 = 0;
	private double x2 = 0;
	private double y1 = 0;
	private double y2 = 0;
	private int idx = 0;
	private Polyline polyline;
	private MapView mMapview;
	private MotionEvent prev;
	private float prevx;
	private float prevy;
	private int flag = 0;
	private AtkMapWidget widget;
	private RelativeLayout rel;
	   
	
	static final CameraPosition PURDUE =
          new CameraPosition.Builder().target(new LatLng(40.428639, -86.913775))
                  .zoom(18)
                  //.bearing(300)
                  //.tilt(50)
                  .build();
	
	
  
	
  private CheckBox mMapdragdisable;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//rel = (RelativeLayout) findViewById(R.id.rel);
	    setUpMapIfNeeded();
	    mMapdragdisable = (CheckBox) findViewById(R.id.disable);
	    
		
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
	            if (mMap != null) {
	                setUpMap();
	            }
	        }
	    }
	

	 private void setUpMap() {
	      //  mMap.setOnMapClickListener(this);
	      //  mMap.setOnMapLongClickListener(this);
	       // mMap.setOnCameraChangeListener(this);
	       // mMap.setOnMarkerDragListener(this);
		  //markerTmp = mMap.addMarker(new MarkerOptions() .position(new LatLng(40.428639, -86.913775)).title("Hello world"));
		  mMap.setMapType(MAP_TYPE_SATELLITE);
	      mMap.moveCamera(CameraUpdateFactory.newCameraPosition(PURDUE));
	        
	    }
  
  
  
  
  

// Implement the interface method
  public void onUpdateMapAfterUserInterection(MotionEvent ev) {
		// TODO Update the map now
	  //widget.movePop();
	 PolylineOptions options = new PolylineOptions();
	 int i;
	 Log.d("onUpdateMapAfterUserInterection", "updating the map");
	
	float touchx = ev.getX();
	//float prevx = prev.getX();
	Log.d("onUpdateMapAfterUserInterection", "got touch x:" + Float.toString(touchx));
	Log.d("onUpdateMapAfterUserInterection", "got prev x:" + Float.toString(prevx));
	
	float touchy = ev.getY();
	//float prevy = prev.getY();
	Log.d("onUpdateMapAfterUserInterection", "got touch y:" + Float.toString(touchy));
	Log.d("onUpdateMapAfterUserInterection", "got prev y:" + Float.toString(prevy));
	
	Point touchPoint = new Point(Math.round(touchx), Math.round(touchy));
	Log.d("onUpdateMapAfterUserInterection", "got touch point, x:" + Integer.toString(touchPoint.x) + " y:" + Integer.toString(touchPoint.y));

    Projection proj = mMap.getProjection();
	   Log.d("onUpdateMapAfterUserInterection", "Got projection");

	   LatLng newPos = proj.fromScreenLocation(touchPoint);
	   Log.d("onUpdateMapAfterUserInterection", "Got lat long");

	   
	 if (mMapdragdisable.isChecked()){
		 //dis = Math.sqrt(Math.pow(touchx - prevx, 2.0) + Math.pow(touchy - prevy, 2.0));
		 dis = Math.hypot(touchx - prevx, touchy - prevy);
		 if ((flag == 0) && (dis > delta)){
			 flag = 1;
			 //k = (touchy - prevy) / (touchx - prevx);
			 x2 = touchx - prevx;
			 Log.d("onUpdateMapAfterUserInterection", "x2:" + Double.toString(x2));
			 //b = touchy - touchx * k;
			 y2 = touchy - prevy;
			 Log.d("onUpdateMapAfterUserInterection", "y2:" + Double.toString(y2));   
		 }
		 if (flag == 1){
			 x1 = touchx - prevx;
			 y1 = touchy - prevy;
			 //perp = Math.abs(k * touchx - touchy + b) / Math.sqrt(k * k + 1);   
	         perp = Math.abs(x1 * y2 - x2 * y1) / Math.hypot(x2, y2);	 
		 }
		 else{
	         perp = 0;
		 }
		 
		 Log.d("onUpdateMapAfterUserInterection", "distance:" + Double.toString(dis));
		 
  		
		 Log.d("onUpdateMapAfterUserInterection", "add Marer: idx:" + Integer.toString(idx));
		 Log.d("onUpdateMapAfterUserInterection", "perp:" + Double.toString(perp));
	     if (perp > away){
	    	 idx++;
	    	 Log.d("onUpdateMapAfterUserInterection", "inside idx:" + Integer.toString(idx));

	    	 marker.add(idx, newPos);
	    	 Log.d("onUpdateMapAfterUserInterection", "got maker added");
	    	 
	    	 //k = (touchy - prevy) / (touchx - prevx);
	    	 Log.d("onUpdateMapAfterUserInterection", "got k");
		     //b = touchy - touchx * k;
		     Log.d("onUpdateMapAfterUserInterection", "got b");
	    	 prevx = touchx;
	    	 prevy = touchy;
	    	 Log.d("onUpdateMapAfterUserInterection", "got prev");
	    	 flag = 0;
	     }
		 for (i = 0; i <= idx; i++){
			 options.add(marker.get(i));
		 }
		 Log.d("onUpdateMapAfterUserInterection", "got option added");
		 options.add(newPos);
		 Log.d("onUpdateMapAfterUserInterection", "options add newPos");
		 polyline.setPoints(options.getPoints());
		 Log.d("onUpdateMapAfterUserInterection", "got polyline added:");
		 //markerTmp.setPosition(newPos);
	   
	 }
	 Log.d("onUpdateMapAfterUserInterection", "Set marker postion");
	 
	 
	   
	  /* if(mMap == null) Log.d("Map is Null", "map is null");
	   markerTmp.setPosition(
			mMap.getProjection().fromScreenLocation(
					new Point(Math.round(touchx), Math.round(touchy))
					)
	);*/
	/* mMap.addMarker(new MarkerOptions()
    .position(new LatLng(40.428639, -86.913775))
    .title("Hello world")
    .draggable(true));*/

    }
  public void mapdragdisable(View view) {
	if (mMapdragdisable.isChecked()){
		mMap.getUiSettings().setScrollGesturesEnabled(false);
		//if(widget == null) widget = new AtkMapWidget(mMap,rel,this);
	}
	else

	{
		mMap.getUiSettings().setScrollGesturesEnabled(true);			
	}
	
	
  }

  @Override
  public void init(MotionEvent ev) {
	// TODO Auto-generated method stub
	
	float touchx = ev.getX();
	float touchy = ev.getY();
	Marker m;
	Point touchPoint = new Point(Math.round(touchx), Math.round(touchy));
	Projection proj = mMap.getProjection();
	LatLng newPos = proj.fromScreenLocation(touchPoint);
	m = mMap.addMarker(new MarkerOptions().position(newPos).visible(false));
	//widget.showPop(m);
	marker.add(idx, newPos);
	polyline = mMap.addPolyline(new PolylineOptions() .add(newPos) .color(Color.YELLOW));
	prevx = touchx;
	prevy = touchy;
	flag = 0;
	Log.d("init", "add Marer: idx:" + Integer.toString(idx) + "Pos:" + Integer.toString(touchPoint.y));

  }

  @Override
  public void show() {
	// TODO Auto-generated method stub
	  if (mMapdragdisable.isChecked()){
	    PolygonOptions options = new PolygonOptions();
		  
	    for (int i=0; i <= idx ; i++){
	    	mMap.addMarker(new MarkerOptions()
              .position(marker.get(i))
              .draggable(true)
	    		);
	    	
	    	options.add(marker.get(i));
	    
	    
	    	
	    	
		}
		mMap.addPolygon(options
              .fillColor(Color.BLUE)        
		        );
		//widget.hidePop();
  
  
    
      }
  }

@Override
public void TrackPadDragListener() {
	// TODO Auto-generated method stub
	
}

@Override
public Marker AtkPassMarker() {
	
	return null;
}

@Override
public List<Marker> AtkPassMarkerList() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void UpdateSelectedMarker(boolean isButtonSelected, int index,
		int nextIndex, int prevIndex, int oldNextIndex, int oldPreviousIndex) {
	// TODO Auto-generated method stub
	
}
}


