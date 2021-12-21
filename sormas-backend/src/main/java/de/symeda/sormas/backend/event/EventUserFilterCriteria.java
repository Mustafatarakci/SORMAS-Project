package de.symeda.sormas.backend.event;

public class EventUserFilterCriteria {

	private boolean includeUserCaseAndEventParticipantFilter;
	private boolean forceRegionJurisdiction;

	public boolean isIncludeUserCaseAndEventParticipantFilter() {
		return includeUserCaseAndEventParticipantFilter;
	}

	public EventUserFilterCriteria includeUserCaseAndEventParticipantFilter(boolean includeUserCaseAndEventParticipantFilter) {
		this.includeUserCaseAndEventParticipantFilter = includeUserCaseAndEventParticipantFilter;
		return this;
	}

	public boolean isForceRegionJurisdiction() {
		return forceRegionJurisdiction;
	}

	public EventUserFilterCriteria forceRegionJurisdiction(boolean forceRegionJurisdiction) {
		this.forceRegionJurisdiction = forceRegionJurisdiction;
		return this;
	}
}
