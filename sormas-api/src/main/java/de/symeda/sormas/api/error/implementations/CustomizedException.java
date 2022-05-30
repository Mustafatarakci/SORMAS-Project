package de.symeda.sormas.api.error.implementations;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class CustomizedException extends RuntimeException {

	private Response.Status status;

	private String message;

	private String entity;

	public CustomizedException(CustomizedException exception) {
		this.status = exception.getStatus();
		this.message = exception.getMessage();
		this.entity = exception.getEntity();
	}

	public Response.Status getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getEntity() {
		return entity;
	}

	public CustomizedException(Response.Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public CustomizedException(Response.Status status, String message, String entity) {
		this.status = status;
		this.message = message;
		this.entity = entity;
	}

	public CustomizedException(Response.Status status, String message, Class entity) {
		this.status = status;
		this.message = message;
		this.entity = entity.getCanonicalName();
	}

}
