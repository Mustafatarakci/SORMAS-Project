package de.symeda.sormas.ui.contact;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.person.ApproximateAgeType.ApproximateAgeHelper;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.ui.AbstractInfoLayout;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.DateFormatHelper;

@SuppressWarnings("serial")
public class ContactInfoLayout extends AbstractInfoLayout<ContactDto> {

	private final ContactDto contactDto;

	public ContactInfoLayout(ContactDto contactDto, UiFieldAccessCheckers fieldAccessCheckers) {
		super(ContactDto.class, fieldAccessCheckers);

		this.contactDto = contactDto;
		setSpacing(true);
		setMargin(false);
		setWidth(100, Unit.PERCENTAGE);
		updateContactInfo();
	}

	private void updateContactInfo() {

		this.removeAllComponents();

		final PersonDto personDto = FacadeProvider.getPersonFacade().getPersonByUuid(contactDto.getPerson().getUuid());

		final VerticalLayout firstColumn = new VerticalLayout();
		firstColumn.setMargin(false);
		firstColumn.setSpacing(true);

		{
			addDescLabel(
				firstColumn,
				ContactDto.UUID,
				DataHelper.getShortUuid(contactDto.getUuid()),
				I18nProperties.getPrefixCaption(ContactDto.I18N_PREFIX, ContactDto.UUID)).setDescription(contactDto.getUuid());

			addDescLabel(
				firstColumn,
				ContactDto.PERSON,
				contactDto.getPerson(),
				I18nProperties.getPrefixCaption(ContactDto.I18N_PREFIX, ContactDto.PERSON));

			if (UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_VIEW)) {

				final HorizontalLayout ageSexRow = new HorizontalLayout();
				ageSexRow.setMargin(false);
				ageSexRow.setSpacing(true);

				addCustomDescLabel(
					ageSexRow,
					PersonDto.class,
					PersonDto.APPROXIMATE_AGE,
					ApproximateAgeHelper.formatApproximateAge(personDto.getApproximateAge(), personDto.getApproximateAgeType()),
					I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.APPROXIMATE_AGE));

				addCustomDescLabel(
					ageSexRow,
					PersonDto.class,
					PersonDto.SEX,
					personDto.getSex(),
					I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.SEX));
				firstColumn.addComponent(ageSexRow);

				addDescLabel(
					firstColumn,
					ContactDto.CONTACT_OFFICER,
					contactDto.getContactOfficer(),
					I18nProperties.getPrefixCaption(ContactDto.I18N_PREFIX, ContactDto.CONTACT_OFFICER));
			}
		}
		this.addComponent(firstColumn);

		final VerticalLayout secondColumn = new VerticalLayout();
		secondColumn.setMargin(false);
		secondColumn.setSpacing(true);

		if (UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_VIEW)) {

			addDescLabel(
				secondColumn,
				ContactDto.DISEASE,
				contactDto.getDisease(),
				I18nProperties.getPrefixCaption(ContactDto.I18N_PREFIX, ContactDto.DISEASE));

			addDescLabel(
				secondColumn,
				ContactDto.CONTACT_CLASSIFICATION,
				contactDto.getContactClassification(),
				I18nProperties.getPrefixCaption(ContactDto.I18N_PREFIX, ContactDto.CONTACT_CLASSIFICATION));

			addDescLabel(
				secondColumn,
				ContactDto.LAST_CONTACT_DATE,
				DateFormatHelper.formatDate(contactDto.getLastContactDate()),
				I18nProperties.getPrefixCaption(ContactDto.I18N_PREFIX, ContactDto.LAST_CONTACT_DATE));
		}
		this.addComponent(secondColumn);
	}
}
