package de.symeda.sormas.backend.epidata;

import java.sql.Timestamp;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.backend.common.BaseAdoService;
import de.symeda.sormas.backend.common.ChangeDateFilterBuilder;
import de.symeda.sormas.backend.exposure.Exposure;

@Stateless
@LocalBean
public class EpiDataService extends BaseAdoService<EpiData> {

	public EpiDataService() {
		super(EpiData.class);
	}

	public EpiData createEpiData() {

		EpiData epiData = new EpiData();
		epiData.setUuid(DataHelper.createUuid());
		return epiData;
	}

	@Override
	public Predicate createChangeDateFilter(CriteriaBuilder cb, From<?, EpiData> epiData, Timestamp date) {
		return addChangeDateFilters(new ChangeDateFilterBuilder(cb, date), epiData).build();
	}

	public ChangeDateFilterBuilder addChangeDateFilters(ChangeDateFilterBuilder filterBuilder, From<?, EpiData> epiData) {
		Join<EpiData, Exposure> exposures = epiData.join(EpiData.EXPOSURES, JoinType.LEFT);
		Join<EpiData, ActivityAsCase> activitiesAsCaseJoin = epiData.join(EpiData.ACTIVITIES_AS_CASE, JoinType.LEFT);

		return filterBuilder.add(epiData)
			.add(exposures)
			.add(exposures, Exposure.LOCATION)
			.add(activitiesAsCaseJoin)
			.add(activitiesAsCaseJoin, ActivityAsCase.LOCATION);
	}
}
