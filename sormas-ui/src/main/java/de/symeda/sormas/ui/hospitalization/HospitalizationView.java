package de.symeda.sormas.ui.hospitalization;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

@SuppressWarnings("serial")
public class HospitalizationView extends AbstractCaseView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/hospitalization";

	public HospitalizationView() {
		super(VIEW_NAME, true);
	}

	@Override
	protected void initView(String params) {

		CommitDiscardWrapperComponent<HospitalizationForm> hospitalizationForm =
			ControllerProvider.getCaseController().getHospitalizationComponent(getCaseRef().getUuid(), getViewMode());
		setSubComponent(hospitalizationForm);
		setCaseEditPermission(hospitalizationForm);
	}
}
