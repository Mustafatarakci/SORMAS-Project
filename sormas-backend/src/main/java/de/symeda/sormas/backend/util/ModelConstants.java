package de.symeda.sormas.backend.util;

public interface ModelConstants {

	String PERSISTENCE_UNIT_NAME = "SormasPU";
	String PERSISTENCE_UNIT_NAME_AUDITLOG = "auditlogPU";
	String HINT_HIBERNATE_READ_ONLY = "org.hibernate.readOnly";

	/**
	 * Hard limit how much parameter can be allowed in a query.<br />
	 * Typically this is an issue when an IN clause becomes too big for an SQL statement in PostgreSQL.
	 */
	int PARAMETER_LIMIT = 32_000;

	/**
	 * Data of the given Collection or Map is persisted as JSON representation in the database.
	 */
	String COLUMN_DEFINITION_JSON = "json";

	/**
	 * Hibernate takes care of transforming the given Java model to and from JSON content in the database.
	 */
	String HIBERNATE_TYPE_JSON = "json";
}
