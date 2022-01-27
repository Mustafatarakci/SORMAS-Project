/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.backend.caze.caseaccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.backend.access.AbstractEntityAccess;
import de.symeda.sormas.backend.access.AccessChangeOperation;
import de.symeda.sormas.backend.access.AccessChangeOperationType;
import de.symeda.sormas.backend.access.AccessibleEntity;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.BaseAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.sample.Sample;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.util.JurisdictionHelper;

@Stateless
@LocalBean
public class CaseAccessService extends BaseAdoService<CaseAccess> {

	public CaseAccessService() {
		super(CaseAccess.class);
	}

	public List<AccessibleEntity> getAccessibleCases(User user) {

		JurisdictionLevel jurisdictionLevel = user.getJurisdictionLevel();

		if (jurisdictionLevel == null || jurisdictionLevel == JurisdictionLevel.NATION) {
			throw new UnsupportedOperationException("User with ID " + user.getId() + " has no or national jurisdiction and can access all entities.");
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AccessibleEntity> cq = cb.createQuery(AccessibleEntity.class);
		Root<CaseAccess> root = cq.from(CaseAccess.class);

		cq.multiselect(root.get(CaseAccess.CAZE).get(AbstractDomainObject.ID), root.get(AbstractEntityAccess.PSEUDONYMIZED));

		/* Responsible user */
		Predicate predicate = cb.equal(root.get(AbstractEntityAccess.REPORTING_USER), user);

		/* Assigned user */
		predicate = CriteriaBuilderHelper.or(cb, predicate, cb.equal(root.get(AbstractEntityAccess.ASSIGNED_USER), user));

		/* Jurisdictions */
		switch (jurisdictionLevel) {
		case REGION:
			predicate = CriteriaBuilderHelper.or(cb, predicate, cb.equal(root.get(AbstractEntityAccess.REGION), user.getRegion()));
			break;
		case DISTRICT:
			predicate = CriteriaBuilderHelper.or(cb, predicate, cb.equal(root.get(AbstractEntityAccess.DISTRICT), user.getDistrict()));
			break;
		case COMMUNITY:
			predicate = CriteriaBuilderHelper.or(cb, predicate, cb.equal(root.get(AbstractEntityAccess.COMMUNITY), user.getCommunity()));
			break;
		case HEALTH_FACILITY:
			predicate = CriteriaBuilderHelper.or(cb, predicate, cb.equal(root.get(AbstractEntityAccess.FACILITY), user.getHealthFacility()));
			break;
		case LABORATORY:
		case EXTERNAL_LABORATORY:
			predicate = CriteriaBuilderHelper.or(cb, predicate, cb.equal(root.get(AbstractEntityAccess.FACILITY), user.getLaboratory()));
			break;
		case POINT_OF_ENTRY:
			predicate = CriteriaBuilderHelper.or(cb, predicate, cb.equal(root.get(AbstractEntityAccess.POINT_OF_ENTRY), user.getPointOfEntry()));
			break;
		default:
			throw new IllegalArgumentException(jurisdictionLevel.toString());
		}

		cq.where(predicate);
		cq.distinct(true);

		return em.createQuery(cq).getResultList();
	}

	public void insertAccessEntries(Case newCase) {

		// General access through user assignment or responsible jurisdiction
		CaseAccess generalAccess = CaseAccess.build(newCase, true, false);
		generalAccess.setReportingUser(newCase.getReportingUser());
		generalAccess.setAssignedUser(newCase.getSurveillanceOfficer());
		generalAccess.setRegion(newCase.getResponsibleRegion());
		generalAccess.setDistrict(newCase.getResponsibleDistrict());
		generalAccess.setCommunity(newCase.getResponsibleCommunity());
		generalAccess.setFacility(newCase.getHealthFacility());
		generalAccess.setPointOfEntry(newCase.getPointOfEntry());

		ensurePersisted(generalAccess);

		if (JurisdictionHelper.hasDifferingJurisdictions(newCase)) {
			// Access through differing place of stay jurisdiction
			CaseAccess jurisdictionAccess = CaseAccess.build(newCase, false, false);
			jurisdictionAccess.setRegion(newCase.getRegion());
			jurisdictionAccess.setDistrict(newCase.getDistrict());
			jurisdictionAccess.setCommunity(newCase.getCommunity());

			ensurePersisted(jurisdictionAccess);
		}
	}

	public void insertAccessEntryForContact(Contact newContact) {

		CaseAccess access = CaseAccess.build(newContact.getCaze(), false, true);
		access.setReportingUser(newContact.getReportingUser());
		access.setAssignedUser(newContact.getContactOfficer());
		access.setRegion(newContact.getRegion());
		access.setDistrict(newContact.getDistrict());
		access.setCommunity(newContact.getCommunity());
		access.setContact(newContact);

		ensurePersisted(access);
	}

	public void insertAccessEntryForSample(Sample newSample) {

		CaseAccess access = CaseAccess.build(newSample.getAssociatedCase(), false, true);
		access.setFacility(newSample.getLab());
		access.setSample(newSample);

		ensurePersisted(access);
	}

	public void updateAccessEntries(List<AccessChangeOperation> changeOperations, Case updatedCase) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// Update primary data
		CriteriaUpdate<CaseAccess> primaryCu = cb.createCriteriaUpdate(CaseAccess.class);
		Root<CaseAccess> primaryRoot = primaryCu.from(CaseAccess.class);

		changeOperations.stream().filter(AccessChangeOperation::isPrimaryData).forEach(o -> {
			try {
				primaryCu.set(
					primaryRoot.get(o.getProperty()),
					new PropertyDescriptor(o.getEntityProperty() != null ? o.getEntityProperty() : o.getProperty(), Case.class).getReadMethod()
						.invoke(updatedCase));
			} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException("Error updating case access entries: " + e.getMessage());
			}
		});

		primaryCu.set(AbstractDomainObject.CHANGE_DATE, Timestamp.from(Instant.now()));
		primaryCu
			.where(cb.and(cb.equal(primaryRoot.get(CaseAccess.CAZE), updatedCase), cb.isTrue(primaryRoot.get(AbstractEntityAccess.PRIMARY_DATA))));

		em.createQuery(primaryCu).executeUpdate();

		// Update or delete secondary data
		changeOperations.stream()
			.filter(
				o -> !o.isPrimaryData()
					&& (o.getOperationType() == AccessChangeOperationType.UPDATE || o.getOperationType() == AccessChangeOperationType.DELETE))
			.forEach(o -> {
				CriteriaUpdate<CaseAccess> secondaryCu = cb.createCriteriaUpdate(CaseAccess.class);
				Root<CaseAccess> secondaryRoot = secondaryCu.from(CaseAccess.class);

				try {
					secondaryCu.set(
						secondaryRoot.get(o.getProperty()),
						new PropertyDescriptor(o.getEntityProperty() != null ? o.getEntityProperty() : o.getProperty(), Case.class).getReadMethod()
							.invoke(updatedCase));
				} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
					throw new RuntimeException("Error updating or deleting case access entries: " + e.getMessage());
				}

				secondaryCu.set(AbstractDomainObject.CHANGE_DATE, Timestamp.from(Instant.now()));
				secondaryCu.where(
					cb.and(
						cb.isFalse(secondaryRoot.get(AbstractEntityAccess.PRIMARY_DATA)),
						cb.equal(secondaryRoot.get(o.getProperty()), o.getId()),
						cb.equal(secondaryRoot.get(CaseAccess.CAZE), updatedCase)));

				em.createQuery(secondaryCu).executeUpdate();
			});

		// Insert secondary data
		List<AccessChangeOperation> secondaryJurisdictionOperations = changeOperations.stream()
			.filter(
				o -> !o.isPrimaryData()
					&& o.getOperationType() == AccessChangeOperationType.INSERT
					&& (o.getProperty().equals(AbstractEntityAccess.REGION)
						|| o.getProperty().equals(AbstractEntityAccess.DISTRICT)
						|| o.getProperty().equals(AbstractEntityAccess.COMMUNITY)))
			.collect(Collectors.toList());

		if (!secondaryJurisdictionOperations.isEmpty()) {
			CaseAccess secondaryJurisdictionAccess = CaseAccess.build(updatedCase, false, false);
			for (AccessChangeOperation changeOperation : secondaryJurisdictionOperations) {
				try {
					new PropertyDescriptor(changeOperation.getProperty(), CaseAccess.class).getWriteMethod()
						.invoke(
							secondaryJurisdictionAccess,
							new PropertyDescriptor(
								changeOperation.getEntityProperty() != null ? changeOperation.getEntityProperty() : changeOperation.getProperty(),
								Case.class).getReadMethod().invoke(updatedCase));
				} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
					throw new RuntimeException("Error inserting case access entries: " + e.getMessage());
				}
			}

			persist(secondaryJurisdictionAccess);
		}
	}

	public void updateAccessEntryForContact(List<AccessChangeOperation> changeOperations, Contact updatedContact) {

		if (changeOperations.stream()
			.anyMatch(o -> o.getProperty().equals(CaseAccess.CAZE) && o.getOperationType() == AccessChangeOperationType.DELETE)) {
			// Delete the access entry if the source case has changed
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<CaseAccess> cd = cb.createCriteriaDelete(CaseAccess.class);
			Root<CaseAccess> root = cd.from(CaseAccess.class);
			cd.where(cb.equal(root.get(CaseAccess.CONTACT), updatedContact));
			em.createQuery(cd).executeUpdate();
		} else {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaUpdate<CaseAccess> cu = cb.createCriteriaUpdate(CaseAccess.class);
			Root<CaseAccess> root = cu.from(CaseAccess.class);

			changeOperations.stream().forEach(o -> {
				try {
					cu.set(
						root.get(o.getProperty()),
						new PropertyDescriptor(o.getEntityProperty() != null ? o.getEntityProperty() : o.getProperty(), Contact.class).getReadMethod()
							.invoke(updatedContact));
				} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
					throw new RuntimeException("Error updating case access entries: " + e.getMessage());
				}
			});

			cu.set(AbstractDomainObject.CHANGE_DATE, Timestamp.from(Instant.now()));
			cu.where(cb.equal(root.get(CaseAccess.CONTACT), updatedContact));

			em.createQuery(cu).executeUpdate();
		}
	}

	public void updateAccessEntryForSample(List<AccessChangeOperation> changeOperations, Sample updatedSample) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<CaseAccess> cu = cb.createCriteriaUpdate(CaseAccess.class);
		Root<CaseAccess> root = cu.from(CaseAccess.class);

		changeOperations.stream().forEach(o -> {
			try {
				cu.set(
					root.get(o.getProperty()),
					new PropertyDescriptor(o.getEntityProperty() != null ? o.getEntityProperty() : o.getProperty(), Sample.class).getReadMethod()
						.invoke(updatedSample));
			} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException("Error updating case access entries: " + e.getMessage());
			}
		});

		cu.set(AbstractDomainObject.CHANGE_DATE, Timestamp.from(Instant.now()));
		cu.where(cb.equal(root.get(CaseAccess.SAMPLE), updatedSample));

		em.createQuery(cu).executeUpdate();
	}

	public void deleteEmptyAccessEntries() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<CaseAccess> cd = cb.createCriteriaDelete(CaseAccess.class);
		Root<CaseAccess> root = cd.from(CaseAccess.class);

		cd.where(
			cb.and(
				cb.isFalse(root.get(AbstractEntityAccess.PRIMARY_DATA)),
				cb.isNull(root.get(AbstractEntityAccess.REPORTING_USER)),
				cb.isNull(root.get(AbstractEntityAccess.ASSIGNED_USER)),
				cb.isNull(root.get(AbstractEntityAccess.REGION)),
				cb.isNull(root.get(AbstractEntityAccess.DISTRICT)),
				cb.isNull(root.get(AbstractEntityAccess.COMMUNITY))));

		em.createQuery(cd).executeUpdate();
	}
}
