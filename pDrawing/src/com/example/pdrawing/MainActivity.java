package com.example.pdrawing;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;

import java.util.ArrayList;
import java.util.List;



import com.example.librarytest.AtkMapWidget;
import com.example.librarytest.AtkMapWidget.AtkMapWidgetListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements 
	AtkMapWidgetListener,OnMapClickListener {
	
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
	private Marker m;
	private Polygon poly;
	   
	static final CameraPosition PURDUE =
          new CameraPosition.Builder().target(new LatLng(40.428639, -86.913775))
                  .zoom(18)
                  .build();
	
	
  
	
  private CheckBox mMapdragdisable;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rel = (RelativeLayout) findViewById(R.id.rel);
	    setUpMapIfNeeded();
	    mMap.setOnMapClickListener(this);
	    mMapdragdisable = (CheckBox) findViewById(R.id.disable);
		polyline = mMap.addPolyline(new PolylineOptions() 
		.add(new LatLng(0,0)).color(Color.YELLOW).width(2));
		poly = mMap.addPolygon(new PolygonOptions()
		.add(new LatLng(0, 0)).strokeWidth(2)
		.strokeColor(Color.BLACK).fillColor(0x7FB5B1E0));
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
	     mMap.setMapType(MAP_TYPE_SATELLITE);
	     mMap.moveCamera(CameraUpdateFactory.newCameraPosition(PURDUE));
	    }
  public void mapdragdisable(View view) {
	if (mMapdragdisable.isChecked()){
		if(widget == null) widget = new AtkMapWidget(mMap,rel,this);
		widget.EnablePop(false);
		widget.hideSelectionButton();
		widget.maxTrackPad(200);
		widget.shiftAtkButtonsUp(120);
	}
	else

	{
		for (int i=0; i <= idx ; i++){
	    	mMap.addMarker(new MarkerOptions()
              .position(marker.get(i))
              .draggable(true));	
		}
		polyline.remove();
		m.remove();
	}
  }
	@Override
	public void TrackPadDragListener() {
		PolylineOptions options = new PolylineOptions();
		int i;
		
		float touchx;
		float touchy;
		
		/*prevx = mMap.getProjection().toScreenLocation(
				polyline.getPoints().get(polyline.getPoints().size()-1)).x;
		prevy = mMap.getProjection().toScreenLocation(
				polyline.getPoints().get(polyline.getPoints().size()-1)).y;*/
		LatLng newPos = m.getPosition();
		touchx = mMap.getProjection().toScreenLocation(
				m.getPosition()).x;
		touchy = mMap.getProjection().toScreenLocation(
				m.getPosition()).y;
		 if (mMapdragdisable.isChecked()){
			 dis = Math.hypot(touchx - prevx, touchy - prevy);
			 if ((flag == 0) && (dis > delta)){
				 flag = 1;
				 x2 = touchx - prevx;
				 y2 = touchy - prevy;
			 }
			 if (flag == 1){
				 x1 = touchx - prevx;
				 y1 = touchy - prevy;
		         perp = Math.abs(x1 * y2 - x2 * y1) / Math.hypot(x2, y2);	 
			 }
			 else{
		         perp = 0;
			 }
		     if (perp > away){
		    	 idx++;

		    	 marker.add(idx, newPos);
		    	 prevx = touchx;
		    	 prevy = touchy;
		    	 flag = 0;
		     }
			 for (i = 0; i <= idx; i++){
				 options.add(marker.get(i));
			 }
			 options.add(newPos);
			 polyline.setPoints(options.getPoints());
			 poly.setPoints(marker);
		 }
	}
	
	@Override
	public Marker AtkPassMarker() {
		return m;
	}
	
	@Override
	public List<Marker> AtkPassMarkerList() {
		return null;
	}
	
	@Override
	public void UpdateSelectedMarker(boolean isButtonSelected, int index,
			int nextIndex, int prevIndex, int oldNextIndex, int oldPreviousIndex) {	
	}

	@Override
	public void onMapClick(LatLng arg0) {
		if(mMapdragdisable.isChecked()){
			m = mMap.addMarker(new MarkerOptions().position(arg0));
			if(marker !=null) {
				marker.clear();
				idx = 0;
				flag = 0;
			}
		}
		marker.add(idx, arg0);
		polyline = mMap.addPolyline(new PolylineOptions() .add(arg0) .color(Color.YELLOW).width(2));
		prevx = mMap.getProjection().toScreenLocation(arg0).x;
		prevy = mMap.getProjection().toScreenLocation(arg0).x;
		flag = 0;
	}
}


