/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.mg6.android.maps.extensions;

import java.util.List;

import android.location.Location;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

public interface GoogleMap {

	int MAP_TYPE_HYBRID = com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
	int MAP_TYPE_NONE = com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
	int MAP_TYPE_NORMAL = com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
	int MAP_TYPE_SATELLITE = com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
	int MAP_TYPE_TERRAIN = com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

	Circle addCircle(CircleOptions circleOptions);

	GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions);

	Marker addMarker(MarkerOptions markerOptions);

	Polygon addPolygon(PolygonOptions polygonOptions);

	Polyline addPolyline(PolylineOptions polylineOptions);

	TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions);

	void animateCamera(CameraUpdate cameraUpdate, CancelableCallback cancelableCallback);

	void animateCamera(CameraUpdate cameraUpdate, int time, CancelableCallback cancelableCallback);

	void animateCamera(CameraUpdate cameraUpdate);

	void clear();

	CameraPosition getCameraPosition();

	int getMapType();

	/**
	 * WARNING: may be changed in future API when this is fixed: http://code.google.com/p/gmaps-api-issues/issues/detail?id=5106
	 */
	List<Circle> getCircles();

	List<GroundOverlay> getGroundOverlays();

	List<Marker> getMarkers();

	Marker getMarkerShowingInfoWindow();

	List<Polygon> getPolygons();

	List<Polyline> getPolylines();

	List<TileOverlay> getTileOverlays();

	float getMaxZoomLevel();

	float getMinZoomLevel();

	Location getMyLocation();

	Projection getProjection();

	UiSettings getUiSettings();

	boolean isIndoorEnabled();

	boolean isMyLocationEnabled();

	boolean isTrafficEnabled();

	void moveCamera(CameraUpdate cameraUpdate);

	void setClustering(ClusteringSettings clusteringSettings);

	boolean setIndoorEnabled(boolean indoorEnabled);

	void setInfoWindowAdapter(InfoWindowAdapter infoWindowAdapter);

	void setLocationSource(LocationSource locationSource);

	void setMapType(int mapType);

	void setMyLocationEnabled(boolean myLocationEnabled);

	void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener);

	void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener);

	void setOnMapClickListener(OnMapClickListener onMapClickListener);

	void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener);

	void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener);

	void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener);

	void setOnMyLocationChangeListener(OnMyLocationChangeListener onMyLocationChangeListener);

	void setTrafficEnabled(boolean trafficEnabled);

	void stopAnimation();

	interface CancelableCallback extends com.google.android.gms.maps.GoogleMap.CancelableCallback {

		@Override
		void onCancel();

		@Override
		void onFinish();
	}

	interface InfoWindowAdapter {

		View getInfoContents(Marker marker);

		View getInfoWindow(Marker marker);
	}

	interface OnCameraChangeListener extends com.google.android.gms.maps.GoogleMap.OnCameraChangeListener {

		@Override
		void onCameraChange(CameraPosition cameraPosition);
	}

	interface OnInfoWindowClickListener {

		void onInfoWindowClick(Marker marker);
	}

	interface OnMapClickListener extends com.google.android.gms.maps.GoogleMap.OnMapClickListener {

		@Override
		void onMapClick(LatLng position);
	}

	interface OnMapLongClickListener extends com.google.android.gms.maps.GoogleMap.OnMapLongClickListener {

		@Override
		void onMapLongClick(LatLng position);
	}

	interface OnMarkerClickListener {

		boolean onMarkerClick(Marker marker);
	}

	interface OnMarkerDragListener {

		void onMarkerDragStart(Marker marker);

		void onMarkerDrag(Marker marker);

		void onMarkerDragEnd(Marker marker);
	}

	interface OnMyLocationChangeListener extends com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener {

		@Override
		public void onMyLocationChange(Location location);
	}
}