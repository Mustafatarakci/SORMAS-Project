package de.symeda.sormas.api.caze;

import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.statistics.StatisticsCaseAttribute;
import de.symeda.sormas.api.statistics.StatisticsCaseCountDto;
import de.symeda.sormas.api.statistics.StatisticsCaseCriteria;
import de.symeda.sormas.api.statistics.StatisticsCaseSubAttribute;

@Remote
public interface CaseStatisticsFacade {

	List<StatisticsCaseCountDto> queryCaseCount(
		StatisticsCaseCriteria caseCriteria,
		StatisticsCaseAttribute groupingA,
		StatisticsCaseSubAttribute subGroupingA,
		StatisticsCaseAttribute groupingB,
		StatisticsCaseSubAttribute subGroupingB,
		boolean includePopulation,
		boolean includeZeroValues,
		Integer populationReferenceYear);
}
