package de.symeda.sormas.ui.utils;

import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.ui.Field;

import de.symeda.sormas.api.EntityDto;

public interface DtoEditForm<DTO extends EntityDto> extends Field<DTO> {

	DTO getDto();

	void setDto(DTO dto);

	FieldGroup getFieldGroup();
}
