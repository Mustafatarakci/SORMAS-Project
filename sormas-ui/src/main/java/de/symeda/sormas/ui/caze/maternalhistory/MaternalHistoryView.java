package de.symeda.sormas.ui.caze.maternalhistory;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

@SuppressWarnings("serial")
public class MaternalHistoryView extends AbstractCaseView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/maternalhistory";

	public MaternalHistoryView() {
		super(VIEW_NAME, true);
	}

	@Override
	protected void initView(String params) {

		CommitDiscardWrapperComponent<MaternalHistoryForm> maternalHistoryComponent =
			ControllerProvider.getCaseController().getMaternalHistoryComponent(getCaseRef().getUuid(), getViewMode());
		setSubComponent(maternalHistoryComponent);

		setCaseEditPermission(maternalHistoryComponent);
	}
}
