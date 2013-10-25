package com.example.librarytest;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

public class AtkMarker {
	private GoogleMap map = null;
	private RelativeLayout rel = null;
	private Activity active;
	private RelativeLayout rel2, rel3;
	private float dens;
	private MapFragment mapf;
	private Marker mkr1;
	private Button trackpad;
	private int shift = 0;
	private float h, w;
	private AtkMarkerListener listener;


	public interface AtkMarkerListener {
		public void TrackPadDragListener();
		public Marker AtkMarkerUpload();
	}
	
	public AtkMarker(GoogleMap map, RelativeLayout rel, Activity active) {
		// Initialization
		this.map = map;
		this.rel = rel;
		this.active = active;
		this.listener = (AtkMarkerListener) active;
		this.dens = Resources.getSystem().getDisplayMetrics().density;
		
		// Create the magnifier window
		createRel();
		
		// Create the trackpad
		h = rel.getHeight() / dens;
		w = rel.getWidth() / dens;
		trackpad = addbutton(trackpad, R.drawable.move_red, Math.round(75 * dens),
				Math.round(100 * dens), ((w / 2) - 50) * dens, (h - 75 - shift) * dens);
		rel.addView(trackpad);
		setTrack();
		
	}
	
	private void setTrack(){
		trackpad.setOnTouchListener(new View.OnTouchListener() {
			Point pmkr = new Point();
			Point pmkr2 = new Point();
			float touchx;
			float touchy;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mkr1 = listener.AtkMarkerUpload();
					if(mkr1==null)break;
					mvScreen();
					pmkr.set(
							map.getProjection().toScreenLocation(
									mkr1.getPosition()).x,
							map.getProjection().toScreenLocation(
									mkr1.getPosition()).y);
					pmkr2.set((int) event.getX(), (int) event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					if(mkr1==null)break;
					touchx = (event.getX() - pmkr2.x) + pmkr.x;
					touchy = (event.getY() - pmkr2.y) + pmkr.y;
					mkr1.setPosition(
							map.getProjection().fromScreenLocation(
									new Point(Math.round(touchx), Math
											.round(touchy))));
					mvScreen();
					listener.TrackPadDragListener();
					
					break;
				case MotionEvent.ACTION_UP:
					if(mkr1==null)break;
					resetrl();
					listener.TrackPadDragListener();
					break;

				}
				return false;
			}
		});
	}
	public void showTrack(){
		trackpad.setVisibility(View.VISIBLE);
	}
	public void hideTrack(){
		trackpad.setVisibility(View.INVISIBLE);
	}
	public void showPop(Marker mkr1){
		this.mkr1 = mkr1;
		mvScreen();
	}
	public void movePop(){
		mvScreen();
	}
	public void shiftUp(int shift){
		this.shift = shift;
		trackpad.setY((h - 75 - shift) * dens);
	}
	public void hidePop(){
		resetrl();
	}
	public void clearAtkMarker(){
		rel.removeView(trackpad);
		rel2.removeAllViews();
		rel3.removeAllViews();
		rel.removeView(rel2);
		rel.removeView(rel3);
	}
	private void createRel() {
		rel2 = new RelativeLayout(active.getBaseContext());
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				(int) (150 * dens), (int) (150 * dens));
		rel2.setLayoutParams(rlp);
		rel2.setPadding(2,2,2,2);
		rel2.setBackgroundColor(Color.WHITE);
		rel.addView(rel2);
		rel2.setId(100);	//this need to be corrected
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_SATELLITE).compassEnabled(false)
				.rotateGesturesEnabled(false).tiltGesturesEnabled(false)
				.zoomControlsEnabled(false).camera(map.getCameraPosition());
		MapFragment.newInstance(options);
		mapf = MapFragment.newInstance(options);
		FragmentTransaction fragmentTransaction = active.getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(100, mapf);
		fragmentTransaction.commit();
		rel3 = new RelativeLayout(active.getBaseContext());
		RelativeLayout.LayoutParams rlp3 = new RelativeLayout.LayoutParams(
				(int) (150 * dens), (int) (150 * dens));
		rel3.setLayoutParams(rlp3);
		rel.addView(rel3);
		ImageView img = new ImageView(active.getBaseContext());
		img.setBackgroundResource(R.drawable.cross_vertex_5);
		img.setX(30 * dens);
		img.setY(30 * dens);
		rel3.addView(img, (int) (90 * dens), (int) (90 * dens));
		resetrl();
	}
	
	private void resetrl() {
		rel2.setX(-200 * dens);
		rel2.setY(-200 * dens);
		rel3.setX(rel2.getX());
		rel3.setY(rel2.getY());
	}
	private void mvScreen() {
		fixBeering();
		mapf.getMap().moveCamera(
				CameraUpdateFactory.newLatLngZoom(mkr1
						.getPosition(), map.getCameraPosition().zoom + 2));
		rel2.setX(map.getProjection().toScreenLocation(
				mkr1.getPosition()).x
				- 78 * dens);
		rel2.setY(map.getProjection().toScreenLocation(
				mkr1.getPosition()).y
				- 170 * dens);
		rel3.setX(rel2.getX());
		rel3.setY(rel2.getY());
	}
	private Button addbutton(Button btn, int dr, int height, int width,
			float x, float y) {
		btn = new Button(active.getBaseContext());
		btn.setBackgroundResource(dr);
		btn.setHeight(height);
		btn.setWidth(width);
		btn.setX(x);
		btn.setY(y);
		btn.setVisibility(View.INVISIBLE);
		return btn;
	}	
	private void fixBeering(){
		if (mapf.getMap().getCameraPosition().bearing != map
				.getCameraPosition().bearing) {
			mapf.getMap().moveCamera(
					CameraUpdateFactory.newCameraPosition(map.getCameraPosition()));
		}
	}

}
