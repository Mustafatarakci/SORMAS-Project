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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.backend.person;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.person.PersonNameDto;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.common.AbstractAdoService;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.contact.ContactService;
import de.symeda.sormas.backend.event.EventParticipant;
import de.symeda.sormas.backend.event.EventParticipantService;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.region.District;
import de.symeda.sormas.backend.user.User;

@Stateless
@LocalBean
public class PersonService extends AbstractAdoService<Person> {

	@EJB
	CaseService caseService;
	@EJB
	ContactService contactService;
	@EJB
	EventParticipantService eventParticipantService;

	public PersonService() {
		super(Person.class);
	}

	public Person createPerson() {
		Person person = new Person();
		return person;
	}

	@Override
	public List<String> getAllUuids(User user) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// persons by LGA
		CriteriaQuery<String> lgaQuery = cb.createQuery(String.class);
		Root<Person> lgaRoot = lgaQuery.from(Person.class);
		Join<Person, Location> address = lgaRoot.join(Person.ADDRESS);
		lgaQuery.select(lgaRoot.get(Person.UUID));
		Predicate lgaFilter = cb.equal(address.get(Location.DISTRICT), user.getDistrict());
		lgaQuery.where(lgaFilter);
		List<String> lgaResultList = em.createQuery(lgaQuery).getResultList();

		// persons by case
		CriteriaQuery<String> casePersonsQuery = cb.createQuery(String.class);
		Root<Case> casePersonsRoot = casePersonsQuery.from(Case.class);
		Join<Person, Person> casePersonsSelect = casePersonsRoot.join(Case.PERSON);
		casePersonsQuery.select(casePersonsSelect.get(Person.UUID));
		Predicate casePersonsFilter = caseService.createUserFilter(cb, casePersonsQuery, casePersonsRoot, user);
		if (casePersonsFilter != null) {
			casePersonsQuery.where(casePersonsFilter);
		}
		casePersonsQuery.distinct(true);
		List<String> casePersonsResultList = em.createQuery(casePersonsQuery).getResultList();

		// persons by contact
		CriteriaQuery<String> contactPersonsQuery = cb.createQuery(String.class);
		Root<Contact> contactPersonsRoot = contactPersonsQuery.from(Contact.class);
		Join<Person, Person> contactPersonsSelect = contactPersonsRoot.join(Contact.PERSON);
		contactPersonsQuery.select(contactPersonsSelect.get(Person.UUID));
		Predicate contactPersonsFilter = contactService.createUserFilter(cb, contactPersonsQuery, contactPersonsRoot,
				user);
		contactPersonsQuery.where(contactPersonsFilter);
		contactPersonsQuery.distinct(true);
		List<String> contactPersonsResultList = em.createQuery(contactPersonsQuery).getResultList();

		// persons by event participant
		CriteriaQuery<String> eventPersonsQuery = cb.createQuery(String.class);
		Root<EventParticipant> eventPersonsRoot = eventPersonsQuery.from(EventParticipant.class);
		Join<Person, Person> eventPersonsSelect = eventPersonsRoot.join(EventParticipant.PERSON);
		eventPersonsQuery.select(eventPersonsSelect.get(Person.UUID));
		Predicate eventPersonsFilter = eventParticipantService.createUserFilter(cb, eventPersonsQuery, eventPersonsRoot,
				user);
		eventPersonsQuery.where(eventPersonsFilter);
		eventPersonsQuery.distinct(true);
		List<String> eventPersonsResultList = em.createQuery(eventPersonsQuery).getResultList();

		return Stream.of(lgaResultList, casePersonsResultList, contactPersonsResultList, eventPersonsResultList)
				.flatMap(List<String>::stream).distinct().collect(Collectors.toList());
	}

	@Override
	public List<Person> getAllAfter(Date date, User user) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// persons by LGA
		CriteriaQuery<Person> personsQuery = cb.createQuery(Person.class);
		Root<Person> personsRoot = personsQuery.from(Person.class);
		Join<Person, Location> address = personsRoot.join(Person.ADDRESS);
		Predicate lgaFilter = cb.equal(address.get(Location.DISTRICT), user.getDistrict());
		// date range
		if (date != null) {
			Predicate dateFilter = createDateFilter(cb, personsQuery, personsRoot, date);
			lgaFilter = cb.and(lgaFilter, dateFilter);
		}
		personsQuery.where(lgaFilter);
		List<Person> lgaResultList = em.createQuery(personsQuery).getResultList();

		// persons by case
		CriteriaQuery<Person> casePersonsQuery = cb.createQuery(Person.class);
		Root<Case> casePersonsRoot = casePersonsQuery.from(Case.class);
		Join<Person, Person> casePersonsSelect = casePersonsRoot.join(Case.PERSON);
		casePersonsQuery.select(casePersonsSelect);
		Predicate casePersonsFilter = caseService.createUserFilter(cb, casePersonsQuery, casePersonsRoot, user);
		// date range
		if (date != null) {
			Predicate dateFilter = createDateFilter(cb, casePersonsQuery, casePersonsSelect, date);
			if (casePersonsFilter != null) {
				casePersonsFilter = cb.and(casePersonsFilter, dateFilter);
			} else {
				casePersonsFilter = dateFilter;
			}
		}
		if (casePersonsFilter != null) {
			casePersonsQuery.where(casePersonsFilter);
		}
		casePersonsQuery.distinct(true);
		List<Person> casePersonsResultList = em.createQuery(casePersonsQuery).getResultList();

		// persons by contact
		CriteriaQuery<Person> contactPersonsQuery = cb.createQuery(Person.class);
		Root<Contact> contactPersonsRoot = contactPersonsQuery.from(Contact.class);
		Join<Person, Person> contactPersonsSelect = contactPersonsRoot.join(Contact.PERSON);
		contactPersonsQuery.select(contactPersonsSelect);
		Predicate contactPersonsFilter = contactService.createUserFilter(cb, contactPersonsQuery, contactPersonsRoot,
				user);
		// date range
		if (date != null) {
			Predicate dateFilter = createDateFilter(cb, contactPersonsQuery, contactPersonsSelect, date);
			contactPersonsFilter = cb.and(contactPersonsFilter, dateFilter);
		}
		if (contactPersonsFilter != null) {
			contactPersonsQuery.where(contactPersonsFilter);
		}
		contactPersonsQuery.distinct(true);
		List<Person> contactPersonsResultList = em.createQuery(contactPersonsQuery).getResultList();

		// persons by event participant
		CriteriaQuery<Person> eventPersonsQuery = cb.createQuery(Person.class);
		Root<EventParticipant> eventPersonsRoot = eventPersonsQuery.from(EventParticipant.class);
		Join<Person, Person> eventPersonsSelect = eventPersonsRoot.join(EventParticipant.PERSON);
		eventPersonsQuery.select(eventPersonsSelect);
		Predicate eventPersonsFilter = eventParticipantService.createUserFilter(cb, eventPersonsQuery, eventPersonsRoot,
				user);
		// date range
		if (date != null) {
			Predicate dateFilter = createDateFilter(cb, eventPersonsQuery, eventPersonsSelect, date);
			eventPersonsFilter = cb.and(eventPersonsFilter, dateFilter);
		}
		if (eventPersonsFilter != null) {
			eventPersonsQuery.where(eventPersonsFilter);
		}
		eventPersonsQuery.distinct(true);
		List<Person> eventPersonsResultList = em.createQuery(eventPersonsQuery).getResultList();

		return Stream.of(lgaResultList, casePersonsResultList, contactPersonsResultList, eventPersonsResultList)
				.flatMap(List<Person>::stream).distinct().sorted(Comparator.comparing(Person::getChangeDate))
				.collect(Collectors.toList());
	}

	public Set<PersonNameDto> getNameDtos(User user) {
		Set<PersonNameDto> persons = new HashSet<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// persons by LGA
		CriteriaQuery<PersonNameDto> lgaQuery = cb.createQuery(PersonNameDto.class);
		Root<Person> lgaRoot = lgaQuery.from(Person.class);
		Join<Person, Location> address = lgaRoot.join(Person.ADDRESS);

		lgaQuery.multiselect(lgaRoot.get(Person.FIRST_NAME), lgaRoot.get(Person.LAST_NAME), lgaRoot.get(Person.UUID));

		Predicate lgaFilter = cb.equal(address.get(Location.DISTRICT), user.getDistrict());
		lgaQuery.where(lgaFilter);
		persons.addAll(em.createQuery(lgaQuery).getResultList());

		// persons by case
		CriteriaQuery<PersonNameDto> casePersonsQuery = cb.createQuery(PersonNameDto.class);
		Root<Case> casePersonsRoot = casePersonsQuery.from(Case.class);
		Join<Case, Person> casePersonsJoin = casePersonsRoot.join(Case.PERSON, JoinType.LEFT);

		casePersonsQuery.multiselect(casePersonsJoin.get(Person.FIRST_NAME), casePersonsJoin.get(Person.LAST_NAME),
				casePersonsJoin.get(Person.UUID));

		Predicate casePersonsFilter = caseService.createUserFilter(cb, casePersonsQuery, casePersonsRoot, user);
		if (casePersonsFilter != null) {
			casePersonsQuery.where(casePersonsFilter);
		}
		casePersonsQuery.distinct(true);
		persons.addAll(em.createQuery(casePersonsQuery).getResultList());

		// persons by contact
		CriteriaQuery<PersonNameDto> contactPersonsQuery = cb.createQuery(PersonNameDto.class);
		Root<Contact> contactPersonsRoot = contactPersonsQuery.from(Contact.class);
		Join<Contact, Person> contactPersonsJoin = contactPersonsRoot.join(Contact.PERSON, JoinType.LEFT);

		contactPersonsQuery.multiselect(contactPersonsJoin.get(Person.FIRST_NAME),
				contactPersonsJoin.get(Person.LAST_NAME), contactPersonsJoin.get(Person.UUID));

		Predicate contactPersonsFilter = contactService.createUserFilter(cb, contactPersonsQuery, contactPersonsRoot,
				user);
		if (contactPersonsFilter != null) {
			contactPersonsQuery.where(contactPersonsFilter);
		}
		contactPersonsQuery.distinct(true);
		persons.addAll(em.createQuery(contactPersonsQuery).getResultList());

		// persons by event participant
		CriteriaQuery<PersonNameDto> eventPersonsQuery = cb.createQuery(PersonNameDto.class);
		Root<EventParticipant> eventPersonsRoot = eventPersonsQuery.from(EventParticipant.class);
		Join<EventParticipant, Person> eventPersonsJoin = eventPersonsRoot.join(EventParticipant.PERSON, JoinType.LEFT);

		eventPersonsQuery.multiselect(eventPersonsJoin.get(Person.FIRST_NAME), eventPersonsJoin.get(Person.LAST_NAME),
				eventPersonsJoin.get(Person.UUID));

		Predicate eventPersonsFilter = eventParticipantService.createUserFilter(cb, eventPersonsQuery, eventPersonsRoot,
				user);
		if (eventPersonsFilter != null) {
			eventPersonsQuery.where(eventPersonsFilter);
		}
		eventPersonsQuery.distinct(true);
		persons.addAll(em.createQuery(eventPersonsQuery).getResultList());

		return persons;
	}

	public List<Person> getDeathsBetween(Date fromDate, Date toDate, District district, Disease disease, User user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Person> casePersonsQuery = cb.createQuery(Person.class);
		Root<Case> casePersonsRoot = casePersonsQuery.from(Case.class);
		Path<Person> casePersonsSelect = casePersonsRoot.get(Case.PERSON);
		casePersonsQuery.select(casePersonsSelect);
		Predicate casePersonsFilter = caseService.createUserFilter(cb, casePersonsQuery, casePersonsRoot, user);

		// only probable and confirmed cases are of interest
		Predicate classificationFilter = cb.equal(casePersonsRoot.get(Case.CASE_CLASSIFICATION),
				CaseClassification.CONFIRMED);
		classificationFilter = cb.or(classificationFilter,
				cb.equal(casePersonsRoot.get(Case.CASE_CLASSIFICATION), CaseClassification.PROBABLE));

		if (casePersonsFilter != null) {
			casePersonsFilter = cb.and(casePersonsFilter, classificationFilter);
		} else {
			casePersonsFilter = classificationFilter;
		}

		// death date range
		Predicate dateFilter = cb.isNotNull(casePersonsSelect.get(Person.DEATH_DATE));
		dateFilter = cb.and(dateFilter, cb.greaterThanOrEqualTo(casePersonsSelect.get(Person.DEATH_DATE), fromDate));
		dateFilter = cb.and(dateFilter, cb.lessThanOrEqualTo(casePersonsSelect.get(Person.DEATH_DATE), toDate));

		if (casePersonsFilter != null) {
			casePersonsFilter = cb.and(casePersonsFilter, dateFilter);
		} else {
			casePersonsFilter = dateFilter;
		}

		if (casePersonsFilter != null && district != null) {
			casePersonsFilter = cb.and(casePersonsFilter, cb.equal(casePersonsRoot.get(Case.DISTRICT), district));
		}

		if (casePersonsFilter != null && disease != null) {
			casePersonsFilter = cb.and(casePersonsFilter, cb.equal(casePersonsRoot.get(Case.DISEASE), disease));
		}

		if (casePersonsFilter != null) {
			casePersonsQuery.where(casePersonsFilter);
		}
		casePersonsQuery.distinct(true);
		List<Person> casePersonsResultList = em.createQuery(casePersonsQuery).getResultList();
		return casePersonsResultList;
	}

	public Location getAddressByPersonId(long personId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Location> cq = cb.createQuery(Location.class);
		Root<Person> root = cq.from(getElementClass());
		cq.where(cb.equal(root.get(Person.ID), personId));
		cq.select(root.get(Person.ADDRESS));
		Location result = em.createQuery(cq).getSingleResult();
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<Person, Person> from, User user) {
		// getAllUuids and getAllAfter have custom implementations
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate createDateFilter(CriteriaBuilder cb, CriteriaQuery cq, From<Person, Person> from, Date date) {
		Predicate dateFilter = cb.greaterThan(from.get(AbstractDomainObject.CHANGE_DATE), date);
		Join<Person, Location> address = from.join(Person.ADDRESS);
		dateFilter = cb.or(dateFilter, cb.greaterThan(address.get(AbstractDomainObject.CHANGE_DATE), date));
		return dateFilter;
	}
}
