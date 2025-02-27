/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.deletionconfiguration.DeletionInfoDto;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public interface CoreFacade<DTO extends EntityDto, INDEX_DTO extends Serializable, REF_DTO extends ReferenceDto, CRITERIA extends BaseCriteria>
	extends BaseFacade<DTO, INDEX_DTO, REF_DTO, CRITERIA> {

	boolean isArchived(String uuid);

	void delete(String uuid, DeletionDetails deletionDetails);

	boolean exists(String uuid);

	List<DTO> getAllAfter(Date date, Integer batchSize, String lastSynchronizedUuid);

	DeletionInfoDto getAutomaticDeletionInfo(String uuid);

	DeletionInfoDto getManuallyDeletionInfo(String uuid);

	void archive(String entityUuid, Date endOfProcessingDate);

	void archive(List<String> entityUuid);

	void dearchive(List<String> entityUuids, String dearchiveReason);

	Date calculateEndOfProcessingDate(String entityUuids);

}
