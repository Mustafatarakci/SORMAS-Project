package de.symeda.sormas.ui.action;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.action.ActionContext;
import de.symeda.sormas.api.action.ActionCriteria;
import de.symeda.sormas.api.action.ActionStatEntry;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class ActionStatsComponent extends VerticalLayout {

	private final ActionCriteria actionCriteria;
	private final ActionContext context;
	private Button createButton;
	private VerticalLayout listLayout;

	public ActionStatsComponent(ActionContext context, EventReferenceDto entityRef) {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
		setSpacing(false);

		this.context = context;
		this.actionCriteria = new ActionCriteria().event(entityRef);

		HorizontalLayout componentHeader = new HorizontalLayout();
		componentHeader.setMargin(false);
		componentHeader.setSpacing(false);
		componentHeader.setWidth(100, Unit.PERCENTAGE);
		addComponent(componentHeader);

		listLayout = new VerticalLayout();
		listLayout.setSpacing(true);
		listLayout.setMargin(false);
		addComponent(listLayout);
		reload();

		Label actionsHeader = new Label(I18nProperties.getString(Strings.entityActions));
		actionsHeader.addStyleName(CssStyles.H3);
		componentHeader.addComponent(actionsHeader);

		if (UserProvider.getCurrent().hasUserRight(UserRight.ACTION_CREATE)) {
			createButton = ButtonHelper.createIconButton(
				Captions.actionNewAction,
				VaadinIcons.PLUS_CIRCLE,
				e -> ControllerProvider.getActionController().create(context, entityRef, this::reload),
				ValoTheme.BUTTON_PRIMARY);

			componentHeader.addComponent(createButton);
			componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
		}
	}

	private void reload() {
		List<ActionStatEntry> stats = FacadeProvider.getActionFacade().getActionStats(actionCriteria);
		listLayout.removeAllComponents();
		if (stats.isEmpty()) {
			Label noActionsLabel = new Label(String.format(I18nProperties.getCaption(Captions.actionNoActions), context.toString()));
			setSpacing(false);
			listLayout.addComponent(noActionsLabel);
		} else {
			setSpacing(true);
			listLayout.addComponents(stats.stream().map(this::toComponent).collect(Collectors.toList()).toArray(new Component[0]));
		}
	}

	private Component toComponent(ActionStatEntry stat) {
		HorizontalLayout res = new HorizontalLayout();
		res.setSpacing(false);
		res.setMargin(false);
		res.setWidth(100, Unit.PERCENTAGE);

		Label statusLabel = new Label(DataHelper.toStringNullable(stat.getActionStatus()));
		res.addComponent(statusLabel);
		res.setExpandRatio(statusLabel, 1);
		CssStyles.style(statusLabel, CssStyles.LABEL_BOLD, CssStyles.LABEL_UPPERCASE);
		Label countLabel = new Label();
		if (stat.getCount() == null) {
			countLabel.setValue("-");
		} else {
			countLabel.setValue(String.valueOf(stat.getCount()));
			countLabel.addStyleNames(CssStyles.BADGE, CssStyles.LABEL_LARGE);
		}
		countLabel.addStyleName(CssStyles.ALIGN_RIGHT);
		res.addComponent(countLabel);
		res.setExpandRatio(countLabel, 0);

		return res;
	}
}
