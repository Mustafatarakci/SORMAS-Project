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

import de.symeda.sormas.backend.access.BaseAccessEntities;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;

public class CaseAccessEntities extends BaseAccessEntities {

	private Region responsibleRegion;
	private District responsibleDistrict;
	private Community responsibleCommunity;

	public Region getResponsibleRegion() {
		return responsibleRegion;
	}

	public CaseAccessEntities responsibleRegionId(Region responsibleRegion) {
		this.responsibleRegion = responsibleRegion;
		return this;
	}

	public District getResponsibleDistrict() {
		return responsibleDistrict;
	}

	public CaseAccessEntities responsibleDistrictId(District responsibleDistrict) {
		this.responsibleDistrict = responsibleDistrict;
		return this;
	}

	public Community getResponsibleCommunity() {
		return responsibleCommunity;
	}

	public CaseAccessEntities responsibleCommunityId(Community responsibleCommunity) {
		this.responsibleCommunity = responsibleCommunity;
		return this;
	}

}
