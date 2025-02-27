package de.symeda.sormas.backend.report;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.report.AggregateReportCriteria;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.backend.common.AdoServiceWithUserFilter;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.pointofentry.PointOfEntry;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserRole;

@Stateless
@LocalBean
public class AggregateReportService extends AdoServiceWithUserFilter<AggregateReport> {

	public AggregateReportService() {
		super(AggregateReport.class);
	}

	public Predicate createCriteriaFilter(
		AggregateReportCriteria criteria,
		CriteriaBuilder cb,
		CriteriaQuery<?> cq,
		From<AggregateReport, AggregateReport> from) {

		Predicate filter = null;

		if (criteria.getRegion() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.equal(from.join(AggregateReport.REGION, JoinType.LEFT).get(Region.UUID), criteria.getRegion().getUuid()));
		}
		if (criteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.equal(from.join(AggregateReport.DISTRICT, JoinType.LEFT).get(District.UUID), criteria.getDistrict().getUuid()));
		}
		if (criteria.getHealthFacility() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.equal(from.join(AggregateReport.HEALTH_FACILITY, JoinType.LEFT).get(Facility.UUID), criteria.getHealthFacility().getUuid()));
		}
		if (criteria.getPointOfEntry() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.equal(from.join(AggregateReport.POINT_OF_ENTRY, JoinType.LEFT).get(PointOfEntry.UUID), criteria.getPointOfEntry().getUuid()));
		}
		if (criteria.getEpiWeekFrom() != null || criteria.getEpiWeekTo() != null) {
			if (criteria.getEpiWeekFrom() == null) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.le(from.get(AggregateReport.YEAR), criteria.getEpiWeekTo().getYear()));
				filter = CriteriaBuilderHelper.and(cb, filter, cb.le(from.get(AggregateReport.EPI_WEEK), criteria.getEpiWeekTo().getWeek()));
			} else if (criteria.getEpiWeekTo() == null) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.ge(from.get(AggregateReport.YEAR), criteria.getEpiWeekFrom().getYear()));
				filter = CriteriaBuilderHelper.and(cb, filter, cb.ge(from.get(AggregateReport.EPI_WEEK), criteria.getEpiWeekFrom().getWeek()));
			} else {
				filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(from.get(AggregateReport.YEAR), criteria.getEpiWeekFrom().getYear(), criteria.getEpiWeekTo().getYear()));
				filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(from.get(AggregateReport.EPI_WEEK), criteria.getEpiWeekFrom().getWeek(), criteria.getEpiWeekTo().getWeek()));
			}
		}

		if (criteria.getDisease() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(AggregateReport.DISEASE), criteria.getDisease()));
		}

		return filter;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, AggregateReport> from) {

		User currentUser = getCurrentUser();
		if (currentUser == null) {
			return null;
		}

		final JurisdictionLevel jurisdictionLevel = currentUser.getJurisdictionLevel();
		if ((jurisdictionLevel == JurisdictionLevel.NATION && !UserRole.isPortHealthUser(currentUser.getUserRoles()))) {
			return null;
		}

		// Whoever created the weekly report is allowed to access it
		Join<AggregateReport, User> reportingUser = from.join(AggregateReport.REPORTING_USER, JoinType.LEFT);
		Predicate filter = cb.equal(reportingUser, currentUser);

		switch (jurisdictionLevel) {
		case REGION:
			final Region region = currentUser.getRegion();
			if (region != null) {
				filter = cb.or(filter, cb.equal(from.get(AggregateReport.REGION), region));
			}
			break;
		case DISTRICT:
			final District district = currentUser.getDistrict();
			if (district != null) {
				filter = cb.or(filter, cb.equal(from.get(AggregateReport.DISTRICT), district));
			}
			break;
		case HEALTH_FACILITY:
			final Facility healthFacility = currentUser.getHealthFacility();
			if (healthFacility != null) {
				filter = cb.or(filter, cb.equal(from.get(AggregateReport.HEALTH_FACILITY), healthFacility));
			}
			break;
		case POINT_OF_ENTRY:
			final PointOfEntry pointOfEntry = currentUser.getPointOfEntry();
			if (pointOfEntry != null) {
				filter = cb.or(filter, cb.equal(from.get(AggregateReport.POINT_OF_ENTRY), pointOfEntry));
			}
			break;
		default:
		}

		return filter;
	}

	public List<AggregateReport> findBy(AggregateReportCriteria aggregateReportCriteria, User user) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AggregateReport> cq = cb.createQuery(getElementClass());
		Root<AggregateReport> from = cq.from(getElementClass());

		Predicate filter = createCriteriaFilter(aggregateReportCriteria, cb, cq, from);

		if (user != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, createUserFilter(cb, cq, from));
		}
		if (filter != null) {
			cq.where(filter);
		}

		return em.createQuery(cq).getResultList();
	}
}
