package de.symeda.sormas.api.event;

import java.io.Serializable;
import java.util.Date;

public class EventGroupIndexDto implements Serializable {

	private static final long serialVersionUID = 8322646404033924938L;

	public static final String I18N_PREFIX = "EventGroup";

	public static final String UUID = "uuid";
	public static final String NAME = "name";
	public static final String EVENT_COUNT = "eventCount";
	public static final String CHANGED_DATE = "changeDate";

	private String uuid;
	private String name;
	private Long eventCount;
	private Date changeDate;

	public EventGroupIndexDto(String uuid, String name, Date changeDate, Long eventCount) {
		this.uuid = uuid;
		this.name = name;
		this.changeDate = changeDate;
		this.eventCount = eventCount;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public Long getEventCount() {
		return eventCount;
	}

	public void setEventCount(Long eventCount) {
		this.eventCount = eventCount;
	}

	public EventGroupReferenceDto toReference() {
		return new EventGroupReferenceDto(getUuid(), getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		EventGroupIndexDto that = (EventGroupIndexDto) o;

		return uuid.equals(that.uuid);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
}
