package de.symeda.sormas.ui.configuration.infrastructure;

import java.util.stream.Collectors;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.infrastructure.facility.FacilityCriteria;
import de.symeda.sormas.api.infrastructure.facility.FacilityIndexDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.ViewConfiguration;

public class FacilitiesGrid extends FilteredGrid<FacilityIndexDto, FacilityCriteria> {

	private static final long serialVersionUID = 4488941182432777837L;

	public FacilitiesGrid(FacilityCriteria criteria) {

		super(FacilityIndexDto.class);
		setSizeFull();

		ViewConfiguration viewConfiguration = ViewModelProviders.of(FacilitiesView.class).get(ViewConfiguration.class);
		setInEagerMode(viewConfiguration.isInEagerMode());

		if (isInEagerMode() && UserProvider.getCurrent().hasUserRight(UserRight.PERFORM_BULK_OPERATIONS)) {
			setCriteria(criteria);
			setEagerDataProvider();
		} else {
			setLazyDataProvider();
			setCriteria(criteria);
		}

		setColumns(
			FacilityIndexDto.NAME,
			FacilityIndexDto.TYPE,
			FacilityIndexDto.REGION,
			FacilityIndexDto.DISTRICT,
			FacilityIndexDto.COMMUNITY,
			FacilityIndexDto.CITY,
			FacilityIndexDto.LATITUDE,
			FacilityIndexDto.LONGITUDE,
			FacilityIndexDto.EXTERNAL_ID);

		if (UserProvider.getCurrent().hasUserRight(UserRight.INFRASTRUCTURE_EDIT)) {
			addEditColumn(e -> ControllerProvider.getInfrastructureController().editFacility(e.getUuid()));
		}

		for (Column<?, ?> column : getColumns()) {
			column.setCaption(I18nProperties.getPrefixCaption(FacilityIndexDto.I18N_PREFIX, column.getId(), column.getCaption()));
		}
	}

	public void reload() {
		getDataProvider().refreshAll();
	}

	public void setLazyDataProvider() {

		DataProvider<FacilityIndexDto, FacilityCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
			query -> FacadeProvider.getFacilityFacade()
				.getIndexList(
					query.getFilter().orElse(null),
					query.getOffset(),
					query.getLimit(),
					query.getSortOrders()
						.stream()
						.map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
						.collect(Collectors.toList()))
				.stream(),
			query -> (int) FacadeProvider.getFacilityFacade().count(query.getFilter().orElse(null)));
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.NONE);
	}

	public void setEagerDataProvider() {

		ListDataProvider<FacilityIndexDto> dataProvider =
			DataProvider.fromStream(FacadeProvider.getFacilityFacade().getIndexList(getCriteria(), null, null, null).stream());
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.MULTI);
	}
}
