package de.symeda.sormas.backend.activityascase;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.backend.common.BaseAdoService;

@Stateless
@LocalBean
public class ActivityAsCaseService extends BaseAdoService<ActivityAsCase> {

	public ActivityAsCaseService() {
		super(ActivityAsCase.class);
	}
}
