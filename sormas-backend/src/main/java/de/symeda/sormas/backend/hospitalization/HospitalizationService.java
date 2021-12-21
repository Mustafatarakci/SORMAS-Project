package de.symeda.sormas.backend.hospitalization;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

@Stateless
@LocalBean
public class HospitalizationService extends BaseAdoService<Hospitalization> {

	public HospitalizationService() {
		super(Hospitalization.class);
	}

	public Hospitalization createHospitalization() {

		Hospitalization hospitalization = new Hospitalization();
		hospitalization.setUuid(DataHelper.createUuid());
		return hospitalization;
	}

}
