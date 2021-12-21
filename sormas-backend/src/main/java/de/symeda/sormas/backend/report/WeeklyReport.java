package de.symeda.sormas.backend.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.user.User;

@Entity(name = "weeklyreport")
public class WeeklyReport extends AbstractDomainObject {

	private static final long serialVersionUID = 2192478891179257201L;

	public static final String REPORTING_USER = "reportingUser";
	public static final String REPORT_DATE_TIME = "reportDateTime";
	public static final String DISTRICT = "district";
	public static final String COMMUNITY = "community";
	public static final String HEALTH_FACILITY = "healthFacility";
	public static final String ASSIGNED_OFFICER = "assignedOfficer";
	public static final String TOTAL_NUMBER_OF_CASES = "totalNumberOfCases";
	public static final String YEAR = "year";
	public static final String EPI_WEEK = "epiWeek";
	public static final String REPORT_ENTRIES = "reportEntries";

	private User reportingUser;
	private Date reportDateTime;
	private District district;
	private Community community;
	private Facility healthFacility;
	private User assignedOfficer;
	private Integer totalNumberOfCases;
	private Integer year;
	private Integer epiWeek;

	private Date changeDateOfEmbeddedLists;
	private List<WeeklyReportEntry> reportEntries = new ArrayList<>();

	@ManyToOne(cascade = {})
	public Facility getHealthFacility() {
		return healthFacility;
	}

	public void setHealthFacility(Facility healthFacility) {
		this.healthFacility = healthFacility;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(nullable = false)
	public User getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(User reportingUser) {
		this.reportingUser = reportingUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	@Column(nullable = false)
	public Integer getTotalNumberOfCases() {
		return totalNumberOfCases;
	}

	public void setTotalNumberOfCases(Integer totalNumberOfCases) {
		this.totalNumberOfCases = totalNumberOfCases;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = WeeklyReportEntry.WEEKLY_REPORT)
	public List<WeeklyReportEntry> getReportEntries() {
		return reportEntries;
	}

	public void setReportEntries(List<WeeklyReportEntry> reportEntries) {
		this.reportEntries = reportEntries;
	}

	/**
	 * This change date has to be set whenever one of the embedded lists is
	 * modified: !oldList.equals(newList)
	 * 
	 * @return
	 */
	public Date getChangeDateOfEmbeddedLists() {
		return changeDateOfEmbeddedLists;
	}

	public void setChangeDateOfEmbeddedLists(Date changeDateOfEmbeddedLists) {
		this.changeDateOfEmbeddedLists = changeDateOfEmbeddedLists;
	}

	@Column(nullable = false)
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(nullable = false)
	public Integer getEpiWeek() {
		return epiWeek;
	}

	public void setEpiWeek(Integer epiWeek) {
		this.epiWeek = epiWeek;
	}

	@ManyToOne(cascade = {})
	@JoinColumn
	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	@ManyToOne(cascade = {})
	@JoinColumn
	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	@ManyToOne(cascade = {})
	@JoinColumn
	public User getAssignedOfficer() {
		return assignedOfficer;
	}

	public void setAssignedOfficer(User assignedOfficer) {
		this.assignedOfficer = assignedOfficer;
	}
}
