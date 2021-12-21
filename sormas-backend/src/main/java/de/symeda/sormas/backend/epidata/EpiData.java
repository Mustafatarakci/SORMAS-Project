package de.symeda.sormas.backend.epidata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import de.symeda.auditlog.api.Audited;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.exposure.Exposure;

@Entity
@Audited
public class EpiData extends AbstractDomainObject {

	private static final long serialVersionUID = -8294812479501735785L;

	public static final String TABLE_NAME = "epidata";

	public static final String CONTACT_WITH_SOURCE_CASE_KNOWN = "contactWithSourceCaseKnown";
	public static final String EXPOSURES = "exposures";
	public static final String ACTIVITIES_AS_CASE = "activitiesAsCase";

	private YesNoUnknown exposureDetailsKnown;
	private YesNoUnknown activityAsCaseDetailsKnown;
	private YesNoUnknown contactWithSourceCaseKnown;
	private YesNoUnknown highTransmissionRiskArea;
	private YesNoUnknown largeOutbreaksArea;
	private YesNoUnknown areaInfectedAnimals;

	private List<Exposure> exposures = new ArrayList<>();
	private List<ActivityAsCase> activitiesAsCase = new ArrayList<>();
	private Date changeDateOfEmbeddedLists;

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getExposureDetailsKnown() {
		return exposureDetailsKnown;
	}

	public void setExposureDetailsKnown(YesNoUnknown exposureDetailsKnown) {
		this.exposureDetailsKnown = exposureDetailsKnown;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getActivityAsCaseDetailsKnown() {
		return activityAsCaseDetailsKnown;
	}

	public void setActivityAsCaseDetailsKnown(YesNoUnknown activityAsCaseDetailsKnown) {
		this.activityAsCaseDetailsKnown = activityAsCaseDetailsKnown;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = Exposure.EPI_DATA)
	public List<Exposure> getExposures() {
		return exposures;
	}

	public void setExposures(List<Exposure> exposures) {
		this.exposures = exposures;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = Exposure.EPI_DATA)
	public List<ActivityAsCase> getActivitiesAsCase() {
		return activitiesAsCase;
	}

	public void setActivitiesAsCase(List<ActivityAsCase> activitiesAsCase) {
		this.activitiesAsCase = activitiesAsCase;
	}

	/**
	 * This change date has to be set whenever exposures are modified
	 */
	public Date getChangeDateOfEmbeddedLists() {
		return changeDateOfEmbeddedLists;
	}

	public void setChangeDateOfEmbeddedLists(Date changeDateOfEmbeddedLists) {
		this.changeDateOfEmbeddedLists = changeDateOfEmbeddedLists;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getAreaInfectedAnimals() {
		return areaInfectedAnimals;
	}

	public void setAreaInfectedAnimals(YesNoUnknown areaInfectedAnimals) {
		this.areaInfectedAnimals = areaInfectedAnimals;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getHighTransmissionRiskArea() {
		return highTransmissionRiskArea;
	}

	public void setHighTransmissionRiskArea(YesNoUnknown highTransmissionRiskArea) {
		this.highTransmissionRiskArea = highTransmissionRiskArea;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getLargeOutbreaksArea() {
		return largeOutbreaksArea;
	}

	public void setLargeOutbreaksArea(YesNoUnknown largeOutbreaksArea) {
		this.largeOutbreaksArea = largeOutbreaksArea;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getContactWithSourceCaseKnown() {
		return contactWithSourceCaseKnown;
	}

	public void setContactWithSourceCaseKnown(YesNoUnknown contactWithSourceCaseKnown) {
		this.contactWithSourceCaseKnown = contactWithSourceCaseKnown;
	}
}
