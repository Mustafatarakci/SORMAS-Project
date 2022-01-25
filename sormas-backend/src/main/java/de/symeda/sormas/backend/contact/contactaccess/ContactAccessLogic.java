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

package de.symeda.sormas.backend.contact.contactaccess;

import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.backend.access.AbstractAccessLogic;
import de.symeda.sormas.backend.access.AbstractEntityAccess;
import de.symeda.sormas.backend.access.AccessChangeOperation;
import de.symeda.sormas.backend.caze.caseaccess.CaseAccess;
import de.symeda.sormas.backend.contact.Contact;

public class ContactAccessLogic extends AbstractAccessLogic {

	public static List<AccessChangeOperation> buildAccessChangeOperations(Contact existingContact, ContactDto updatedContact) {

		List<AccessChangeOperation> changeOperations = new ArrayList<>();

		insertChangeOperation(
			changeOperations,
			existingContact.getReportingUser(),
			updatedContact.getReportingUser(),
			AbstractEntityAccess.REPORTING_USER,
			null);
		insertChangeOperation(
			changeOperations,
			existingContact.getContactOfficer(),
			updatedContact.getContactOfficer(),
			AbstractEntityAccess.ASSIGNED_USER,
			ContactDto.CONTACT_OFFICER);
		insertChangeOperation(changeOperations, existingContact.getRegion(), updatedContact.getRegion(), AbstractEntityAccess.REGION, null);
		insertChangeOperation(changeOperations, existingContact.getDistrict(), updatedContact.getDistrict(), AbstractEntityAccess.DISTRICT, null);
		insertChangeOperation(changeOperations, existingContact.getCommunity(), updatedContact.getCommunity(), AbstractEntityAccess.COMMUNITY, null);
		insertChangeOperation(changeOperations, existingContact.getCaze(), updatedContact.getCaze(), CaseAccess.CAZE, null);

		return changeOperations;
	}

}
