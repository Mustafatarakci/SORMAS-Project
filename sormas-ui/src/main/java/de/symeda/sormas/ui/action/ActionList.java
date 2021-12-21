package de.symeda.sormas.ui.action;

import java.util.List;

import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.action.ActionContext;
import de.symeda.sormas.api.action.ActionCriteria;
import de.symeda.sormas.api.action.ActionDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.PaginationList;

@SuppressWarnings("serial")
public class ActionList extends PaginationList<ActionDto> {

	private final ActionCriteria actionCriteria;
	private final ActionContext context;

	public ActionList(ActionContext context, ActionCriteria actionCriteria, int maxDisplayedEntries) {

		super(maxDisplayedEntries);
		this.context = context;
		this.actionCriteria = actionCriteria;
	}

	@Override
	public void reload() {
		List<ActionDto> actions = FacadeProvider.getActionFacade().getActionList(actionCriteria, null, null);

		setEntries(actions);
		showPage(1);
		if (actions.isEmpty()) {
			Label noActionsLabel = new Label(String.format(I18nProperties.getCaption(Captions.actionNoActions), context.toString()));
			listLayout.addComponent(noActionsLabel);
		}
	}

	@Override
	protected void drawDisplayedEntries() {

		List<ActionDto> displayedEntries = getDisplayedEntries();

		for (int i = 0, displayedEntriesSize = displayedEntries.size(); i < displayedEntriesSize; i++) {
			ActionDto action = displayedEntries.get(i);
			ActionListEntry listEntry = new ActionListEntry(action);
			if (UserProvider.getCurrent().hasUserRight(UserRight.ACTION_EDIT)) {
				listEntry.addEditListener(
					i,
					(ClickListener) event -> ControllerProvider.getActionController().edit(listEntry.getAction(), ActionList.this::reload));
			}
			listLayout.addComponent(listEntry);
		}
	}
}
