package de.symeda.sormas.ui.map;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * State of the map which is transferred to the web browser whenever a property
 * changed.
 */
public class LeafletState extends JavaScriptComponentState {

	private static final long serialVersionUID = -8746016099669605525L;

	private int zoom;
	private double centerLatitude;
	private double centerLongitude;

	private boolean tileLayerVisible;
	private float tileLayerOpacity;

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public double getCenterLatitude() {
		return centerLatitude;
	}

	public void setCenterLatitude(double centerLatitude) {
		this.centerLatitude = centerLatitude;
	}

	public double getCenterLongitude() {
		return centerLongitude;
	}

	public void setCenterLongitude(double centerLongitude) {
		this.centerLongitude = centerLongitude;
	}

	public boolean isTileLayerVisible() {
		return tileLayerVisible;
	}

	public void setTileLayerVisible(boolean tileLayerVisible) {
		this.tileLayerVisible = tileLayerVisible;
	}

	public float getTileLayerOpacity() {
		return tileLayerOpacity;
	}

	public void setTileLayerOpacity(float tileLayerOpacity) {
		this.tileLayerOpacity = tileLayerOpacity;
	}
}
