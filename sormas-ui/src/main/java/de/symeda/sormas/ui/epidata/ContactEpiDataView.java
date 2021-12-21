package de.symeda.sormas.ui.epidata;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.contact.AbstractContactView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

@SuppressWarnings("serial")
public class ContactEpiDataView extends AbstractContactView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/epidata";

	public ContactEpiDataView() {
		super(VIEW_NAME);
	}

	@Override
	protected void initView(String params) {
		CommitDiscardWrapperComponent<EpiDataForm> epidDataForm =
			ControllerProvider.getContactController().getEpiDataComponent(getContactRef().getUuid());
		setSubComponent(epidDataForm);
		setContactEditPermission(epidDataForm);
	}
}
