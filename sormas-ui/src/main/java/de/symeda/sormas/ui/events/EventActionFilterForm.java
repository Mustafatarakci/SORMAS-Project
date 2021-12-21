package de.symeda.sormas.ui.events;

import de.symeda.sormas.api.action.ActionCriteria;
import de.symeda.sormas.api.action.ActionDto;
import de.symeda.sormas.ui.utils.AbstractFilterForm;
import de.symeda.sormas.ui.utils.FieldConfiguration;

/**
 * Form for filtering actions related to an event.
 */
public class EventActionFilterForm extends AbstractFilterForm<ActionCriteria> {

	private static final long serialVersionUID = -8661345403078183132L;

	protected EventActionFilterForm() {
		super(ActionCriteria.class, ActionDto.I18N_PREFIX);
	}

	@Override
	protected String[] getMainFilterLocators() {
		return new String[] {
			ActionDto.ACTION_STATUS };
	}

	@Override
	protected void addFields() {
		addField(FieldConfiguration.pixelSized(ActionDto.ACTION_STATUS, 200));
	}
}
