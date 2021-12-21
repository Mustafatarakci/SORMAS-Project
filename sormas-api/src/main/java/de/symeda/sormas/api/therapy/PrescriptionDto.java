package de.symeda.sormas.api.therapy;

import java.util.Date;

import javax.validation.constraints.Size;

import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.FieldConstraints;
import de.symeda.sormas.api.utils.SensitiveData;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

public class PrescriptionDto extends PseudonymizableDto {

	private static final long serialVersionUID = -5028702472324192079L;

	public static final String I18N_PREFIX = "Prescription";

	public static final String PRESCRIPTION_DATE = "prescriptionDate";
	public static final String PRESCRIPTION_START = "prescriptionStart";
	public static final String PRESCRIPTION_END = "prescriptionEnd";
	public static final String PRESCRIBING_CLINICIAN = "prescribingClinician";
	public static final String PRESCRIPTION_TYPE = "prescriptionType";
	public static final String PRESCRIPTION_DETAILS = "prescriptionDetails";
	public static final String DRUG_INTAKE_DETAILS = "drugIntakeDetails";
	public static final String TYPE_OF_DRUG = "typeOfDrug";
	public static final String FREQUENCY = "frequency";
	public static final String DOSE = "dose";
	public static final String ROUTE = "route";
	public static final String ROUTE_DETAILS = "routeDetails";
	public static final String ADDITIONAL_NOTES = "additionalNotes";

	private TherapyReferenceDto therapy;
	private Date prescriptionDate;
	private Date prescriptionStart;
	private Date prescriptionEnd;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String prescribingClinician;
	private TreatmentType prescriptionType;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String prescriptionDetails;
	private TypeOfDrug typeOfDrug;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String frequency;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String dose;
	private TreatmentRoute route;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String routeDetails;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_BIG, message = Validations.textTooLong)
	private String additionalNotes;

	public static PrescriptionDto buildPrescription(TherapyReferenceDto therapy) {
		PrescriptionDto prescription = new PrescriptionDto();
		prescription.setUuid(DataHelper.createUuid());
		prescription.setTherapy(therapy);
		prescription.setPrescriptionDate(new Date());

		return prescription;
	}

	public PrescriptionReferenceDto toReference() {
		return new PrescriptionReferenceDto(getUuid(), toString());
	}

	public TherapyReferenceDto getTherapy() {
		return therapy;
	}

	public void setTherapy(TherapyReferenceDto therapy) {
		this.therapy = therapy;
	}

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Date getPrescriptionStart() {
		return prescriptionStart;
	}

	public void setPrescriptionStart(Date prescriptionStart) {
		this.prescriptionStart = prescriptionStart;
	}

	public Date getPrescriptionEnd() {
		return prescriptionEnd;
	}

	public void setPrescriptionEnd(Date prescriptionEnd) {
		this.prescriptionEnd = prescriptionEnd;
	}

	public String getPrescribingClinician() {
		return prescribingClinician;
	}

	public void setPrescribingClinician(String prescribingClinician) {
		this.prescribingClinician = prescribingClinician;
	}

	public TreatmentType getPrescriptionType() {
		return prescriptionType;
	}

	public void setPrescriptionType(TreatmentType prescriptionType) {
		this.prescriptionType = prescriptionType;
	}

	public String getPrescriptionDetails() {
		return prescriptionDetails;
	}

	public void setPrescriptionDetails(String prescriptionDetails) {
		this.prescriptionDetails = prescriptionDetails;
	}

	public TypeOfDrug getTypeOfDrug() {
		return typeOfDrug;
	}

	public void setTypeOfDrug(TypeOfDrug typeOfDrug) {
		this.typeOfDrug = typeOfDrug;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public TreatmentRoute getRoute() {
		return route;
	}

	public void setRoute(TreatmentRoute route) {
		this.route = route;
	}

	public String getRouteDetails() {
		return routeDetails;
	}

	public void setRouteDetails(String routeDetails) {
		this.routeDetails = routeDetails;
	}

	public String getAdditionalNotes() {
		return additionalNotes;
	}

	public void setAdditionalNotes(String additionalNotes) {
		this.additionalNotes = additionalNotes;
	}
}
