package de.symeda.sormas.backend.hospitalization;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.BaseAdoService;

@Stateless
@LocalBean
public class PreviousHospitalizationService extends BaseAdoService<PreviousHospitalization> {

	public PreviousHospitalizationService() {
		super(PreviousHospitalization.class);
	}

	public List<Object[]> getFirstPreviousHospitalizations(List<Long> caseIds) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<PreviousHospitalization> prevHospRoot = cq.from(getElementClass());
		Join<PreviousHospitalization, Hospitalization> hospitalizationJoin =
			prevHospRoot.join(PreviousHospitalization.HOSPITALIZATION, JoinType.LEFT);
		Root<Case> caseRoot = cq.from(Case.class);
		Join<Case, Hospitalization> caseHospitalizationJoin = caseRoot.join(Case.HOSPITALIZATION, JoinType.LEFT);

		cq.multiselect(caseRoot.get(Case.ID), prevHospRoot.get(PreviousHospitalization.ID));

		Expression<String> caseIdsExpression = caseRoot.get(Case.ID);
		cq.where(
			cb.and(
				caseIdsExpression.in(caseIds),
				cb.equal(hospitalizationJoin.get(Hospitalization.ID), caseHospitalizationJoin.get(Hospitalization.ID))));
		cq.orderBy(cb.asc(caseRoot.get(Case.ID)), cb.asc(prevHospRoot.get(PreviousHospitalization.ADMISSION_DATE)));

		return em.createQuery(cq).getResultList();
	}
}
