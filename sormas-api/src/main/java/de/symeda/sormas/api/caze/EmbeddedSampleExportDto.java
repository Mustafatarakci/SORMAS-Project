package de.symeda.sormas.api.caze;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.SensitiveData;

public class EmbeddedSampleExportDto implements Serializable {

	private String uuid;
	private Date dateTime;
	@SensitiveData
	private String lab;
	private PathogenTestResultType result;

	public EmbeddedSampleExportDto() {
	}

	public EmbeddedSampleExportDto(String uuid, Date dateTime, String lab, PathogenTestResultType result) {
		this.uuid = uuid;
		this.dateTime = dateTime;
		this.lab = lab;
		this.result = result;
	}

	public String formatString() {
		StringBuilder sb = new StringBuilder();

		sb.append(DateHelper.formatDateForExport(dateTime)).append(" (");
		if (lab != null && lab.length() > 0) {
			sb.append(lab).append(", ");
		}
		sb.append(result).append(")");

		return sb.toString();
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getLab() {
		return lab;
	}

	public void setLab(String lab) {
		this.lab = lab;
	}

	public PathogenTestResultType getResult() {
		return result;
	}

	public void setResult(PathogenTestResultType result) {
		this.result = result;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
