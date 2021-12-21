package de.symeda.sormas.backend.location;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.backend.common.BaseAdoService;

@Stateless
@LocalBean
public class LocationService extends BaseAdoService<Location> {

	public LocationService() {
		super(Location.class);
	}

}
