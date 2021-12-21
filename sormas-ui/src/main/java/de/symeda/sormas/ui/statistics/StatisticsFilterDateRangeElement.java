package de.symeda.sormas.ui.statistics;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.v7.ui.DateField;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;

@SuppressWarnings("serial")
public class StatisticsFilterDateRangeElement extends StatisticsFilterElement {

	private DateField dateFromField;
	private DateField dateToField;

	public StatisticsFilterDateRangeElement(int rowIndex) {

		setSpacing(true);

		dateFromField = new DateField(I18nProperties.getCaption(Captions.from));
		dateFromField.setId("dateFrom-" + rowIndex);
		dateToField = new DateField(I18nProperties.getCaption(Captions.to));
		dateToField.setId("dateTo-" + rowIndex);

		addComponent(dateFromField);
		addComponent(dateToField);
	}

	@Override
	public List<TokenizableValue> getSelectedValues() {

		List<TokenizableValue> values = new ArrayList<>();
		TokenizableValue fromValue = new TokenizableValue(dateFromField.getValue(), 0);
		TokenizableValue toValue = new TokenizableValue(dateToField.getValue(), 1);
		values.add(fromValue);
		values.add(toValue);
		return values;
	}
}
