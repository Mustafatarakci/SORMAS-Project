package de.symeda.sormas.ui.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.PopupDateField;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DateFilterOption;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.EpiWeek;

public class EpiWeekAndDateFilterComponent<DATE_TYPE> extends HorizontalLayout {

	private static final long serialVersionUID = 8752630393182185034L;

	private final ComboBox dateFilterOptionFilter;
	private final ComboBox dateTypeSelector;
	private final ComboBox weekFromFilter;
	private final ComboBox weekToFilter;
	private final PopupDateField dateFromFilter;
	private final PopupDateField dateToFilter;

	public EpiWeekAndDateFilterComponent(boolean fillAutomatically, boolean showCaption, String infoText, AbstractFilterForm parentFilterForm) {
		this(fillAutomatically, showCaption, infoText, null, null, null, parentFilterForm);
	}

	public EpiWeekAndDateFilterComponent(
		boolean fillAutomatically,
		boolean showCaption,
		String infoText,
		DATE_TYPE[] dateTypes,
		String dateTypePrompt,
		DATE_TYPE defaultDateType,
		AbstractFilterForm parentFilterForm) {
		setSpacing(true);

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		dateFilterOptionFilter = ComboBoxHelper.createComboBoxV7();
		dateTypeSelector = ComboBoxHelper.createComboBoxV7();
		weekFromFilter = ComboBoxHelper.createComboBoxV7();
		weekToFilter = ComboBoxHelper.createComboBoxV7();
		dateFromFilter = new PopupDateField();
		dateToFilter = new PopupDateField();

		// Date filter options
		dateFilterOptionFilter.setId("dateFilterOption");
		dateFilterOptionFilter.setWidth(200, Unit.PIXELS);
		dateFilterOptionFilter.addItems((Object[]) DateFilterOption.values());
		dateFilterOptionFilter.setNullSelectionAllowed(false);
		dateFilterOptionFilter.select(DateFilterOption.EPI_WEEK);
		if (showCaption) {
			dateFilterOptionFilter.setCaption(I18nProperties.getCaption(Captions.dashboardCustomPeriod));
		}

		dateFilterOptionFilter.addValueChangeListener(e -> {
			if (e.getProperty().getValue() == DateFilterOption.DATE) {
				int newIndex = getComponentIndex(weekFromFilter);
				removeComponent(weekFromFilter);
				removeComponent(weekToFilter);
				addComponent(dateFromFilter, newIndex);
				addComponent(dateToFilter, newIndex + 1);

				if (fillAutomatically) {
					dateFromFilter.setValue(DateHelper.subtractDays(c.getTime(), 7));
				}
				if (fillAutomatically) {
					dateToFilter.setValue(c.getTime());
				}
			} else if (getComponentIndex(dateFromFilter) != -1) {
				int newIndex = getComponentIndex(dateFromFilter);
				removeComponent(dateFromFilter);
				removeComponent(dateToFilter);
				addComponent(weekFromFilter, newIndex);
				addComponent(weekToFilter, newIndex + 1);

				if (fillAutomatically) {
					weekFromFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
				}
				if (fillAutomatically) {
					weekToFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
				}
			}
		});
		addComponent(dateFilterOptionFilter);

		// New case date type selector
		if (dateTypes != null) {
			dateTypeSelector.setId("dateType");
			dateTypeSelector.setWidth(200, Unit.PIXELS);
			dateTypeSelector.addItems(dateTypes);
			if (dateTypePrompt != null) {
				dateTypeSelector.setInputPrompt(dateTypePrompt);
			}
			if (defaultDateType != null) {
				dateTypeSelector.select(defaultDateType);
			}
			if (showCaption) {
				CssStyles.style(dateTypeSelector, CssStyles.FORCE_CAPTION);
			}
			addComponent(dateTypeSelector);

			if (!StringUtils.isEmpty(infoText)) {
				Label infoLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml(), ContentMode.HTML);
				infoLabel.setSizeUndefined();
				infoLabel.setDescription(infoText, ContentMode.HTML);
				CssStyles.style(infoLabel, CssStyles.LABEL_XLARGE, CssStyles.LABEL_SECONDARY);
				addComponent(infoLabel);
			}
		}

		// Epi week filter
		List<EpiWeek> epiWeekList = DateHelper.createEpiWeekList(c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR));

		weekFromFilter.setId("weekFrom");
		weekFromFilter.setWidth(200, Unit.PIXELS);
		for (EpiWeek week : epiWeekList) {
			weekFromFilter.addItem(week);
		}
		weekFromFilter.setNullSelectionAllowed(false);
		if (fillAutomatically) {
			weekFromFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
		}
		if (showCaption) {
			weekFromFilter.setCaption(I18nProperties.getCaption(Captions.epiWeekFrom));
		}
		addComponent(weekFromFilter);

		weekToFilter.setId("weekTo");
		weekToFilter.setWidth(200, Unit.PIXELS);
		for (EpiWeek week : epiWeekList) {
			weekToFilter.addItem(week);
		}
		weekToFilter.setNullSelectionAllowed(false);
		if (fillAutomatically) {
			weekToFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
		}
		if (showCaption) {
			weekToFilter.setCaption(I18nProperties.getCaption(Captions.epiWeekTo));
		}
		addComponent(weekToFilter);

		// Date filter
		dateFromFilter.setId("dateFrom");
		dateFromFilter.setWidth(200, Unit.PIXELS);
		if (showCaption) {
			dateFromFilter.setCaption(I18nProperties.getCaption(Captions.from));
		}

		dateToFilter.setId("dateTo");
		dateToFilter.setWidth(200, Unit.PIXELS);
		if (showCaption) {
			dateToFilter.setCaption(I18nProperties.getCaption(Captions.to));
		}

		if (parentFilterForm != null) {
			dateFilterOptionFilter.addValueChangeListener(e -> parentFilterForm.onChange());
			dateTypeSelector.addValueChangeListener(e -> parentFilterForm.onChange());
			weekFromFilter.addValueChangeListener(e -> parentFilterForm.onChange());
			weekToFilter.addValueChangeListener(e -> parentFilterForm.onChange());
			dateFromFilter.addValueChangeListener(e -> parentFilterForm.onChange());
			dateToFilter.addValueChangeListener(e -> parentFilterForm.onChange());
		}
	}

	public void setNotificationsForMissingFilters() {
		DateFilterOption dateFilterOption = (DateFilterOption) dateFilterOptionFilter.getValue();
		Notification notification;
		if (dateFilterOption == DateFilterOption.DATE) {
			notification = new Notification(
				I18nProperties.getString(Strings.headingMissingDateFilter),
				I18nProperties.getString(Strings.messageMissingDateFilter),
				Notification.Type.WARNING_MESSAGE,
				false);
		} else {
			notification = new Notification(
				I18nProperties.getString(Strings.headingMissingEpiWeekFilter),
				I18nProperties.getString(Strings.messageMissingEpiWeekFilter),
				Notification.Type.WARNING_MESSAGE,
				false);
		}
		notification.setDelayMsec(-1);
		notification.show(Page.getCurrent());
	}

	public ComboBox getDateFilterOptionFilter() {
		return dateFilterOptionFilter;
	}

	public ComboBox getDateTypeSelector() {
		return dateTypeSelector;
	}

	public ComboBox getWeekFromFilter() {
		return weekFromFilter;
	}

	public ComboBox getWeekToFilter() {
		return weekToFilter;
	}

	public PopupDateField getDateFromFilter() {
		return dateFromFilter;
	}

	public PopupDateField getDateToFilter() {
		return dateToFilter;
	}
}
