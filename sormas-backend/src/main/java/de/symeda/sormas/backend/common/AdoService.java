package de.symeda.sormas.backend.common;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.validation.constraints.NotNull;

public interface AdoService<ADO extends AbstractDomainObject> {

	List<ADO> getAll();

	ADO getById(long id);

	ADO getByUuid(String uuid);

	Boolean exists(@NotNull String uuid);

	/**
	 * <b>DELETES</b> an entity from the database!
	 * 
	 * @param deleteme
	 */
	void delete(ADO deleteme);

	/**
	 * Speichert ein neues Objekt in der Datenbank.
	 * Es darf vorher keine id haben und ist danch attacht.
	 */
	void persist(ADO persistme);

	/**
	 * Zum Speichern, wenn das ado neu oder direkt aus der Datenbank ist.<br/>
	 * Ruft persist() für neue Entities auf,
	 * bei attachten wird nichts gemacht.
	 * <br/>
	 * Das ado ist nach dem Aufruf attacht.
	 * 
	 * @param ado
	 * @throws EntityExistsException
	 *             wenn das ado detacht ist
	 */
	void ensurePersisted(ADO ado) throws EntityExistsException;

	/**
	 * JPA-Session flushen
	 */
	void doFlush();
}
