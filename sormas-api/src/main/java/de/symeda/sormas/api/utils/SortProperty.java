package de.symeda.sormas.api.utils;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SortProperty implements Serializable {

	private static final long serialVersionUID = 2972594862424083789L;

	public final String propertyName;

	public final boolean ascending;


	public SortProperty(String propertyName) {
		this(propertyName, true);
	}

	@JsonCreator
	public SortProperty(@JsonProperty("propertyName") String propertyName, @JsonProperty("ascending") boolean ascending) {
		this.propertyName = propertyName;
		this.ascending = ascending;
	}


}
