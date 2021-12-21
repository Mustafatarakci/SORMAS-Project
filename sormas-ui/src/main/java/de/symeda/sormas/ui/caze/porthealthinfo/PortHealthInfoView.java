package de.symeda.sormas.ui.caze.porthealthinfo;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

@SuppressWarnings("serial")
public class PortHealthInfoView extends AbstractCaseView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/porthealthinfo";

	public PortHealthInfoView() {
		super(VIEW_NAME, false);
	}

	@Override
	protected void initView(String params) {

		CommitDiscardWrapperComponent<PortHealthInfoForm> portHealthInfoComponent =
			ControllerProvider.getCaseController().getPortHealthInfoComponent(getCaseRef().getUuid());
		setSubComponent(portHealthInfoComponent);

		setCaseEditPermission(portHealthInfoComponent);
	}
}
