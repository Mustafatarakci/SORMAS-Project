package de.symeda.sormas.ui.events.groups;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.event.EventGroupReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class EventGroupMemberListComponent extends VerticalLayout {

	private EventGroupReferenceDto eventGroupReference;
	private EventGroupMemberList list;
	private Button createButton;

	public EventGroupMemberListComponent(EventGroupReferenceDto eventGroupReference) {

		this.eventGroupReference = eventGroupReference;

		createEventListComponent(new EventGroupMemberList(eventGroupReference), I18nProperties.getString(Strings.entityEvents));

	}

	private void createEventListComponent(EventGroupMemberList eventList, String heading) {
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

		if (UserProvider.getCurrent().hasUserRight(UserRight.EVENTGROUP_LINK)) {
			createButton = ButtonHelper.createButton(I18nProperties.getCaption(Captions.linkEvent));
			createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			createButton.setIcon(VaadinIcons.PLUS_CIRCLE);
			createButton.addClickListener(event -> ControllerProvider.getEventController().selectEvent(eventGroupReference));
			componentHeader.addComponent(createButton);
			componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
		}
	}
}
