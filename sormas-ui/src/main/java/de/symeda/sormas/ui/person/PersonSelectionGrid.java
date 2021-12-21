package de.symeda.sormas.ui.person;

import java.util.List;

import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.shared.ui.grid.HeightMode;
import com.vaadin.v7.ui.Grid;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.person.PersonSimilarityCriteria;
import de.symeda.sormas.api.person.SimilarPersonDto;
import de.symeda.sormas.ui.UserProvider;

@SuppressWarnings("serial")
public class PersonSelectionGrid extends Grid {

	/**
	 * Create a grid of persons listing all persons similar to the given criteria.
	 */
	public PersonSelectionGrid() {
		buildGrid();
	}

	private void buildGrid() {
		setSizeFull();
		setSelectionMode(SelectionMode.SINGLE);
		setHeightMode(HeightMode.ROW);

		BeanItemContainer<SimilarPersonDto> container = new BeanItemContainer<>(SimilarPersonDto.class);
		GeneratedPropertyContainer generatedContainer = new GeneratedPropertyContainer(container);
		setContainerDataSource(generatedContainer);

		setColumns(
			SimilarPersonDto.FIRST_NAME,
			SimilarPersonDto.LAST_NAME,
			SimilarPersonDto.NICKNAME,
			SimilarPersonDto.AGE_AND_BIRTH_DATE,
			SimilarPersonDto.SEX,
			SimilarPersonDto.PRESENT_CONDITION,
			SimilarPersonDto.PHONE,
			SimilarPersonDto.DISTRICT_NAME,
			SimilarPersonDto.COMMUNITY_NAME,
			SimilarPersonDto.POSTAL_CODE,
			SimilarPersonDto.CITY,
			SimilarPersonDto.STREET,
			SimilarPersonDto.HOUSE_NUMBER,
			SimilarPersonDto.NATIONAL_HEALTH_ID,
			SimilarPersonDto.PASSPORT_NUMBER);

		for (Column column : getColumns()) {
			String propertyId = column.getPropertyId().toString();
			String i18nPrefix = SimilarPersonDto.getI18nPrefix(propertyId);
			column.setHeaderCaption(I18nProperties.getPrefixCaption(i18nPrefix, propertyId, column.getHeaderCaption()));
		}

		getColumn(SimilarPersonDto.FIRST_NAME).setMinimumWidth(150);
		getColumn(SimilarPersonDto.LAST_NAME).setMinimumWidth(150);
	}

	@SuppressWarnings("unchecked")
	private BeanItemContainer<SimilarPersonDto> getContainer() {
		GeneratedPropertyContainer container = (GeneratedPropertyContainer) super.getContainerDataSource();
		return (BeanItemContainer<SimilarPersonDto>) container.getWrappedContainer();
	}

	/**
	 * Load similar persons for the given criteria.
	 * 
	 * @param criteria
	 *            The person criteria.
	 */
	public void loadData(PersonSimilarityCriteria criteria) {
		List<SimilarPersonDto> similarPersons =
			FacadeProvider.getPersonFacade().getSimilarPersonDtos(UserProvider.getCurrent().getUserReference(), criteria);

		getContainer().removeAllItems();
		getContainer().addAll(similarPersons);
		setHeightByRows(similarPersons.size() > 0 ? (Math.min(similarPersons.size(), 10)) : 1);
	}
}
