package de.symeda.sormas.ui.dashboard.contacts;

import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.contacts.epicurve.ContactsEpiCurveBuilder;
import de.symeda.sormas.ui.dashboard.contacts.epicurve.ContactsEpiCurveBuilders;
import de.symeda.sormas.ui.dashboard.diagram.AbstractEpiCurveComponent;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class ContactsEpiCurveComponent extends AbstractEpiCurveComponent {

	private static final long serialVersionUID = 6582975657305031105L;

	private ContactsEpiCurveMode epiCurveContactsMode;

	public ContactsEpiCurveComponent(DashboardDataProvider dashboardDataProvider) {
		super(dashboardDataProvider);
	}

	@Override
	protected PopupButton createEpiCurveModeSelector() {
		if (epiCurveContactsMode == null) {
			epiCurveContactsMode = ContactsEpiCurveMode.FOLLOW_UP_STATUS;
			epiCurveLabel.setValue(I18nProperties.getCaption(Captions.dashboardFollowUpStatusChart));
		}

		VerticalLayout groupingLayout = new VerticalLayout();
		groupingLayout.setMargin(true);
		groupingLayout.setSizeUndefined();

		PopupButton dataDropdown = ButtonHelper.createPopupButton(Captions.dashboardData, groupingLayout, CssStyles.BUTTON_SUBTLE);

		OptionGroup dataSelect = new OptionGroup();
		dataSelect.setWidth(100, Unit.PERCENTAGE);
		dataSelect.addItems((Object[]) ContactsEpiCurveMode.values());
		dataSelect.setValue(epiCurveContactsMode);
		dataSelect.select(epiCurveContactsMode);
		dataSelect.addValueChangeListener(e -> {
			epiCurveContactsMode = (ContactsEpiCurveMode) e.getProperty().getValue();
			switch (epiCurveContactsMode) {
			case FOLLOW_UP_STATUS:
				epiCurveLabel.setValue(I18nProperties.getCaption(Captions.dashboardFollowUpStatusChart));
				break;
			case CONTACT_CLASSIFICATION:
				epiCurveLabel.setValue(I18nProperties.getCaption(Captions.dashboardContactClassificationChart));
				break;
			case FOLLOW_UP_UNTIL:
				epiCurveLabel.setValue(I18nProperties.getCaption(Captions.dashboardFollowUpUntilChart));
				break;
			}
			clearAndFillEpiCurveChart();
		});
		groupingLayout.addComponent(dataSelect);

		return dataDropdown;
	}

	@Override
	public void clearAndFillEpiCurveChart() {
		ContactsEpiCurveBuilder contactsEpiCurveBuilder = ContactsEpiCurveBuilders.getEpiCurveBuilder(epiCurveContactsMode, epiCurveGrouping);
		epiCurveChart.setHcjs(contactsEpiCurveBuilder.buildFrom(buildListOfFilteredDates(), dashboardDataProvider));
	}
}
