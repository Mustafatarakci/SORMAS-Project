package de.symeda.sormas.ui.contact;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class ContactListComponent extends VerticalLayout {

	private final ContactList list;

	public ContactListComponent(EventParticipantReferenceDto eventParticipantRef) {
		HorizontalLayout componentHeader = new HorizontalLayout();
		componentHeader.setMargin(false);
		componentHeader.setSpacing(false);
		componentHeader.setWidth(100, Unit.PERCENTAGE);
		addComponent(componentHeader);

		Label contactsHeader = new Label(I18nProperties.getString(Strings.entityContacts));
		contactsHeader.addStyleName(CssStyles.H3);
		componentHeader.addComponent(contactsHeader);

		ContactList contactList = new ContactList(eventParticipantRef);
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
		setSpacing(false);

		EventReferenceDto eventRef = FacadeProvider.getEventFacade().getReferenceByEventParticipant(eventParticipantRef.getUuid());
		CheckBox contactOnlyWithSourceCaseInGivenEvent = new CheckBox(I18nProperties.getCaption(Captions.contactOnlyWithSourceCaseInGivenEvent));
		contactOnlyWithSourceCaseInGivenEvent.addStyleNames(CssStyles.CHECKBOX_FILTER_INLINE, CssStyles.VSPACE_4);
		contactOnlyWithSourceCaseInGivenEvent.addValueChangeListener(e -> {
			if (e.getValue()) {
				contactList.filterContactListBySourceCaseInGivenEvent(eventRef);
			} else {
				contactList.filterContactListBySourceCaseInGivenEvent(null);
			}
			contactList.reload();
		});
		addComponent(contactOnlyWithSourceCaseInGivenEvent);

		list = contactList;
		addComponent(list);
		list.reload();

		if (UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_CREATE)) {
			Button createButton = ButtonHelper.createButton(I18nProperties.getCaption(Captions.contactNewContact));
			createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			createButton.setIcon(VaadinIcons.PLUS_CIRCLE);
			createButton.addClickListener(e -> ControllerProvider.getContactController().create(eventParticipantRef));
			componentHeader.addComponent(createButton);
			componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
		}
	}

	public void reload() {
		list.reload();
	}
}
