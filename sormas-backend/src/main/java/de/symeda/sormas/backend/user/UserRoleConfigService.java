package de.symeda.sormas.backend.user;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.AdoServiceWithUserFilter;

@Stateless
@LocalBean
public class UserRoleConfigService extends AdoServiceWithUserFilter<UserRoleConfig> {

	@Resource
	private SessionContext sessionContext;

	public UserRoleConfigService() {
		super(UserRoleConfig.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, UserRoleConfig> from) {
		// a user can read all user role configurations
		return null;
	}

	public UserRoleConfig getByUserRole(UserRole userRole) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		ParameterExpression<UserRole> userRoleParam = cb.parameter(UserRole.class, UserRoleConfig.USER_ROLE);
		CriteriaQuery<UserRoleConfig> cq = cb.createQuery(UserRoleConfig.class);
		Root<UserRoleConfig> from = cq.from(UserRoleConfig.class);
		cq.where(cb.equal(from.get(UserRoleConfig.USER_ROLE), userRoleParam));

		TypedQuery<UserRoleConfig> q = em.createQuery(cq).setParameter(userRoleParam, userRole);

		UserRoleConfig entity = q.getResultList().stream().findFirst().orElse(null);

		return entity;
	}

	public List<String> getDeletedUuids(Date since) {

		String queryString = "SELECT " + AbstractDomainObject.UUID + " FROM " + UserRoleConfig.TABLE_NAME + AbstractDomainObject.HISTORY_TABLE_SUFFIX
			+ " h" + " WHERE sys_period @> CAST (?1 AS timestamptz)" + " AND NOT EXISTS (SELECT FROM " + UserRoleConfig.TABLE_NAME + " WHERE "
			+ AbstractDomainObject.ID + " = h." + AbstractDomainObject.ID + ")";
		Query nativeQuery = em.createNativeQuery(queryString);
		nativeQuery.setParameter(1, since);
		@SuppressWarnings("unchecked")
		List<String> results = (List<String>) nativeQuery.getResultList();
		return results;
	}
}
