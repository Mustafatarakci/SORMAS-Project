package de.symeda.sormas.backend.externalmessage.labmessage;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.backend.common.BaseAdoService;
import de.symeda.sormas.backend.common.DeletableAdo;

@Stateless
@LocalBean
public class TestReportService extends BaseAdoService<TestReport> {

	public TestReportService() {
		super(TestReport.class);
	}

	/**
	 * Creates a default filter that should be used as the basis of queries in this service..
	 * This essentially removes {@link DeletableAdo#deleted} test reports from the queries.
	 */
	public Predicate createDefaultFilter(CriteriaBuilder cb, Root<TestReport> root) {
		return cb.isFalse(root.join(TestReport.LAB_MESSAGE, JoinType.LEFT).get(DeletableAdo.DELETED));
	}

}
