package de.symeda.sormas.backend.symptoms;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.backend.common.BaseAdoService;

@Stateless
@LocalBean
public class SymptomsService extends BaseAdoService<Symptoms> {

	public SymptomsService() {
		super(Symptoms.class);
	}

}
