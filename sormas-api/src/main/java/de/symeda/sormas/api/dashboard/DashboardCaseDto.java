package de.symeda.sormas.api.dashboard;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseReferenceDefinition;
import de.symeda.sormas.api.person.PresentCondition;

public class DashboardCaseDto implements Serializable {

	private static final long serialVersionUID = -5705128377788207648L;

	public static final String I18N_PREFIX = "CaseData";

	private long id;
	private String uuid;
	private Date reportDate;
	private CaseClassification caseClassification;
	private Disease disease;
	private PresentCondition casePersonCondition;
	private Disease causeOfDeathDisease;
	private CaseReferenceDefinition caseReferenceDefinition;

	private DashboardQuarantineDataDto dashboardQuarantineDataDto;

	public DashboardCaseDto(
		long id,
		String uuid,
		Date reportDate,
		CaseClassification caseClassification,
		Disease disease,
		PresentCondition casePersonCondition,
		Disease causeOfDeathDisease,
		Date quarantineFrom,
		Date quarantineTo,
		CaseReferenceDefinition caseReferenceDefinition) {

		this.id = id;
		this.uuid = uuid;
		this.reportDate = reportDate;
		this.caseClassification = caseClassification;
		this.disease = disease;
		this.casePersonCondition = casePersonCondition;
		this.causeOfDeathDisease = causeOfDeathDisease;
		this.dashboardQuarantineDataDto = new DashboardQuarantineDataDto(id, quarantineFrom, quarantineTo);
		this.caseReferenceDefinition = caseReferenceDefinition;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public CaseClassification getCaseClassification() {
		return caseClassification;
	}

	public void setCaseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public PresentCondition getCasePersonCondition() {
		return casePersonCondition;
	}

	public void setCasePersonCondition(PresentCondition casePersonCondition) {
		this.casePersonCondition = casePersonCondition;
	}

	public Disease getCauseOfDeathDisease() {
		return causeOfDeathDisease;
	}

	public void setCauseOfDeathDisease(Disease causeOfDeathDisease) {
		this.causeOfDeathDisease = causeOfDeathDisease;
	}

	public Boolean wasFatal() {
		return getCasePersonCondition() != null && getCasePersonCondition() != PresentCondition.ALIVE && getCauseOfDeathDisease() == getDisease();
	}

	public DashboardQuarantineDataDto getDashboardQuarantineDataDto() {
		return dashboardQuarantineDataDto;
	}

	public CaseReferenceDefinition getCaseReferenceDefinition() {
		return caseReferenceDefinition;
	}

	public void setCaseReferenceDefinition(CaseReferenceDefinition caseReferenceDefinition) {
		this.caseReferenceDefinition = caseReferenceDefinition;
	}
}
