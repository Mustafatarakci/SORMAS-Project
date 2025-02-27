package de.symeda.sormas.backend.campaign;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.campaign.CampaignCriteria;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.AbstractCoreAdoService;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;

@Stateless
@LocalBean
public class CampaignService extends AbstractCoreAdoService<Campaign> {

	public CampaignService() {
		super(Campaign.class);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Predicate createUserFilterInternal(CriteriaBuilder cb, CriteriaQuery cq, From<?, Campaign> from) {
		return createUserFilter(new CampaignQueryContext(cb, cq, from));
	}

	public Predicate createUserFilter(CampaignQueryContext queryContext) {
		// A user who has access to CampaignView can read all campaigns
		return null;
	}

	public Predicate buildCriteriaFilter(CampaignQueryContext queryContext, CampaignCriteria campaignCriteria) {

		CriteriaBuilder cb = queryContext.getCriteriaBuilder();
		From<?, Campaign> from = queryContext.getRoot();

		Predicate filter = null;
		if (campaignCriteria.getDeleted() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Campaign.DELETED), campaignCriteria.getDeleted()));
		}
		if (campaignCriteria.getStartDateAfter() != null || campaignCriteria.getStartDateBefore() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.between(from.get(Campaign.START_DATE), campaignCriteria.getStartDateAfter(), campaignCriteria.getStartDateBefore()));
		}
		if (campaignCriteria.getEndDateAfter() != null || campaignCriteria.getEndDateBefore() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.between(from.get(Campaign.END_DATE), campaignCriteria.getEndDateAfter(), campaignCriteria.getEndDateAfter()));
		}
		if (campaignCriteria.getFreeText() != null) {
			String[] textFilters = campaignCriteria.getFreeText().split("\\s+");
			for (String textFilter : textFilters) {
				if (DataHelper.isNullOrEmpty(textFilter)) {
					continue;
				}

				Predicate likeFilters = cb.or(
					CriteriaBuilderHelper.unaccentedIlike(cb, from.get(Campaign.NAME), textFilter),
					CriteriaBuilderHelper.ilike(cb, from.get(Campaign.UUID), textFilter));
				filter = CriteriaBuilderHelper.and(cb, filter, likeFilters);
			}
		}
		if (campaignCriteria.getRelevanceStatus() != null) {
			if (campaignCriteria.getRelevanceStatus() == EntityRelevanceStatus.ACTIVE) {
				filter = CriteriaBuilderHelper
					.and(cb, filter, cb.or(cb.equal(from.get(Campaign.ARCHIVED), false), cb.isNull(from.get(Campaign.ARCHIVED))));
			} else if (campaignCriteria.getRelevanceStatus() == EntityRelevanceStatus.ARCHIVED) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Campaign.ARCHIVED), true));
			}
		}
		return filter;
	}

	public List<String> getAllActiveUuids() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Campaign> from = cq.from(getElementClass());

		Predicate filter = cb.and();

		if (getCurrentUser() != null) {
			Predicate userFilter = createUserFilter(cb, cq, from);
			filter = CriteriaBuilderHelper.and(cb, createActiveCampaignsFilter(cb, from), userFilter);
		}

		cq.where(filter);
		cq.select(from.get(Campaign.UUID));

		return em.createQuery(cq).getResultList();
	}

	public List<Campaign> getAllActive() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Campaign> cq = cb.createQuery(getElementClass());
		Root<Campaign> from = cq.from(getElementClass());
		cq.where(createActiveCampaignsFilter(cb, from));
		cq.orderBy(cb.desc(from.get(AbstractDomainObject.CHANGE_DATE)));

		return em.createQuery(cq).getResultList();
	}

	public Predicate createActiveCampaignsFilter(CriteriaBuilder cb, Root<Campaign> root) {
		return cb.and(cb.isFalse(root.get(Campaign.ARCHIVED)), cb.isFalse(root.get(Campaign.DELETED)));
	}

	@Override
	public List<Campaign> getAllAfter(Date since) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Campaign> cq = cb.createQuery(getElementClass());
		Root<Campaign> root = cq.from(getElementClass());

		Predicate filter = createUserFilter(cb, cq, root);
		if (since != null) {
			Predicate dateFilter = createChangeDateFilter(cb, root, since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}
		if (filter != null) {
			cq.where(filter);
		}
		cq.orderBy(cb.desc(root.get(AbstractDomainObject.CHANGE_DATE)));

		return em.createQuery(cq).getResultList();
	}

}
