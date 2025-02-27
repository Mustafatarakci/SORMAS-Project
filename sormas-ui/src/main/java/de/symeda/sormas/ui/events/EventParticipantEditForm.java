/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.events;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

import com.vaadin.ui.Button;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.PersonContext;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.person.PersonEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.PersonDependentEditForm;

public class EventParticipantEditForm extends PersonDependentEditForm<EventParticipantDto> {

	private static final long serialVersionUID = 1L;

	private static final String MEDICAL_INFORMATION_LOC = "medicalInformationLoc";

	private static final String HTML_LAYOUT = fluidRowLocs(EventParticipantDto.REGION, EventParticipantDto.DISTRICT)
		+ fluidRowLocs(EventParticipantDto.REPORTING_USER)
		+ fluidRowLocs(EventParticipantDto.INVOLVEMENT_DESCRIPTION)
		+ fluidRowLocs(PERSON_SEARCH_LOC)
		+ fluidRowLocs(EventParticipantDto.PERSON)
		+ loc(MEDICAL_INFORMATION_LOC)
		+ fluidRowLocs(EventParticipantDto.VACCINATION_STATUS)
		+ fluidRowLocs(CaseDataDto.DELETION_REASON)
		+ fluidRowLocs(CaseDataDto.OTHER_DELETION_REASON);

	private final EventDto event;
	private final Boolean searchPerson;

	private final boolean isPseudonymized;

	private final boolean isPersonPseudonymized;
	private PersonEditForm pef;
	private PersonDto originalPerson;
	private Button searchPersonButton;

	public EventParticipantEditForm(EventDto event, boolean isPseudonymized, boolean isPersonPseudonymized, boolean searchPerson) {
		super(
			EventParticipantDto.class,
			EventParticipantDto.I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withDisease(event.getDisease()),
			UiFieldAccessCheckers.getDefault(isPseudonymized));
		this.event = event;
		this.isPseudonymized = isPseudonymized;
		this.isPersonPseudonymized = isPersonPseudonymized;
		this.searchPerson = searchPerson;

		addFields();
	}

	@Override
	protected void addFields() {
		if (event == null) {
			// workaround to stop initialization until event is set
			return;
		}

		if (searchPerson) {
			searchPersonButton = createPersonSearchButton(PERSON_SEARCH_LOC);
			searchPersonButton.setCaption(I18nProperties.getString(Strings.infoSearchPersonOnDependentForm));
			getContent().addComponent(searchPersonButton, PERSON_SEARCH_LOC);
		}

		pef = new PersonEditForm(PersonContext.EVENT_PARTICIPANT, event.getDisease(), event.getDiseaseDetails(), null, isPersonPseudonymized);
		pef.setWidth(100, Unit.PERCENTAGE);
		pef.setImmediate(true);
		getFieldGroup().bind(pef, EventParticipantDto.PERSON);
		getContent().addComponent(pef, EventParticipantDto.PERSON);

		addField(EventParticipantDto.INVOLVEMENT_DESCRIPTION, TextField.class);

		ComboBox region = addInfrastructureField(EventParticipantDto.REGION);
		region.setDescription(I18nProperties.getPrefixDescription(EventParticipantDto.I18N_PREFIX, EventParticipantDto.REGION));
		ComboBox district = addInfrastructureField(EventParticipantDto.DISTRICT);
		district.setDescription(I18nProperties.getPrefixDescription(EventParticipantDto.I18N_PREFIX, EventParticipantDto.DISTRICT));

		region.addValueChangeListener(e -> {
			RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();

			FieldHelper
				.updateItems(district, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
		});

		region.addItems(FacadeProvider.getRegionFacade().getAllActiveByServerCountry());

		LocationDto locationDto = event.getEventLocation();
		boolean shouldBeRequired = locationDto.getRegion() == null || locationDto.getDistrict() == null;
		region.setRequired(shouldBeRequired);
		district.setRequired(shouldBeRequired);

		addField(EventParticipantDto.REPORTING_USER, ComboBox.class);
		setReadOnly(true, EventParticipantDto.REPORTING_USER);

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		addField(EventParticipantDto.VACCINATION_STATUS);

		addField(EventParticipantDto.DELETION_REASON);
		addField(EventParticipantDto.OTHER_DELETION_REASON, TextArea.class).setRows(3);
		setVisible(false, EventParticipantDto.DELETION_REASON, EventParticipantDto.OTHER_DELETION_REASON);
	}

	public String getPersonFirstName() {
		return (String) getField(PersonDto.FIRST_NAME).getValue();
	}

	public String getPersonLastName() {
		return (String) getField(PersonDto.LAST_NAME).getValue();
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

	@Override
	public void setValue(EventParticipantDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
		super.setValue(newFieldValue);
		this.originalPerson = newFieldValue.getPerson();
		this.pef.setValue(newFieldValue.getPerson());
	}

	@Override
	public void setPerson(PersonDto person) {
		if (person != null) {
			this.getValue().setPerson(person);
		} else {
			this.getValue().setPerson(originalPerson);
		}
		getFieldGroup().unbind(pef);
		pef = new PersonEditForm(
			PersonContext.EVENT_PARTICIPANT,
			event.getDisease(),
			event.getDiseaseDetails(),
			null,
			person != null ? person.isPseudonymized() : isPersonPseudonymized);
		pef.setWidth(100, Unit.PERCENTAGE);
		pef.setImmediate(true);
		getFieldGroup().bind(pef, EventParticipantDto.PERSON);
		getContent().addComponent(pef, EventParticipantDto.PERSON);
	}

	@Override
	protected void enablePersonFields(Boolean enable) {
		pef.getFieldGroup().setEnabled(enable);
	}
}
