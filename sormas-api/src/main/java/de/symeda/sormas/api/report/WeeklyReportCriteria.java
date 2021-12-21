package de.symeda.sormas.api.report;

import java.io.Serializable;

import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.EpiWeek;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class WeeklyReportCriteria extends BaseCriteria implements Serializable, Cloneable {

	private static final long serialVersionUID = 5114202107622217837L;

	private EpiWeek epiWeek;
	private UserReferenceDto reportingUser;
	private RegionReferenceDto reportingUserRegion;
	private UserReferenceDto assignedOfficer;
	private Boolean officerReport;
	private Boolean zeroReport;

	@Override
	public WeeklyReportCriteria clone() {
		try {
			return (WeeklyReportCriteria) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public EpiWeek getEpiWeek() {
		return epiWeek;
	}

	public WeeklyReportCriteria epiWeek(EpiWeek epiWeek) {
		this.epiWeek = epiWeek;
		return this;
	}

	public RegionReferenceDto getReportingUserRegion() {
		return reportingUserRegion;
	}

	public WeeklyReportCriteria reportingUserRegion(RegionReferenceDto reportingUserRegion) {
		this.reportingUserRegion = reportingUserRegion;
		return this;
	}

	public UserReferenceDto getAssignedOfficer() {
		return assignedOfficer;
	}

	public WeeklyReportCriteria assignedOfficer(UserReferenceDto assignedOfficer) {
		this.assignedOfficer = assignedOfficer;
		return this;
	}

	public Boolean getOfficerReport() {
		return officerReport;
	}

	public WeeklyReportCriteria officerReport(Boolean officerReport) {
		this.officerReport = officerReport;
		return this;
	}

	public Boolean getZeroReport() {
		return zeroReport;
	}

	public WeeklyReportCriteria zeroReport(Boolean zeroReport) {
		this.zeroReport = zeroReport;
		return this;
	}

	public UserReferenceDto getReportingUser() {
		return reportingUser;
	}

	public WeeklyReportCriteria reportingUser(UserReferenceDto reportingUser) {
		this.reportingUser = reportingUser;
		return this;
	}
}
