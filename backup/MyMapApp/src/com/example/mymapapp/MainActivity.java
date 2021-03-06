package com.example.mymapapp;

import com.example.mymapapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;


public class MainActivity extends FragmentActivity
implements OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener {

private GoogleMap mMap;
private TextView mTapTextView;
private TextView mCameraTextView;

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
    if (mMap != null) {
        setUpMap();
    }
}
}

private void setUpMap() {
mMap.setOnMapClickListener(this);
mMap.setOnMapLongClickListener(this);
mMap.setOnCameraChangeListener(this);
mMap.setMyLocationEnabled(true); //shows the location button and enables it
}

@Override
public void onMapClick(LatLng point) {
mTapTextView.setText("tapped, point=" + point);

}

@Override
public void onMapLongClick(LatLng point) {
mTapTextView.setText("long pressed, point=" + point);
}

@Override
public void onCameraChange(final CameraPosition position) {
mCameraTextView.setText(position.toString());
}
}
