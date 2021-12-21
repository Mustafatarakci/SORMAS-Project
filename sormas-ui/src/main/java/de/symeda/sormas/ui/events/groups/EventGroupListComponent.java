package de.symeda.sormas.ui.events.groups;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventGroupCriteria;
import de.symeda.sormas.api.event.EventGroupReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class EventGroupListComponent extends VerticalLayout {

	private EventGroupList list;
	private Button createButton;

	public EventGroupListComponent(EventReferenceDto eventReference) {

		EventGroupList eventList = new EventGroupList(eventReference);
		createEventGroupListComponent(eventList, I18nProperties.getCaption(Captions.eventGroups), e -> {
			EventDto event = FacadeProvider.getEventFacade().getEventByUuid(eventReference.getUuid(), false);
			UserProvider user = UserProvider.getCurrent();
			if (!user.hasNationalJurisdictionLevel() && !user.hasRegion(event.getEventLocation().getRegion())) {
				new Notification(
					I18nProperties.getString(Strings.headingEventGroupLinkEventIssue),
					I18nProperties.getString(Strings.errorEventFromAnotherJurisdiction),
					Notification.Type.ERROR_MESSAGE,
					false).show(Page.getCurrent());
				return;
			}

			EventGroupCriteria eventGroupCriteria = new EventGroupCriteria();
			Set<String> eventGroupUuids = FacadeProvider.getEventGroupFacade()
				.getCommonEventGroupsByEvents(Collections.singletonList(event.toReference()))
				.stream()
				.map(EventGroupReferenceDto::getUuid)
				.collect(Collectors.toSet());
			eventGroupCriteria.setExcludedUuids(eventGroupUuids);
			if (user.hasUserRight(UserRight.EVENTGROUP_CREATE) && user.hasUserRight(UserRight.EVENTGROUP_LINK)) {
				long events = FacadeProvider.getEventGroupFacade().count(eventGroupCriteria);
				if (events > 0) {
					ControllerProvider.getEventGroupController().selectOrCreate(eventReference);
				} else {
					ControllerProvider.getEventGroupController().create(eventReference);
				}
			} else if (user.hasUserRight(UserRight.EVENTGROUP_CREATE)) {
				ControllerProvider.getEventGroupController().create(eventReference);
			} else {
				long events = FacadeProvider.getEventGroupFacade().count(eventGroupCriteria);
				if (events > 0) {
					ControllerProvider.getEventGroupController().select(eventReference);
				} else {
					new Notification(
						I18nProperties.getString(Strings.headingEventGroupLinkEventIssue),
						I18nProperties.getString(Strings.errorNotRequiredRights),
						Notification.Type.ERROR_MESSAGE,
						false).show(Page.getCurrent());
				}
			}
		});
	}

	private void createEventGroupListComponent(EventGroupList eventList, String heading, Button.ClickListener clickListener) {
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

		UserProvider user = UserProvider.getCurrent();
		if (user.hasUserRight(UserRight.EVENTGROUP_CREATE) || user.hasUserRight(UserRight.EVENTGROUP_LINK)) {
			createButton = ButtonHelper.createButton(I18nProperties.getCaption(Captions.linkEventGroup));
			createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			createButton.setIcon(VaadinIcons.PLUS_CIRCLE);
			createButton.addClickListener(clickListener);
			componentHeader.addComponent(createButton);
			componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
		}
	}
}
