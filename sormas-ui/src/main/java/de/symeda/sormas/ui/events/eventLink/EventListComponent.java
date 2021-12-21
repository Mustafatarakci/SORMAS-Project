
package de.symeda.sormas.ui.events.eventLink;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventCriteria;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class EventListComponent extends VerticalLayout {

	private EventList list;
	private Button createButton;

	public EventListComponent(CaseReferenceDto caseRef) {

		createEventListComponent(new EventList(caseRef), I18nProperties.getString(Strings.entityEvents), false, e -> {

			EventCriteria eventCriteria = new EventCriteria();

			//check if there are active events in the database
			long events = FacadeProvider.getEventFacade().count(eventCriteria);
			if (events > 0) {
				ControllerProvider.getEventController().selectOrCreateEvent(caseRef);
			} else {
				ControllerProvider.getEventController().create(caseRef);
			}
		});

	}

	public EventListComponent(ContactReferenceDto contactRef) {

		ContactDto contact = FacadeProvider.getContactFacade().getContactByUuid(contactRef.getUuid());

		EventList eventList = new EventList(contact.getPerson());

		createEventListComponent(eventList, I18nProperties.getString(Strings.entityEvents), false, e -> {

			EventCriteria eventCriteria = new EventCriteria();

			//check if there are active events in the database
			long events = FacadeProvider.getEventFacade().count(eventCriteria);
			if (events > 0) {
				ControllerProvider.getEventController().selectOrCreateEvent(contact);
			} else {
				ControllerProvider.getEventController().create(contact);
			}
		});

		if (contact.getCaze() != null) {
			CheckBox contactOnlyWithSourceCaseInEvent = new CheckBox(I18nProperties.getCaption(Captions.eventOnlyWithContactSourceCaseInvolved));
			contactOnlyWithSourceCaseInEvent.addStyleNames(CssStyles.CHECKBOX_FILTER_INLINE, CssStyles.VSPACE_4);
			contactOnlyWithSourceCaseInEvent.setWidthFull();
			contactOnlyWithSourceCaseInEvent.addValueChangeListener(e -> {
				if (e.getValue()) {
					eventList.filterEventListByCase(contact.getCaze());
				} else {
					eventList.filterEventListByCase(null);
				}
				eventList.reload();
			});
			addComponent(contactOnlyWithSourceCaseInEvent, 1);
		}
	}

	public EventListComponent(EventReferenceDto superordinateEvent) {

		EventList eventList = new EventList(superordinateEvent);
		createEventListComponent(eventList, I18nProperties.getCaption(Captions.eventSubordinateEvents), true, e -> {
			EventCriteria eventCriteria = new EventCriteria();
			long events = FacadeProvider.getEventFacade().count(eventCriteria);
			if (events > 0) {
				ControllerProvider.getEventController().selectOrCreateSubordinateEvent(superordinateEvent);
			} else {
				ControllerProvider.getEventController().createSubordinateEvent(superordinateEvent);
			}
		});
	}

	private void createEventListComponent(EventList eventList, String heading, boolean bottomCreateButton, Button.ClickListener clickListener) {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
		setSpacing(false);

		HorizontalLayout componentHeader = new HorizontalLayout();
		componentHeader.setMargin(false);
		componentHeader.setSpacing(false);
		componentHeader.setWidth(100, Unit.PERCENTAGE);
		addComponent(componentHeader);

		list = eventList;
		addComponent(list);
		list.reload();

		Label eventLabel = new Label(heading);
		eventLabel.addStyleName(CssStyles.H3);
		componentHeader.addComponent(eventLabel);

		if (UserProvider.getCurrent().hasUserRight(UserRight.EVENT_CREATE)) {
			createButton = ButtonHelper.createButton(I18nProperties.getCaption(Captions.linkEvent));
			createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			createButton.setIcon(VaadinIcons.PLUS_CIRCLE);
			createButton.addClickListener(clickListener);
			if (bottomCreateButton) {
				HorizontalLayout buttonLayout = new HorizontalLayout();
				buttonLayout.setMargin(false);
				buttonLayout.setSpacing(true);
				buttonLayout.setWidth(100, Unit.PERCENTAGE);
				CssStyles.style(buttonLayout, CssStyles.VSPACE_TOP_3);
				buttonLayout.addComponent(createButton);
				addComponent(buttonLayout);
			} else {
				componentHeader.addComponent(createButton);
				componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
			}
		}
	}
}
