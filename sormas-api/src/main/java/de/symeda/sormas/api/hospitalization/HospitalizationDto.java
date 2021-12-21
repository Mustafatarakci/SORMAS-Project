package de.symeda.sormas.api.hospitalization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.FieldConstraints;
import de.symeda.sormas.api.utils.Outbreaks;
import de.symeda.sormas.api.utils.YesNoUnknown;

public class HospitalizationDto extends EntityDto {

	private static final long serialVersionUID = 4846215199480684369L;

	public static final String I18N_PREFIX = "CaseHospitalization";

	public static final String ADMITTED_TO_HEALTH_FACILITY = "admittedToHealthFacility";
	public static final String ADMISSION_DATE = "admissionDate";
	public static final String DISCHARGE_DATE = "dischargeDate";
	public static final String ISOLATED = "isolated";
	public static final String ISOLATION_DATE = "isolationDate";
	public static final String LEFT_AGAINST_ADVICE = "leftAgainstAdvice";
	public static final String HOSPITALIZED_PREVIOUSLY = "hospitalizedPreviously";
	public static final String PREVIOUS_HOSPITALIZATIONS = "previousHospitalizations";
	public static final String INTENSIVE_CARE_UNIT = "intensiveCareUnit";
	public static final String INTENSIVE_CARE_UNIT_START = "intensiveCareUnitStart";
	public static final String INTENSIVE_CARE_UNIT_END = "intensiveCareUnitEnd";
	public static final String HOSPITALIZATION_REASON = "hospitalizationReason";
	public static final String OTHER_HOSPITALIZATION_REASON = "otherHospitalizationReason";
	public static final String DESCRIPTION = "description";

	// Fields are declared in the order they should appear in the import template

	@Outbreaks
	private YesNoUnknown admittedToHealthFacility;
	private Date admissionDate;
	private Date dischargeDate;
	private YesNoUnknown isolated;
	private Date isolationDate;
	private YesNoUnknown leftAgainstAdvice;

	private YesNoUnknown hospitalizedPreviously;
	@Valid
	private List<PreviousHospitalizationDto> previousHospitalizations = new ArrayList<>();
	private YesNoUnknown intensiveCareUnit;
	private Date intensiveCareUnitStart;
	private Date intensiveCareUnitEnd;
	private HospitalizationReasonType hospitalizationReason;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String otherHospitalizationReason;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_BIG, message = Validations.textTooLong)
	private String description;

	public static HospitalizationDto build() {
		HospitalizationDto hospitalization = new HospitalizationDto();
		hospitalization.setUuid(DataHelper.createUuid());
		return hospitalization;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public YesNoUnknown getIsolated() {
		return isolated;
	}

	public void setIsolated(YesNoUnknown isolated) {
		this.isolated = isolated;
	}

	public Date getIsolationDate() {
		return isolationDate;
	}

	public void setIsolationDate(Date isolationDate) {
		this.isolationDate = isolationDate;
	}

	@ImportIgnore
	public YesNoUnknown getHospitalizedPreviously() {
		return hospitalizedPreviously;
	}

	public void setHospitalizedPreviously(YesNoUnknown hospitalizedPreviously) {
		this.hospitalizedPreviously = hospitalizedPreviously;
	}

	@ImportIgnore
	public List<PreviousHospitalizationDto> getPreviousHospitalizations() {
		return previousHospitalizations;
	}

	public void setPreviousHospitalizations(List<PreviousHospitalizationDto> previousHospitalizations) {
		this.previousHospitalizations = previousHospitalizations;
	}

	public YesNoUnknown getAdmittedToHealthFacility() {
		return admittedToHealthFacility;
	}

	public void setAdmittedToHealthFacility(YesNoUnknown admittedToHealthFacility) {
		this.admittedToHealthFacility = admittedToHealthFacility;
	}

	public YesNoUnknown getIntensiveCareUnit() {
		return intensiveCareUnit;
	}

	public void setIntensiveCareUnit(YesNoUnknown intensiveCareUnit) {
		this.intensiveCareUnit = intensiveCareUnit;
	}

	public Date getIntensiveCareUnitStart() {
		return intensiveCareUnitStart;
	}

	public void setIntensiveCareUnitStart(Date intensiveCareUnitStart) {
		this.intensiveCareUnitStart = intensiveCareUnitStart;
	}

	public Date getIntensiveCareUnitEnd() {
		return intensiveCareUnitEnd;
	}

	public void setIntensiveCareUnitEnd(Date intensiveCareUnitEnd) {
		this.intensiveCareUnitEnd = intensiveCareUnitEnd;
	}

	public YesNoUnknown getLeftAgainstAdvice() {
		return leftAgainstAdvice;
	}

	public void setLeftAgainstAdvice(YesNoUnknown leftAgainstAdvice) {
		this.leftAgainstAdvice = leftAgainstAdvice;
	}

	public HospitalizationReasonType getHospitalizationReason() {
		return hospitalizationReason;
	}

	public void setHospitalizationReason(HospitalizationReasonType hospitalizationReason) {
		this.hospitalizationReason = hospitalizationReason;
	}

	public String getOtherHospitalizationReason() {
		return otherHospitalizationReason;
	}

	public void setOtherHospitalizationReason(String otherHospitalizationReason) {
		this.otherHospitalizationReason = otherHospitalizationReason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
