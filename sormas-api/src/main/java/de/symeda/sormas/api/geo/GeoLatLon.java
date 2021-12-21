package de.symeda.sormas.api.geo;

import java.io.Serializable;

public class GeoLatLon implements Serializable, Cloneable {

	private static final long serialVersionUID = 6016397482506424761L;

	private final Double lat;
	private final Double lon;

	public GeoLatLon(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLon() {
		return lon;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GeoLatLon other = (GeoLatLon) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat)) {
			return false;
		}
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon)) {
			return false;
		}
		return true;
	}

	@Override
	public GeoLatLon clone() {

		try {
			return (GeoLatLon) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Clone failed", e);
		}
	}
}
