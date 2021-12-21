package de.symeda.sormas.ui.caze;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.person.PersonContext;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.person.PersonEditForm;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

@SuppressWarnings("serial")
public class CasePersonView extends AbstractCaseView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/person";

	public CasePersonView() {
		super(VIEW_NAME, true);
	}

	@Override
	protected void initView(String params) {

		CaseDataDto caseData = FacadeProvider.getCaseFacade().getCaseDataByUuid(getCaseRef().getUuid());
		CommitDiscardWrapperComponent<PersonEditForm> personEditComponent = ControllerProvider.getPersonController()
			.getPersonEditComponent(
				PersonContext.CASE,
				caseData.getPerson().getUuid(),
				caseData.getDisease(),
				caseData.getDiseaseDetails(),
				UserRight.CASE_EDIT,
				getViewMode());

		setSubComponent(personEditComponent);
		setCaseEditPermission(personEditComponent);
	}
}
