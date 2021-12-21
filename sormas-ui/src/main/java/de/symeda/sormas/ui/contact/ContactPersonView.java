package de.symeda.sormas.ui.contact;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.person.PersonContext;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.person.PersonEditForm;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

public class ContactPersonView extends AbstractContactView {

	private static final long serialVersionUID = -1L;

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/person";

	public ContactPersonView() {
		super(VIEW_NAME);
	}

	@Override
	protected void initView(String params) {

		ContactDto dto = FacadeProvider.getContactFacade().getContactByUuid(getContactRef().getUuid());

		CommitDiscardWrapperComponent<PersonEditForm> contactPersonComponent = ControllerProvider.getPersonController()
			.getPersonEditComponent(PersonContext.CONTACT,dto.getPerson().getUuid(), dto.getDisease(), dto.getDiseaseDetails(), UserRight.CONTACT_EDIT, null);
		setSubComponent(contactPersonComponent);

		setContactEditPermission(contactPersonComponent);
	}
}
