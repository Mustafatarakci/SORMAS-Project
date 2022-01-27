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

package de.symeda.sormas.backend.sample.sampleaccess;

import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.backend.access.AbstractAccessLogic;
import de.symeda.sormas.backend.access.AbstractEntityAccess;
import de.symeda.sormas.backend.access.AccessChangeOperation;
import de.symeda.sormas.backend.access.BaseAccessEntities;

public abstract class SampleAccessLogic extends AbstractAccessLogic {

	public static List<AccessChangeOperation> buildAccessChangeOperations(BaseAccessEntities sampleAccessEntities, SampleDto updatedSample) {

		List<AccessChangeOperation> changeOperations = new ArrayList<>();

		insertChangeOperation(
			changeOperations,
			sampleAccessEntities.getFacility(),
			updatedSample.getLab(),
			AbstractEntityAccess.FACILITY,
			SampleDto.LAB);

		return changeOperations;
	}

}
