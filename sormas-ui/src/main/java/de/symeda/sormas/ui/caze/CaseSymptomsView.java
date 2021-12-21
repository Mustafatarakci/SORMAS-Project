package de.symeda.sormas.ui.caze;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.symptoms.SymptomsForm;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

public class CaseSymptomsView extends AbstractCaseView {

	private static final long serialVersionUID = -1L;

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/symptoms";

	public CaseSymptomsView() {
		super(VIEW_NAME, true);
	}

	@Override
	protected void initView(String params) {

		CommitDiscardWrapperComponent<SymptomsForm> caseSymptomsComponent =
			ControllerProvider.getCaseController().getSymptomsEditComponent(getCaseRef().getUuid(), getViewMode());
		setSubComponent(caseSymptomsComponent);
		setCaseEditPermission(caseSymptomsComponent);
	}
}
