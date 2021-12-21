package de.symeda.sormas.api.caze;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.PersonalData;
import de.symeda.sormas.api.utils.SensitiveData;
import de.symeda.sormas.api.utils.pseudonymization.Pseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.LatitudePseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.LongitudePseudonymizer;

public class MapCaseDto implements Serializable {

	private static final long serialVersionUID = -3021332968056368431L;

	public static final String I18N_PREFIX = "CaseData";

	private String uuid;
	private Date reportDate;
	private CaseClassification caseClassification;
	private Disease disease;
	private PersonReferenceDto person;
	@PersonalData
	@SensitiveData
	@Pseudonymizer(LatitudePseudonymizer.class)
	private Double healthFacilityLat;
	@PersonalData
	@SensitiveData
	@Pseudonymizer(LongitudePseudonymizer.class)
	private Double healthFacilityLon;
	@SensitiveData
	@Pseudonymizer(LatitudePseudonymizer.class)
	private Double reportLat;
	@SensitiveData
	@Pseudonymizer(LongitudePseudonymizer.class)
	private Double reportLon;
	@PersonalData
	@SensitiveData
	@Pseudonymizer(LatitudePseudonymizer.class)
	private Double addressLat;
	@PersonalData
	@SensitiveData
	@Pseudonymizer(LongitudePseudonymizer.class)
	private Double addressLon;

	private String healthFacilityUuid;

	private Boolean isInJurisdiction;

	public MapCaseDto(
		String uuid,
		Date reportDate,
		CaseClassification caseClassification,
		Disease disease,
		String personUuid,
		String personFirstName,
		String personLastName,
		String healthFacilityUuid,
		Double healthFacilityLat,
		Double healthFacilityLon,
		Double reportLat,
		Double reportLon,
		Double addressLat,
		Double addressLon,
		boolean isInJurisdiction) {

		this.uuid = uuid;
		this.reportDate = reportDate;
		this.caseClassification = caseClassification;
		this.disease = disease;
		this.person = new PersonReferenceDto(personUuid, personFirstName, personLastName);
		this.setHealthFacilityLat(healthFacilityLat);
		this.setHealthFacilityLon(healthFacilityLon);
		this.reportLat = reportLat;
		this.reportLon = reportLon;
		this.addressLat = addressLat;
		this.addressLon = addressLon;
		this.healthFacilityUuid = healthFacilityUuid;
		this.isInJurisdiction = isInJurisdiction;
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

	public String getHealthFacilityUuid() {
		return healthFacilityUuid;
	}

	public PersonReferenceDto getPerson() {
		return person;
	}

	public void setPerson(PersonReferenceDto person) {
		this.person = person;
	}

	public Double getReportLat() {
		return reportLat;
	}

	public void setReportLat(Double reportLat) {
		this.reportLat = reportLat;
	}

	public Double getReportLon() {
		return reportLon;
	}

	public void setReportLon(Double reportLon) {
		this.reportLon = reportLon;
	}

	public Double getAddressLat() {
		return addressLat;
	}

	public void setAddressLat(Double addressLat) {
		this.addressLat = addressLat;
	}

	public Double getAddressLon() {
		return addressLon;
	}

	public void setAddressLon(Double addressLon) {
		this.addressLon = addressLon;
	}

	@Override
	public String toString() {
		return person.toString() + " (" + DataHelper.getShortUuid(uuid) + ")";
	}

	public Double getHealthFacilityLat() {
		return healthFacilityLat;
	}

	public void setHealthFacilityLat(Double healthFacilityLat) {
		this.healthFacilityLat = healthFacilityLat;
	}

	public Double getHealthFacilityLon() {
		return healthFacilityLon;
	}

	public void setHealthFacilityLon(Double healthFacilityLon) {
		this.healthFacilityLon = healthFacilityLon;
	}

	public Boolean getInJurisdiction() {
		return isInJurisdiction;
	}
}
