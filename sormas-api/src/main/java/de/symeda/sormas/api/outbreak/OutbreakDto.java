package de.symeda.sormas.api.outbreak;

import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;

public class OutbreakDto extends EntityDto {

	private static final long serialVersionUID = -1947258237240456473L;

	public static final String I18N_PREFIX = "Outbreak";

	private DistrictReferenceDto district;
	private Disease disease;
	private Date startDate;
	private Date endDate;

	private UserReferenceDto reportingUser;
	private Date reportDate;

	public static OutbreakDto build(DistrictReferenceDto district, Disease disease, UserReferenceDto reportingUser) {

		OutbreakDto outbreak = new OutbreakDto();
		outbreak.setUuid(DataHelper.createUuid());
		outbreak.setDistrict(district);
		outbreak.setDisease(disease);
		outbreak.setReportingUser(reportingUser);
		outbreak.setReportDate(new Date());
		return outbreak;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public UserReferenceDto getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(UserReferenceDto reportingUser) {
		this.reportingUser = reportingUser;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Expected to be in the past
	 */
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
