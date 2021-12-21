package de.symeda.sormas.ui.events;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SubMenu;
import de.symeda.sormas.ui.utils.AbstractDetailView;
import de.symeda.sormas.ui.utils.DirtyStateComponent;

@SuppressWarnings("serial")
public abstract class AbstractEventView extends AbstractDetailView<EventReferenceDto> {

	public static final String ROOT_VIEW_NAME = EventsView.VIEW_NAME;

	protected AbstractEventView(String viewName) {
		super(viewName);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		super.enter(event);
		initOrRedirect(event);
	}

	@Override
	public void refreshMenu(SubMenu menu, String params) {

		if (!findReferenceByParams(params)) {
			return;
		}

		menu.removeAllViews();
		menu.addView(EventsView.VIEW_NAME, I18nProperties.getCaption(Captions.eventEventsList));
		menu.addView(EventDataView.VIEW_NAME, I18nProperties.getCaption(EventDto.I18N_PREFIX), params);
		menu.addView(EventParticipantsView.VIEW_NAME, I18nProperties.getCaption(Captions.eventEventParticipants), params);
		menu.addView(EventActionsView.VIEW_NAME, I18nProperties.getCaption(Captions.eventEventActions), params);

		setMainHeaderComponent(ControllerProvider.getEventController().getEventViewTitleLayout(getReference().getUuid()));
	}

	@Override
	protected EventReferenceDto getReferenceByUuid(String uuid) {

		final EventReferenceDto reference;
		if (FacadeProvider.getEventFacade().exists(uuid)) {
			reference = FacadeProvider.getEventFacade().getReferenceByUuid(uuid);
		} else {
			reference = null;
		}
		return reference;
	}

	@Override
	protected String getRootViewName() {
		return ROOT_VIEW_NAME;
	}

	@Override
	protected void setSubComponent(DirtyStateComponent newComponent) {
		super.setSubComponent(newComponent);

		if (getReference() != null && FacadeProvider.getEventFacade().isDeleted(getReference().getUuid())) {
			newComponent.setEnabled(false);
		}
	}

	public void setEventEditPermission(Component component) {

		Boolean isEventEditAllowed = isEventEditAllowed();

		if (!isEventEditAllowed) {
			component.setEnabled(false);
		}
	}

	protected Boolean isEventEditAllowed() {
		return FacadeProvider.getEventFacade().isEventEditAllowed(getEventRef().getUuid());
	}

	public EventReferenceDto getEventRef() {
		return getReference();
	}
}
