package de.symeda.sormas.backend.geocoding;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.geocoding.GeocodingFacade;
import de.symeda.sormas.api.geo.GeoLatLon;

@Stateless(name = "GeocodingFacade")
public class GeocodingFacadeEjb implements GeocodingFacade {

	@EJB
	private GeocodingService geocodingService;

	@Override
	public boolean isEnabled() {
		return geocodingService.isEnabled();
	}

	@Override
	public GeoLatLon getLatLon(String street, String houseNumber, String postalCode, String city) {

		if (StringUtils.isNotBlank(street) && (StringUtils.isNotBlank(city) || StringUtils.isNotBlank(postalCode))) {
			return geocodingService.getLatLon(new LocationQuery(houseNumber, street, postalCode, city));
		}

		return null;
	}
}
