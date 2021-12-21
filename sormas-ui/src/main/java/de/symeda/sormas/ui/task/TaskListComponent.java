package de.symeda.sormas.ui.task;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.task.TaskContext;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class TaskListComponent extends VerticalLayout {

	private TaskList list;
	private Button createButton;

	public TaskListComponent(TaskContext context, ReferenceDto entityRef, Disease disease) {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
		setSpacing(false);

		HorizontalLayout componentHeader = new HorizontalLayout();
		componentHeader.setMargin(false);
		componentHeader.setSpacing(false);
		componentHeader.setWidth(100, Unit.PERCENTAGE);
		addComponent(componentHeader);

		list = new TaskList(context, entityRef);
		addComponent(list);
		list.reload();

		Label tasksHeader = new Label(I18nProperties.getString(Strings.entityTasks));
		tasksHeader.addStyleName(CssStyles.H3);
		componentHeader.addComponent(tasksHeader);

		if (UserProvider.getCurrent().hasUserRight(UserRight.TASK_CREATE)) {
			createButton = ButtonHelper.createIconButton(
				Captions.taskNewTask,
				VaadinIcons.PLUS_CIRCLE,
				e -> ControllerProvider.getTaskController().create(context, entityRef, disease, this::reload),
				ValoTheme.BUTTON_PRIMARY);

			componentHeader.addComponent(createButton);
			componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
		}
	}

	public void reload() {
		list.reload();
	}
}
