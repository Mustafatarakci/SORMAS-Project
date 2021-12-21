package de.symeda.sormas.api.dashboard;

import java.io.Serializable;
import java.util.Date;

public class DashboardQuarantineDataDto implements Serializable {

	private long id;
	private Date quarantineFrom;
	private Date quarantineTo;

	public DashboardQuarantineDataDto(long id, Date quarantinefrom, Date quarantineTo) {
		this.id = id;
		this.quarantineFrom = quarantinefrom;
		this.quarantineTo = quarantineTo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getQuarantineFrom() {
		return quarantineFrom;
	}

	public void setQuarantineFrom(Date quarantineFrom) {
		this.quarantineFrom = quarantineFrom;
	}

	public Date getQuarantineTo() {
		return quarantineTo;
	}

	public void setQuarantineTo(Date quarantineTo) {
		this.quarantineTo = quarantineTo;
	}
}
