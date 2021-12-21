package de.symeda.sormas.ui.contact;

import java.util.List;

import com.vaadin.navigator.View;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.contact.ContactCriteria;
import de.symeda.sormas.api.contact.ContactIndexDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.utils.ViewConfiguration;

@SuppressWarnings("serial")
public class ContactGrid extends AbstractContactGrid<ContactIndexDto> {

	public ContactGrid(ContactCriteria criteria, Class<? extends View> viewClass, Class<? extends ViewConfiguration> viewConfigurationClass) {
		super(ContactIndexDto.class, criteria, viewClass, viewConfigurationClass);
	}

	protected List<ContactIndexDto> getGridData(ContactCriteria contactCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {

		return FacadeProvider.getContactFacade().getIndexList(contactCriteria, first, max, sortProperties);
	}
}
