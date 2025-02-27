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

package de.symeda.sormas.backend.sormastosormas.share.sharerequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.sormastosormas.SormasServerDescriptor;
import de.symeda.sormas.api.sormastosormas.sharerequest.ShareRequestCriteria;
import de.symeda.sormas.api.sormastosormas.sharerequest.SormasToSormasShareRequestDto;
import de.symeda.sormas.api.sormastosormas.sharerequest.SormasToSormasShareRequestFacade;
import de.symeda.sormas.api.sormastosormas.sharerequest.SormasToSormasShareRequestIndexDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.sormastosormas.access.SormasToSormasDiscoveryService;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfo;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoFacadeEjb;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoFacadeEjb.SormasToSormasOriginInfoFacadeEjbLocal;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.QueryHelper;

@Stateless(name = "SormasToSormasShareRequestFacade")
public class SormasToSormasShareRequestFacadeEJB implements SormasToSormasShareRequestFacade {

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private SormasToSormasShareRequestService shareRequestService;

	@EJB
	private SormasToSormasOriginInfoFacadeEjbLocal originInfoFacade;

	@EJB
	private SormasToSormasDiscoveryService sormasToSormasDiscoveryService;

	@Override
	public SormasToSormasShareRequestDto saveShareRequest(@Valid SormasToSormasShareRequestDto dto) {
		SormasToSormasShareRequest request = fromDto(dto, true);

		shareRequestService.ensurePersisted(request);

		return toDto(request);
	}

	@Override
	public SormasToSormasShareRequestDto getShareRequestByUuid(String uuid) {
		SormasToSormasShareRequest request = shareRequestService.getByUuid(uuid);

		return toDto(request);
	}

	@Override
	public List<SormasToSormasShareRequestIndexDto> getIndexList(
		ShareRequestCriteria criteria,
		Integer first,
		Integer max,
		List<SortProperty> sortProperties) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SormasToSormasShareRequestIndexDto> cq = cb.createQuery(SormasToSormasShareRequestIndexDto.class);
		Root<SormasToSormasShareRequest> requestRoot = cq.from(SormasToSormasShareRequest.class);
		Path<SormasToSormasOriginInfo> originInfo = requestRoot.get(SormasToSormasShareRequest.ORIGIN_INFO);

		cq.multiselect(
			requestRoot.get(SormasToSormasShareRequest.UUID),
			requestRoot.get(SormasToSormasShareRequest.CREATION_DATE),
			requestRoot.get(SormasToSormasShareRequest.DATA_TYPE),
			requestRoot.get(SormasToSormasShareRequest.STATUS),
			originInfo.get(SormasToSormasOriginInfo.ORGANIZATION_ID),
			originInfo.get(SormasToSormasOriginInfo.SENDER_NAME),
			originInfo.get(SormasToSormasOriginInfo.OWNERSHIP_HANDED_OVER),
			originInfo.get(SormasToSormasOriginInfo.COMMENT));

		Predicate filter = null;
		if (criteria != null) {
			filter = shareRequestService.buildCriteriaFilter(criteria, cb, requestRoot);
		}

		if (filter != null) {
			cq.where(filter);
		}

		List<Order> order = new ArrayList<>();
		if (sortProperties != null && sortProperties.size() > 0) {
			for (SortProperty sortProperty : sortProperties) {
				Expression<?> expression;
				switch (sortProperty.propertyName) {
				case SormasToSormasShareRequest.UUID:
				case SormasToSormasShareRequest.CREATION_DATE:
				case SormasToSormasShareRequest.DATA_TYPE:
				case SormasToSormasShareRequest.STATUS:
					expression = requestRoot.get(sortProperty.propertyName);
					break;
				case SormasToSormasOriginInfo.ORGANIZATION_ID:
				case SormasToSormasOriginInfo.SENDER_NAME:
				case SormasToSormasOriginInfo.COMMENT:
				case SormasToSormasOriginInfo.OWNERSHIP_HANDED_OVER:
					expression = originInfo.get(sortProperty.propertyName);
					break;
				default:
					throw new IllegalArgumentException(sortProperty.propertyName);
				}
				order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
			}
		}

		cq.orderBy(order);

		List<SormasToSormasShareRequestIndexDto> requests = QueryHelper.getResultList(em, cq, first, max);

		if (!requests.isEmpty()) {
			Map<String, SormasServerDescriptor> serverDescriptorMap = sormasToSormasDiscoveryService.getAllAvailableServers()
				.stream()
				.collect(Collectors.toMap(SormasServerDescriptor::getId, Function.identity()));

			requests.forEach(request -> {
				String organizationId = request.getOrganizationId();
				SormasServerDescriptor serverDescriptor = serverDescriptorMap.get(organizationId);

				request.setOrganizationName(serverDescriptor != null ? serverDescriptor.getName() : organizationId);
			});
		}

		return requests;
	}

	@Override
	public long count(ShareRequestCriteria criteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<SormasToSormasShareRequest> requestRoot = cq.from(SormasToSormasShareRequest.class);

		Predicate filter = null;
		if (criteria != null) {
			filter = shareRequestService.buildCriteriaFilter(criteria, cb, requestRoot);
		}

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(cb.count(requestRoot));

		return em.createQuery(cq).getSingleResult();
	}

	public Page<SormasToSormasShareRequestIndexDto> getIndexPage(
		ShareRequestCriteria criteria,
		Integer first,
		Integer max,
		List<SortProperty> sortProperties) {
		List<SormasToSormasShareRequestIndexDto> shareRequestIndexList = getIndexList(criteria, first, max, sortProperties);
		long totalElementCount = count(criteria);
		return new Page<>(shareRequestIndexList, first, max, totalElementCount);
	}

	public SormasToSormasShareRequest fromDto(@NotNull SormasToSormasShareRequestDto source, boolean checkChangeDate) {

		SormasToSormasShareRequest target =
			DtoHelper.fillOrBuildEntity(source, shareRequestService.getByUuid(source.getUuid()), SormasToSormasShareRequest::new, checkChangeDate);

		target.setDataType(source.getDataType());
		target.setStatus(source.getStatus());
		target.setOriginInfo(originInfoFacade.fromDto(source.getOriginInfo(), checkChangeDate));
		target.setCasesList(source.getCases());
		target.setContactsList(source.getContacts());
		target.setEventsList(source.getEvents());
		target.setEventParticipantsList(source.getEventParticipants());
		target.setResponseComment(source.getResponseComment());

		return target;
	}

	public static SormasToSormasShareRequestDto toDto(SormasToSormasShareRequest source) {
		if (source == null) {
			return null;
		}
		SormasToSormasShareRequestDto target = new SormasToSormasShareRequestDto();
		DtoHelper.fillDto(target, source);

		target.setDataType(source.getDataType());
		target.setStatus(source.getStatus());
		target.setOriginInfo(SormasToSormasOriginInfoFacadeEjb.toDto(source.getOriginInfo()));
		target.setCases(source.getCasesList());
		target.setContacts(source.getContactsList());
		target.setEvents(source.getEventsList());
		target.setEventParticipants(source.getEventParticipantsList());
		target.setResponseComment(source.getResponseComment());

		return target;
	}

	public List<SormasToSormasShareRequestDto> getShareRequestsByUuids(List<String> uuids) {
		return shareRequestService.getByUuids(uuids).stream().map(SormasToSormasShareRequestFacadeEJB::toDto).collect(Collectors.toList());
	}

	@LocalBean
	@Stateless
	public static class SormasToSormasShareRequestFacadeEJBLocal extends SormasToSormasShareRequestFacadeEJB {

	}
}
