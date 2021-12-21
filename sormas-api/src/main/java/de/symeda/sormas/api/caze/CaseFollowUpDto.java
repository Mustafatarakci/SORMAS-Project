package de.symeda.sormas.api.caze;

import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.followup.FollowUpDto;
import de.symeda.sormas.api.person.SymptomJournalStatus;

public class CaseFollowUpDto extends FollowUpDto {

	private static final long serialVersionUID = -7782443664670559221L;

	private Date symptomsOnsetDate;

	private SymptomJournalStatus symptomJournalStatus;
	private Boolean isInJurisdiction;

	//@formatter:off
	public CaseFollowUpDto(String uuid, Date changeDate, String personFirstName, String personLastName,
						   Date reportDate, Date symptomsOnsetDate, Date followUpUntil, SymptomJournalStatus symptomJournalStatus,
						   Disease disease,
						   boolean isInJurisdiction
	) {
	//formatter:on
		super(uuid, personFirstName, personLastName, reportDate, followUpUntil, disease);
		this.symptomsOnsetDate = symptomsOnsetDate;
		this.symptomJournalStatus = symptomJournalStatus;
		this.isInJurisdiction = isInJurisdiction;
	}

	public Boolean getInJurisdiction() {
		return isInJurisdiction;
	}

	public Date getSymptomsOnsetDate() {
		return symptomsOnsetDate;
	}

	public void setSymptomsOnsetDate(Date symptomsOnsetDate) {
		this.symptomsOnsetDate = symptomsOnsetDate;
	}

	public SymptomJournalStatus getSymptomJournalStatus() {
		return symptomJournalStatus;
	}

	public void setSymptomJournalStatus(SymptomJournalStatus symptomJournalStatus) {
		this.symptomJournalStatus = symptomJournalStatus;
	}
}
