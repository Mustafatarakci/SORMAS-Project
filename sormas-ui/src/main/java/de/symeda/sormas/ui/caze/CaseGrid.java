package de.symeda.sormas.ui.caze;

import java.util.List;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseIndexDto;
import de.symeda.sormas.api.utils.SortProperty;

@SuppressWarnings("serial")
public class CaseGrid extends AbstractCaseGrid<CaseIndexDto> {

	public CaseGrid(CaseCriteria criteria) {
		super(CaseIndexDto.class, criteria);
	}

	@Override
	protected List<CaseIndexDto> getGridData(CaseCriteria caseCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		return FacadeProvider.getCaseFacade().getIndexList(caseCriteria, first, max, sortProperties);
	}
}
