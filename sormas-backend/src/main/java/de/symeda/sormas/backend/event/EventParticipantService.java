/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.backend.event;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EditPermissionType;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.caze.VaccinationStatus;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.event.EventParticipantCriteria;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.backend.common.AbstractCoreAdoService;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.ChangeDateBuilder;
import de.symeda.sormas.backend.common.ChangeDateFilterBuilder;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.contact.ContactQueryContext;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.person.PersonQueryContext;
import de.symeda.sormas.backend.sample.Sample;
import de.symeda.sormas.backend.sample.SampleService;
import de.symeda.sormas.backend.sormastosormas.share.shareinfo.SormasToSormasShareInfoService;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.util.IterableHelper;
import de.symeda.sormas.backend.util.JurisdictionHelper;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.QueryHelper;

@Stateless
@LocalBean
public class EventParticipantService extends AbstractCoreAdoService<EventParticipant> {

	@EJB
	private EventService eventService;
	@EJB
	private SampleService sampleService;
	@EJB
	private SormasToSormasShareInfoService sormasToSormasShareInfoService;

	public EventParticipantService() {
		super(EventParticipant.class);
	}

	public List<EventParticipant> getAllAfter(Date date, Integer batchSize, String lastSynchronizedUuid) {
		return getAllAfter(date, null, batchSize, lastSynchronizedUuid);
	}

	public List<EventParticipant> getAllAfter(Date date, User user, Integer batchSize, String lastSynchronizedUuid) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(getElementClass());
		Root<EventParticipant> from = cq.from(getElementClass());

		EventParticipantQueryContext eventParticipantQueryContext = new EventParticipantQueryContext(cb, cq, from);

		Join<EventParticipant, Event> event = eventParticipantQueryContext.getJoins().getEvent();

		Predicate filter = cb
			.and(cb.or(cb.isFalse(event.get(Event.ARCHIVED)), cb.isNull(event.get(Event.ARCHIVED))), cb.isFalse(from.get(EventParticipant.ARCHIVED)));

		filter = cb.and(filter, createDefaultFilter(cb, from));

		if (user != null) {
			Predicate userFilter = createUserFilter(eventParticipantQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, userFilter);
		}

		if (date != null) {
			Predicate dateFilter = createChangeDateFilter(cb, from, date, lastSynchronizedUuid);
			filter = cb.and(filter, dateFilter);
		}

		cq.where(filter);
		cq.distinct(true);

		return getBatchedQueryResults(cb, cq, from, batchSize);
	}

	public List<String> getAllActiveUuids(User user) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<EventParticipant> from = cq.from(getElementClass());
		EventParticipantQueryContext eventParticipantQueryContext = new EventParticipantQueryContext(cb, cq, from);

		Join<EventParticipant, Event> event = eventParticipantQueryContext.getJoins().getEvent();

		Predicate filter = cb.or(cb.equal(event.get(Event.ARCHIVED), false), cb.isNull(event.get(Event.ARCHIVED)));

		filter = cb.and(filter, createDefaultFilter(cb, from));

		if (user != null) {
			Predicate userFilter = createUserFilter(eventParticipantQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, userFilter);
		}

		cq.where(filter);
		cq.select(from.get(EventParticipant.UUID));

		return em.createQuery(cq).getResultList();
	}

	public List<EventParticipant> getAllByEventAfter(Date date, Event event) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(getElementClass());
		Root<EventParticipant> from = cq.from(getElementClass());

		Predicate filter = cb.equal(from.get(EventParticipant.EVENT), event);
		if (date != null) {
			filter = cb.and(filter, createChangeDateFilter(cb, from, date));
		}
		cq.where(filter);
		cq.orderBy(cb.desc(from.get(EventParticipant.CREATION_DATE)));

		List<EventParticipant> resultList = em.createQuery(cq).getResultList();
		return resultList;
	}

	public List<EventParticipant> getAllActiveByEvent(Event event) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(getElementClass());
		Root<EventParticipant> from = cq.from(getElementClass());

		Predicate filter = cb.equal(from.get(EventParticipant.EVENT), event);
		filter = cb.and(filter, cb.equal(from.get(Event.DELETED), false));

		cq.where(filter);
		cq.orderBy(cb.desc(from.get(EventParticipant.CREATION_DATE)));

		List<EventParticipant> resultList = em.createQuery(cq).getResultList();
		return resultList;
	}

	public Predicate buildCriteriaFilter(EventParticipantCriteria criteria, EventParticipantQueryContext eventParticipantQueryContext) {

		final CriteriaBuilder cb = eventParticipantQueryContext.getCriteriaBuilder();
		final From<?, EventParticipant> from = eventParticipantQueryContext.getRoot();
		final CriteriaQuery cq = eventParticipantQueryContext.getQuery();
		final EventParticipantJoins joins = eventParticipantQueryContext.getJoins();
		final Join<EventParticipant, Event> event = joins.getEvent();
		final Join<EventParticipant, Person> person = joins.getPerson();
		final PersonQueryContext personQueryContext = new PersonQueryContext(cb, cq, joins.getPersonJoins());

		Predicate filter = null;
		if (criteria.getEvent() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(event.get(Event.UUID), criteria.getEvent().getUuid()));
		}
		if (criteria.getPerson() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(person.get(Person.UUID), criteria.getPerson().getUuid()));
		}

		if (criteria.getFreeText() != null) {
			String[] textFilters = criteria.getFreeText().split("\\s+");
			for (String textFilter : textFilters) {
				if (DataHelper.isNullOrEmpty(textFilter)) {
					continue;
				}

				Predicate likeFilters = cb.or(
					CriteriaBuilderHelper.unaccentedIlike(cb, person.get(Person.FIRST_NAME), textFilter),
					CriteriaBuilderHelper.unaccentedIlike(cb, person.get(Person.LAST_NAME), textFilter),
					phoneNumberPredicate(cb, personQueryContext.getSubqueryExpression(ContactQueryContext.PERSON_PHONE_SUBQUERY), textFilter));
				filter = CriteriaBuilderHelper.and(cb, filter, likeFilters);
			}
		}

		if (criteria.getBirthdateYYYY() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(person.get(Person.BIRTHDATE_YYYY), criteria.getBirthdateYYYY()));
		}
		if (criteria.getBirthdateMM() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(person.get(Person.BIRTHDATE_MM), criteria.getBirthdateMM()));
		}
		if (criteria.getBirthdateDD() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(person.get(Person.BIRTHDATE_DD), criteria.getBirthdateDD()));
		}
		if (criteria.getPathogenTestResult() != null) {
			Join<EventParticipant, Sample> samples = from.join(EventParticipant.SAMPLES, JoinType.LEFT);
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(samples.get(Sample.PATHOGEN_TEST_RESULT), criteria.getPathogenTestResult()));
		}
		if (criteria.getVaccinationStatus() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(EventParticipant.VACCINATION_STATUS), criteria.getVaccinationStatus()));
		}
		if (Boolean.TRUE.equals(criteria.getNoResultingCase())) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.isNull(from.get(EventParticipant.RESULTING_CASE)));
		}

		if (criteria.getRelevanceStatus() != null) {
			if (criteria.getRelevanceStatus() == EntityRelevanceStatus.ACTIVE) {
				filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.and(
						cb.or(cb.equal(event.get(Event.ARCHIVED), false), cb.isNull(event.get(Event.ARCHIVED))),
						cb.or(cb.equal(from.get(EventParticipant.ARCHIVED), false), cb.isNull(from.get(EventParticipant.ARCHIVED)))));
			} else if (criteria.getRelevanceStatus() == EntityRelevanceStatus.ARCHIVED) {
				filter = CriteriaBuilderHelper
					.and(cb, filter, cb.or(cb.equal(event.get(Event.ARCHIVED), true), cb.equal(from.get(EventParticipant.ARCHIVED), true)));
			}
		}

		filter = CriteriaBuilderHelper.and(cb, filter, createDefaultFilter(cb, from));

		return filter;
	}

	public Predicate createActiveEventParticipantsFilter(EventParticipantQueryContext eventParticipantQueryContext) {

		final EventParticipantJoins joins = eventParticipantQueryContext.getJoins();
		final Join<EventParticipant, Event> event = joins.getEvent();
		final CriteriaBuilder cb = eventParticipantQueryContext.getCriteriaBuilder();
		return cb.and(cb.isFalse(event.get(Event.ARCHIVED)), cb.isFalse(event.get(Event.DELETED)));
	}

	public Predicate createActiveEventParticipantsInActiveEventsFilter(EventParticipantQueryContext eventParticipantQueryContext) {

		final EventParticipantJoins joins = eventParticipantQueryContext.getJoins();
		final From<?, EventParticipant> eventParticipant = eventParticipantQueryContext.getRoot();
		final Join<EventParticipant, Event> event = joins.getEvent();
		final CriteriaBuilder cb = eventParticipantQueryContext.getCriteriaBuilder();
		return cb.and(
			cb.isFalse(eventParticipant.get(EventParticipant.DELETED)),
			cb.isFalse(event.get(Event.ARCHIVED)),
			cb.isFalse(event.get(Event.DELETED)));
	}

	public Predicate createActiveEventParticipantsFilter(CriteriaBuilder cb, Join<?, EventParticipant> eventParticipantJoin) {

		return cb.and(cb.isFalse(eventParticipantJoin.get(EventParticipant.DELETED)));
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Predicate createUserFilterInternal(CriteriaBuilder cb, CriteriaQuery cq, From<?, EventParticipant> from) {
		return createUserFilter(new EventParticipantQueryContext(cb, cq, from));
	}

	public Predicate createUserFilter(EventParticipantQueryContext eventParticipantQueryContext) {

		final EventUserFilterCriteria eventUserFilterCriteria = new EventUserFilterCriteria();
		eventUserFilterCriteria.includeUserCaseAndEventParticipantFilter(true);
		eventUserFilterCriteria.forceRegionJurisdiction(true);

		return createUserFilter(eventParticipantQueryContext, eventUserFilterCriteria);
	}

	public Predicate createUserFilter(EventParticipantQueryContext epqc, EventUserFilterCriteria eventUserFilterCriteria) {

		final CriteriaBuilder cb = epqc.getCriteriaBuilder();
		final CriteriaQuery<?> cq = epqc.getQuery();
		final EventParticipantJoins joins = epqc.getJoins();

		return eventService.createUserFilter(new EventQueryContext(cb, cq, joins.getEventJoins()), eventUserFilterCriteria);
	}

	public EventParticipant getByEventAndPerson(String eventUuid, String personUuid) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(getElementClass());
		Root<EventParticipant> from = cq.from(getElementClass());

		cq.where(
			createDefaultFilter(cb, from),
			cb.equal(from.join(EventParticipant.PERSON).get(Person.UUID), personUuid),
			cb.equal(from.join(EventParticipant.EVENT).get(Event.UUID), eventUuid));

		return em.createQuery(cq).getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void delete(EventParticipant eventParticipant, DeletionDetails deletionDetails) {

		eventParticipant.getSamples()
			.stream()
			.filter(sample -> sample.getAssociatedCase() == null && sample.getAssociatedContact() == null)
			.forEach(sample -> sampleService.delete(sample, deletionDetails));

		super.delete(eventParticipant, deletionDetails);
	}

	public List<String> getAllUuidsByEventUuids(List<String> eventUuids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<EventParticipant> from = cq.from(getElementClass());

		cq.select(from.get(EventParticipant.UUID));
		cq.where(createDefaultFilter(cb, from), cb.in(from.join(EventParticipant.EVENT).get(Event.UUID)).value(eventUuids));

		return em.createQuery(cq).getResultList();
	}

	public List<String> getArchivedUuidsSince(Date since) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<EventParticipant> eventParticipant = cq.from(EventParticipant.class);
		EventParticipantQueryContext epqc = new EventParticipantQueryContext(cb, cq, eventParticipant);

		Predicate filter = createUserFilter(epqc);
		if (since != null) {
			Predicate dateFilter = cb.greaterThanOrEqualTo(eventParticipant.get(EventParticipant.CHANGE_DATE), since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}

		Join<EventParticipant, Event> eventJoin = epqc.getJoins().getEvent();
		Predicate archivedFilter =
			cb.or(cb.equal(eventParticipant.get(EventParticipant.ARCHIVED), true), cb.equal(eventJoin.get(Event.ARCHIVED), true));
		if (filter != null) {
			filter = cb.and(filter, archivedFilter);
		} else {
			filter = archivedFilter;
		}

		cq.where(filter);
		cq.select(eventParticipant.get(Contact.UUID));

		List<String> resultList = em.createQuery(cq).getResultList();
		return resultList;
	}

	public List<String> getDeletedUuidsSince(Date since, User user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<EventParticipant> eventParticipantRoot = cq.from(EventParticipant.class);
		EventUserFilterCriteria eventUserFilterCriteria = new EventUserFilterCriteria();
		eventUserFilterCriteria.includeUserCaseAndEventParticipantFilter(true);
		eventUserFilterCriteria.forceRegionJurisdiction(true);

		Predicate filter = eventService.createUserFilter(
			new EventQueryContext(cb, cq, new EventJoins(eventParticipantRoot, eventParticipantRoot.join(EventParticipant.EVENT, JoinType.LEFT))),
			eventUserFilterCriteria);

		if (since != null) {
			Predicate dateFilter = createChangeDateFilter(cb, eventParticipantRoot, since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}

		Predicate deletedFilter = cb.equal(eventParticipantRoot.get(EventParticipant.DELETED), true);
		if (filter != null) {
			filter = cb.and(filter, deletedFilter);
		} else {
			filter = deletedFilter;
		}

		cq.where(filter);
		cq.select(eventParticipantRoot.get(EventParticipant.UUID));

		return em.createQuery(cq).getResultList();
	}

	public Predicate createDefaultFilter(CriteriaBuilder cb, From<?, EventParticipant> root) {
		return cb.isFalse(root.get(EventParticipant.DELETED));
	}

	public Predicate createDefaultInUndeletedEventsFilter(EventParticipantQueryContext eventParticipantQueryContext) {

		final EventParticipantJoins joins = eventParticipantQueryContext.getJoins();
		final From<?, EventParticipant> eventParticipant = eventParticipantQueryContext.getRoot();
		final Join<EventParticipant, Event> event = joins.getEvent();
		final CriteriaBuilder cb = eventParticipantQueryContext.getCriteriaBuilder();
		return CriteriaBuilderHelper.and(cb, createDefaultFilter(cb, eventParticipant), cb.isFalse(event.get(Event.DELETED)));
	}

	public List<EventParticipant> findBy(EventParticipantCriteria criteria, User user) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(getElementClass());
		Root<EventParticipant> from = cq.from(getElementClass());
		final EventParticipantQueryContext queryContext = new EventParticipantQueryContext(cb, cq, from);

		Predicate filter = buildCriteriaFilter(criteria, queryContext);

		if (user != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, createUserFilter(queryContext));
		}
		if (filter != null) {
			cq.where(filter);
		}
		cq.orderBy(cb.asc(from.get(EventParticipant.CREATION_DATE)));

		return em.createQuery(cq).getResultList();
	}

	public Optional<EventParticipant> getFirst(EventParticipantCriteria criteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(EventParticipant.class);
		Root<EventParticipant> eventParticipant = cq.from(EventParticipant.class);
		EventParticipantQueryContext eventParticipantQueryContext = new EventParticipantQueryContext(cb, cq, eventParticipant);

		Predicate filter = buildCriteriaFilter(criteria, eventParticipantQueryContext);
		cq.where(filter);
		cq.orderBy(cb.asc(eventParticipant.get(EventParticipant.UUID)));

		return QueryHelper.getFirstResult(em, cq, Optional::ofNullable);
	}

	public List<EventParticipant> getByEventUuids(List<String> eventUuids) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(EventParticipant.class);
		Root<EventParticipant> epRoot = cq.from(EventParticipant.class);
		Join<EventParticipant, Event> eventJoin = epRoot.join(EventParticipant.EVENT, JoinType.LEFT);

		Predicate filter = cb.and(createDefaultFilter(cb, epRoot), eventJoin.get(AbstractDomainObject.UUID).in(eventUuids));

		cq.where(filter);
		return em.createQuery(cq).getResultList();
	}

	@Override
	public Predicate createChangeDateFilter(CriteriaBuilder cb, From<?, EventParticipant> from, Timestamp date) {
		ChangeDateFilterBuilder changeDateFilterBuilder = new ChangeDateFilterBuilder(cb, date, from, null);

		return addChangeDates(changeDateFilterBuilder, from, false).build();

	}

	@Override
	protected <T extends ChangeDateBuilder<T>> T addChangeDates(
		T builder,
		From<?, EventParticipant> eventParticipantFrom,
		boolean includeExtendedChangeDateFilters) {

		Join<EventParticipant, Sample> eventParticipantSampleJoin = eventParticipantFrom.join(EventParticipant.SAMPLES, JoinType.LEFT);

		builder = super.addChangeDates(builder, eventParticipantFrom, includeExtendedChangeDateFilters)
			.add(eventParticipantFrom, EventParticipant.SORMAS_TO_SORMAS_ORIGIN_INFO)
			.add(eventParticipantFrom, EventParticipant.SORMAS_TO_SORMAS_SHARES)
			.add(eventParticipantSampleJoin);

		return builder;
	}

	public EditPermissionType isEventParticipantEditAllowed(EventParticipant eventParticipant) {

		if (eventParticipant.getSormasToSormasOriginInfo() != null && !eventParticipant.getSormasToSormasOriginInfo().isOwnershipHandedOver()) {
			return EditPermissionType.REFUSED;
		}

		if (!inJurisdiction(eventParticipant) || sormasToSormasShareInfoService.isEventParticipantOwnershipHandedOver(eventParticipant)) {
			return EditPermissionType.REFUSED;
		}

		return super.getEditPermissionType(eventParticipant);
	}

	public Collection<EventParticipant> getByPersonUuids(List<String> personUuids) {

		List<EventParticipant> eventParticipants = new LinkedList<>();
		IterableHelper.executeBatched(
			personUuids,
			ModelConstants.PARAMETER_LIMIT,
			batchedPersonUuids -> eventParticipants.addAll(getEventParticipantsByPersonUuids(batchedPersonUuids)));
		return eventParticipants;
	}

	private List<EventParticipant> getEventParticipantsByPersonUuids(List<String> personUuids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EventParticipant> cq = cb.createQuery(EventParticipant.class);
		Root<EventParticipant> eventParticipantRoot = cq.from(EventParticipant.class);
		Join<EventParticipant, Person> personJoin = eventParticipantRoot.join(Contact.PERSON, JoinType.LEFT);

		cq.where(personJoin.get(AbstractDomainObject.UUID).in(personUuids));

		return em.createQuery(cq).getResultList();
	}

	public boolean inJurisdictionOrOwned(EventParticipant eventParticipant) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Boolean> cq = cb.createQuery(Boolean.class);
		Root<EventParticipant> root = cq.from(EventParticipant.class);
		cq.multiselect(JurisdictionHelper.booleanSelector(cb, inJurisdictionOrOwned(new EventParticipantQueryContext(cb, cq, root))));
		cq.where(cb.equal(root.get(EventParticipant.UUID), eventParticipant.getUuid()));
		return em.createQuery(cq).getSingleResult();
	}

	public boolean inJurisdiction(EventParticipant eventParticipant) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Boolean> cq = cb.createQuery(Boolean.class);
		Root<EventParticipant> root = cq.from(EventParticipant.class);
		cq.multiselect(JurisdictionHelper.booleanSelector(cb, inJurisdiction(new EventParticipantQueryContext(cb, cq, root))));
		cq.where(cb.equal(root.get(EventParticipant.UUID), eventParticipant.getUuid()));
		return em.createQuery(cq).getSingleResult();
	}

	public Predicate inJurisdiction(EventParticipantQueryContext qc) {
		final User currentUser = this.getCurrentUser();
		return EventParticipantJurisdictionPredicateValidator.of(qc, currentUser).inJurisdiction();
	}

	public Predicate inJurisdictionOrOwned(EventParticipantQueryContext qc) {
		final User currentUser = this.getCurrentUser();
		return EventParticipantJurisdictionPredicateValidator.of(qc, currentUser).inJurisdictionOrOwned();
	}

	/**
	 * Sets the vaccination status of all event participants of the specified person and disease with vaccination date <= event start date.
	 * Vaccinations without a vaccination date are relevant for all event participants.
	 *
	 * @param personId
	 *            The ID of the event participant person
	 * @param disease
	 *            The disease of the events
	 * @param vaccinationDate
	 *            The vaccination date of the created or updated vaccination
	 */
	public void updateVaccinationStatuses(Long personId, Disease disease, Date vaccinationDate) {

		// Only consider event participants with relevance date at least one day after the vaccination date
		if (vaccinationDate == null) {
			return;
		} else {
			vaccinationDate = DateHelper.getEndOfDay(vaccinationDate);
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<EventParticipant> cu = cb.createCriteriaUpdate(EventParticipant.class);
		Root<EventParticipant> root = cu.from(EventParticipant.class);

		Subquery<Event> eventSq = cu.subquery(Event.class);
		Root<Event> eventSqRoot = eventSq.from(Event.class);
		eventSq.select(eventSqRoot);

		Predicate datePredicate = vaccinationDate != null
			? cb.or(
				cb.greaterThan(eventSqRoot.get(Event.START_DATE), vaccinationDate),
				cb.and(cb.isNull(eventSqRoot.get(Event.START_DATE)), cb.greaterThanOrEqualTo(eventSqRoot.get(Event.END_DATE), vaccinationDate)),
				cb.and(
					cb.isNull(eventSqRoot.get(Event.START_DATE)),
					cb.isNull(eventSqRoot.get(Event.END_DATE)),
					cb.greaterThan(eventSqRoot.get(Event.REPORT_DATE_TIME), vaccinationDate)))
			: null;

		eventSq.where(
			CriteriaBuilderHelper
				.and(cb, cb.equal(eventSqRoot, root.get(EventParticipant.EVENT)), cb.equal(eventSqRoot.get(Event.DISEASE), disease), datePredicate));

		cu.set(root.get(EventParticipant.VACCINATION_STATUS), VaccinationStatus.VACCINATED);
		cu.set(root.get(AbstractDomainObject.CHANGE_DATE), new Date());

		cu.where(cb.and(cb.equal(root.get(EventParticipant.PERSON), personId), cb.isNotNull(eventSq.getSelection())));

		em.createQuery(cu).executeUpdate();
	}
}
