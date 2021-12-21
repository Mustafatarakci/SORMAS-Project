package de.symeda.sormas.ui.utils;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.OptionGroup;

public class NullableOptionGroup extends OptionGroup {

	public NullableOptionGroup() {
		setup();
	}

	public NullableOptionGroup(String caption, Collection<?> options) {
		super(caption, options);
		setup();
	}

	public NullableOptionGroup(String caption, Container dataSource) {
		super(caption, dataSource);
		setup();
	}

	public NullableOptionGroup(String caption) {
		super(caption);
		setup();
	}

	private void setup() {
		super.setMultiSelect(!isRequired());
		if (isRequired()) {
			setConverter((Converter<Object, ?>) null);
		} else {
			setConverter(new NullableOptionGroupConverter(getFirstValue((Set) getValue())));
		}
	}

	@Override
	public void setMultiSelect(boolean multiSelect) {
		throw new UnsupportedOperationException();
	}

	public Object getNullableValue() {
		final Object value = super.getValue();
		return ObjectUtils.isNotEmpty(value) ? getFirstValue((Set) value) : null;
	}

	@Override
	public void setRequired(boolean required) {
		boolean readOnly = isReadOnly();
		setReadOnly(false);
		super.setRequired(required);
		setup();
		setReadOnly(readOnly);
	}

	private Object getFirstValue(Set value) {
		return value.stream().findFirst().orElse(null);
	}
}
