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

import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.backend.access.AbstractAccessLogic;
import de.symeda.sormas.backend.access.AbstractEntityAccess;
import de.symeda.sormas.backend.access.AccessChangeOperation;
import de.symeda.sormas.backend.caze.Case;

public abstract class CaseAccessLogic extends AbstractAccessLogic {

	public static List<AccessChangeOperation> buildAccessChangeOperations(Case existingCase, CaseDataDto updatedCase) {

		List<AccessChangeOperation> changeOperations = new ArrayList<>();

		insertChangeOperation(
			changeOperations,
			existingCase.getReportingUser(),
			updatedCase.getReportingUser(),
			AbstractEntityAccess.REPORTING_USER,
			null);
		insertChangeOperation(
			changeOperations,
			existingCase.getSurveillanceOfficer(),
			updatedCase.getSurveillanceOfficer(),
			AbstractEntityAccess.ASSIGNED_USER,
			CaseDataDto.SURVEILLANCE_OFFICER);
		insertChangeOperation(
			changeOperations,
			existingCase.getResponsibleRegion(),
			updatedCase.getResponsibleRegion(),
			AbstractEntityAccess.REGION,
			CaseDataDto.RESPONSIBLE_REGION);
		insertChangeOperation(
			changeOperations,
			existingCase.getResponsibleDistrict(),
			updatedCase.getResponsibleDistrict(),
			AbstractEntityAccess.DISTRICT,
			CaseDataDto.RESPONSIBLE_DISTRICT);
		insertChangeOperation(
			changeOperations,
			existingCase.getResponsibleCommunity(),
			updatedCase.getResponsibleCommunity(),
			AbstractEntityAccess.COMMUNITY,
			CaseDataDto.RESPONSIBLE_COMMUNITY);
		insertChangeOperation(
			changeOperations,
			existingCase.getHealthFacility(),
			updatedCase.getHealthFacility(),
			AbstractEntityAccess.FACILITY,
			CaseDataDto.HEALTH_FACILITY);
		insertChangeOperation(
			changeOperations,
			existingCase.getPointOfEntry(),
			updatedCase.getPointOfEntry(),
			AbstractEntityAccess.POINT_OF_ENTRY,
			null);
		insertChangeOperation(
			changeOperations,
			existingCase.getRegion(),
			updatedCase.getRegion(),
			AbstractEntityAccess.REGION,
			null,
			false,
			existingCase.getDistrict() == null && existingCase.getCommunity() == null);
		insertChangeOperation(
			changeOperations,
			existingCase.getDistrict(),
			updatedCase.getDistrict(),
			AbstractEntityAccess.DISTRICT,
			null,
			false,
			existingCase.getRegion() == null && existingCase.getCommunity() == null);
		insertChangeOperation(
			changeOperations,
			existingCase.getCommunity(),
			updatedCase.getCommunity(),
			AbstractEntityAccess.COMMUNITY,
			null,
			false,
			existingCase.getRegion() == null && existingCase.getDistrict() == null);

		return changeOperations;
	}

}
