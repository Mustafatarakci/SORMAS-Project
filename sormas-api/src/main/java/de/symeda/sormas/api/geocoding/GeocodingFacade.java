package de.symeda.sormas.api.geocoding;

import javax.ejb.Remote;

import de.symeda.sormas.api.geo.GeoLatLon;

@Remote
public interface GeocodingFacade {

	boolean isEnabled();

	GeoLatLon getLatLon(String street, String houseNumber, String postalCode, String city);
}
