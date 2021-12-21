package de.symeda.sormas.ui.events.groups;

import java.util.List;

import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.event.EventCriteria;
import de.symeda.sormas.api.event.EventGroupReferenceDto;
import de.symeda.sormas.api.event.EventIndexDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.PaginationList;

public class EventGroupMemberList extends PaginationList<EventIndexDto> {

	private final EventGroupReferenceDto eventGroupReference;
	private final EventCriteria eventCriteria = new EventCriteria();
	private final Label noEventLabel;

	public EventGroupMemberList(EventGroupReferenceDto eventGroupReference) {
		super(10);

		this.eventGroupReference = eventGroupReference;

		eventCriteria.setEventGroup(eventGroupReference);
		eventCriteria.setUserFilterIncluded(false);
		noEventLabel = new Label(I18nProperties.getCaption(Captions.eventNoEventLinkedToEventGroup));
	}

	@Override
	public void reload() {
		List<EventIndexDto> events = FacadeProvider.getEventFacade().getIndexList(eventCriteria, 0, maxDisplayedEntries * 20, null);

		setEntries(events);
		if (!events.isEmpty()) {
			showPage(1);
		} else {
			listLayout.removeAllComponents();
			updatePaginationLayout();
			listLayout.addComponent(noEventLabel);
		}
	}

	@Override
	protected void drawDisplayedEntries() {
		List<EventIndexDto> displayedEntries = getDisplayedEntries();
		for (int i = 0, displayedEntriesSize = displayedEntries.size(); i < displayedEntriesSize; i++) {
			EventIndexDto event = displayedEntries.get(i);
			EventGroupMemberListEntry listEntry = new EventGroupMemberListEntry(event);

			UserProvider user = UserProvider.getCurrent();
			if (user.hasUserRight(UserRight.EVENTGROUP_LINK)) {
				listEntry.addUnlinkEventListener(i, (ClickListener) clickEvent -> {
					if (!user.hasNationalJurisdictionLevel() && !user.hasRegion(new RegionReferenceDto(event.getRegionUuid()))) {
						new Notification(
							I18nProperties.getString(Strings.headingEventGroupUnlinkEventIssue),
							I18nProperties.getString(Strings.errorEventFromAnotherJurisdiction),
							Notification.Type.ERROR_MESSAGE,
							false).show(Page.getCurrent());
						return;
					}

					ControllerProvider.getEventGroupController().unlinkEventGroup(event.toReference(), eventGroupReference);
					reload();
				});
			}
			if (user.hasUserRight(UserRight.EVENTGROUP_EDIT)) {
				listEntry.addEditListener(
					i,
					(ClickListener) clickEvent -> ControllerProvider.getEventController().navigateToData(listEntry.getEvent().getUuid()));
			}
			listLayout.addComponent(listEntry);
		}
	}

	protected void filterEventListByCase(CaseReferenceDto caseRef) {
		eventCriteria.caze(caseRef);
	}
}
