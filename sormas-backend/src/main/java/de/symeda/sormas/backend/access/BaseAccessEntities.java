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

package de.symeda.sormas.backend.access;

import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.pointofentry.PointOfEntry;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.user.User;

public class BaseAccessEntities {

	private User reportingUser;
	private User assignedUser;
	private Region region;
	private District district;
	private Community community;
	private Facility facility;
	private PointOfEntry pointOfEntry;

	public User getReportingUser() {
		return reportingUser;
	}

	public BaseAccessEntities reportingUserId(User reportingUser) {
		this.reportingUser = reportingUser;
		return this;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public BaseAccessEntities assignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
		return this;
	}

	public Region getRegion() {
		return region;
	}

	public BaseAccessEntities region(Region region) {
		this.region = region;
		return this;
	}

	public District getDistrict() {
		return district;
	}

	public BaseAccessEntities district(District district) {
		this.district = district;
		return this;
	}

	public Community getCommunity() {
		return community;
	}

	public BaseAccessEntities community(Community community) {
		this.community = community;
		return this;
	}

	public Facility getFacility() {
		return facility;
	}

	public BaseAccessEntities facility(Facility facility) {
		this.facility = facility;
		return this;
	}

	public PointOfEntry getPointOfEntry() {
		return pointOfEntry;
	}

	public BaseAccessEntities pointOfEntry(PointOfEntry pointOfEntry) {
		this.pointOfEntry = pointOfEntry;
		return this;
	}

}
