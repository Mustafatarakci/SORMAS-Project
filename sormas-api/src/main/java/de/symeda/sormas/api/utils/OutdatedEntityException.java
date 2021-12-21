package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.EntityDto;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Thrown when a entity that is supposed to be saved is older than the current
 * version in the database
 */
@SuppressWarnings("serial")
public class OutdatedEntityException extends ValidationRuntimeException {

	public OutdatedEntityException(String entityUuid, Class<? extends EntityDto> entityClass, Date dtoChangeDate, Timestamp entityChangeDate) {
		super(
			String.format(
				"%s is older (%s) than current version on server (%s): '%s'",
				entityClass.getSimpleName(),
				dtoChangeDate,
				entityChangeDate,
				entityUuid));
	}
}
