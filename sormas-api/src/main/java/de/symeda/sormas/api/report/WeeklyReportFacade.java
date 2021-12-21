package de.symeda.sormas.api.report;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.EpiWeek;

@Remote
public interface WeeklyReportFacade {

	List<WeeklyReportDto> getAllWeeklyReportsAfter(Date date);

	List<WeeklyReportDto> getByUuids(List<String> uuids);

	WeeklyReportDto saveWeeklyReport(@Valid WeeklyReportDto dto);

	List<String> getAllUuids();

	/**
	 * Returns only regions that do have surveillance officers
	 */
	List<WeeklyReportRegionSummaryDto> getSummariesPerRegion(EpiWeek epiWeek);

	List<WeeklyReportOfficerSummaryDto> getSummariesPerOfficer(RegionReferenceDto region, EpiWeek epiWeek);

	WeeklyReportDto getByEpiWeekAndUser(EpiWeek epiWeek, UserReferenceDto userRef);

	WeeklyReportDto getByUuid(String uuid);
}
