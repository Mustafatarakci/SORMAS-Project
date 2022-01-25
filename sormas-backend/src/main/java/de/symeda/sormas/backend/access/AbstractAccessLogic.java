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

import java.util.List;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.backend.common.AbstractDomainObject;

public abstract class AbstractAccessLogic {

	public static void insertChangeOperation(
		List<AccessChangeOperation> changeOperations,
		AbstractDomainObject existingEntity,
		ReferenceDto updatedEntity,
		String property,
		String entityProperty) {

		insertChangeOperation(changeOperations, existingEntity, updatedEntity, property, entityProperty, true, false);
	}

	public static void insertChangeOperation(
		List<AccessChangeOperation> changeOperations,
		AbstractDomainObject existingEntity,
		ReferenceDto updatedEntity,
		String property,
		String entityProperty,
		boolean primaryData,
		boolean wouldBeInserted) {

		AccessChangeOperationType changeOperationType =
			EntityAccessLogic.getAccessChangeOperationType(existingEntity, updatedEntity, wouldBeInserted);
		if (changeOperationType != null) {
			changeOperations.add(
				new AccessChangeOperation(
					property,
					entityProperty,
					changeOperationType,
					primaryData,
					existingEntity != null ? existingEntity.getId() : null));
		}
	}

}
