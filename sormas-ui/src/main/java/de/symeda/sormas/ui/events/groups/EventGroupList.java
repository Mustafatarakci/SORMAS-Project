package de.symeda.sormas.ui.events.groups;

import java.util.List;

import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventCriteria;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventGroupCriteria;
import de.symeda.sormas.api.event.EventGroupIndexDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.PaginationList;

public class EventGroupList extends PaginationList<EventGroupIndexDto> {

	private final EventReferenceDto event;
	private final EventGroupCriteria eventGroupCriteria = new EventGroupCriteria();
	private final Label noEventGroupLabel;

	public EventGroupList(EventReferenceDto event) {
		super(5);

		this.event = event;

		this.eventGroupCriteria.setEvent(event);
		this.eventGroupCriteria.setUserFilterIncluded(false);
		this.eventGroupCriteria.relevanceStatus(EntityRelevanceStatus.ACTIVE);
		this.noEventGroupLabel = new Label(I18nProperties.getString(Strings.infoNoEventGroups));
	}

	@Override
	public void reload() {
		List<EventGroupIndexDto> events = FacadeProvider.getEventGroupFacade().getIndexList(eventGroupCriteria, 0, maxDisplayedEntries * 20, null);

		setEntries(events);
		if (!events.isEmpty()) {
			showPage(1);
		} else {
			listLayout.removeAllComponents();
			updatePaginationLayout();
			listLayout.addComponent(noEventGroupLabel);
		}
	}

	@Override
	protected void drawDisplayedEntries() {
		EventDto event = FacadeProvider.getEventFacade().getEventByUuid(this.event.getUuid(), false);
		List<EventGroupIndexDto> displayedEntries = getDisplayedEntries();
		for (int i = 0, displayedEntriesSize = displayedEntries.size(); i < displayedEntriesSize; i++) {
			EventGroupIndexDto eventGroup = displayedEntries.get(i);
			EventGroupListEntry listEntry = new EventGroupListEntry(eventGroup);

			UserProvider user = UserProvider.getCurrent();
			if (user.hasUserRight(UserRight.EVENTGROUP_LINK)) {
				listEntry.addUnlinkEventListener(i, (ClickListener) clickEvent -> {
					if (!user.hasNationalJurisdictionLevel() && !user.hasRegion(event.getEventLocation().getRegion())) {
						new Notification(
							I18nProperties.getString(Strings.headingEventGroupUnlinkEventIssue),
							I18nProperties.getString(Strings.errorEventFromAnotherJurisdiction),
							Notification.Type.ERROR_MESSAGE,
							false).show(Page.getCurrent());
						return;
					}

					ControllerProvider.getEventGroupController().unlinkEventGroup(this.event, listEntry.getEventGroup().toReference());
					reload();
				});
			}
			if (user.hasUserRight(UserRight.EVENTGROUP_EDIT)) {
				listEntry.addEditListener(i, (ClickListener) clickEvent -> {
					ControllerProvider.getEventGroupController().navigateToData(listEntry.getEventGroup().getUuid());
				});
			}
			listEntry.addListEventsListener(i, (ClickListener) clickEvent -> {
				EventCriteria eventCriteria = new EventCriteria();
				eventCriteria.setEventGroup(listEntry.getEventGroup().toReference());
				ControllerProvider.getEventController().navigateTo(eventCriteria);
			});
			listLayout.addComponent(listEntry);
		}
	}
}
