package de.symeda.sormas.ui.events.groups;

import java.util.stream.Collectors;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventGroupCriteria;
import de.symeda.sormas.api.event.EventGroupIndexDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.utils.FilteredGrid;

@SuppressWarnings("serial")
public class EventGroupSelectionGrid extends FilteredGrid<EventGroupIndexDto, EventGroupCriteria> {

	public EventGroupSelectionGrid(EventGroupCriteria criteria) {
		super(EventGroupIndexDto.class);

		setLazyDataProvider();
		setCriteria(criteria);
		buildGrid();
	}

	private void buildGrid() {
		setSizeFull();
		setSelectionMode(SelectionMode.SINGLE);
		setHeightMode(HeightMode.ROW);

		setColumns(
			EventGroupIndexDto.UUID,
			EventGroupIndexDto.NAME,
			EventGroupIndexDto.EVENT_COUNT);

		for (Column<EventGroupIndexDto, ?> column : getColumns()) {
			column.setCaption(I18nProperties.getPrefixCaption(EventGroupIndexDto.I18N_PREFIX, column.getId(), column.getCaption()));
		}
		getColumn(EventGroupIndexDto.EVENT_COUNT).setSortable(false);

		getColumn(EventGroupIndexDto.NAME).setMaximumWidth(300);
	}

	public void setLazyDataProvider() {

		DataProvider<EventGroupIndexDto, EventGroupCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
			query -> FacadeProvider.getEventGroupFacade()
				.getIndexList(
					query.getFilter().orElse(null),
					query.getOffset(),
					query.getLimit(),
					query.getSortOrders()
						.stream()
						.map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
						.collect(Collectors.toList()))
				.stream(),
			query -> (int) FacadeProvider.getEventGroupFacade().count(query.getFilter().orElse(null)));
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.NONE);

		EventGroupSelectionGrid tempGrid = this;
		dataProvider.addDataProviderListener((DataProviderListener<EventGroupIndexDto>) dataChangeEvent -> {
			if (tempGrid.getItemCount() > 0) {
				tempGrid.setHeightByRows(Math.min(tempGrid.getItemCount(), 5));
			} else {
				tempGrid.setHeightByRows(1);
			}
		});
	}

	public void setCriteria(EventGroupCriteria criteria) {
		getFilteredDataProvider().setFilter(criteria);
	}

	@SuppressWarnings("unchecked")
	public ConfigurableFilterDataProvider<EventGroupIndexDto, Void, EventGroupCriteria> getFilteredDataProvider() {
		return (ConfigurableFilterDataProvider<EventGroupIndexDto, Void, EventGroupCriteria>) super.getDataProvider();
	}
}
