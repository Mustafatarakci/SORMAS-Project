/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.symeda.sormas.ui;

import de.symeda.sormas.api.ConfigFacade;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.campaign.CampaignFacade;
import de.symeda.sormas.api.campaign.data.CampaignFormDataFacade;
import de.symeda.sormas.api.campaign.form.CampaignFormMetaFacade;
import de.symeda.sormas.api.caze.CaseFacade;
import de.symeda.sormas.api.caze.caseimport.CaseImportFacade;
import de.symeda.sormas.api.caze.classification.CaseClassificationFacade;
import de.symeda.sormas.api.contact.ContactFacade;
import de.symeda.sormas.api.document.DocumentFacade;
import de.symeda.sormas.api.epidata.EpiDataFacade;
import de.symeda.sormas.api.event.EventFacade;
import de.symeda.sormas.api.event.EventParticipantFacade;
import de.symeda.sormas.api.event.eventimport.EventImportFacade;
import de.symeda.sormas.api.externalmessage.ExternalMessageFacade;
import de.symeda.sormas.api.feature.FeatureConfigurationFacade;
import de.symeda.sormas.api.geo.GeoShapeProvider;
import de.symeda.sormas.api.hospitalization.HospitalizationFacade;
import de.symeda.sormas.api.immunization.ImmunizationFacade;
import de.symeda.sormas.api.importexport.ExportFacade;
import de.symeda.sormas.api.importexport.ImportFacade;
import de.symeda.sormas.api.infrastructure.community.CommunityFacade;
import de.symeda.sormas.api.infrastructure.country.CountryFacade;
import de.symeda.sormas.api.infrastructure.district.DistrictFacade;
import de.symeda.sormas.api.infrastructure.facility.FacilityFacade;
import de.symeda.sormas.api.infrastructure.pointofentry.PointOfEntryFacade;
import de.symeda.sormas.api.infrastructure.region.RegionFacade;
import de.symeda.sormas.api.outbreak.OutbreakFacade;
import de.symeda.sormas.api.person.PersonFacade;
import de.symeda.sormas.api.report.WeeklyReportFacade;
import de.symeda.sormas.api.sample.PathogenTestFacade;
import de.symeda.sormas.api.sample.SampleFacade;
import de.symeda.sormas.api.symptoms.SymptomsFacade;
import de.symeda.sormas.api.task.TaskFacade;
import de.symeda.sormas.api.travelentry.TravelEntryFacade;
import de.symeda.sormas.api.travelentry.travelentryimport.TravelEntryImportFacade;
import de.symeda.sormas.api.user.UserFacade;
import de.symeda.sormas.api.user.UserRoleFacade;
import de.symeda.sormas.api.vaccination.VaccinationFacade;
import de.symeda.sormas.api.visit.VisitFacade;
import de.symeda.sormas.backend.campaign.CampaignFacadeEjb;
import de.symeda.sormas.backend.campaign.data.CampaignFormDataFacadeEjb;
import de.symeda.sormas.backend.campaign.form.CampaignFormMetaFacadeEjb;
import de.symeda.sormas.backend.caze.CaseFacadeEjb.CaseFacadeEjbLocal;
import de.symeda.sormas.backend.caze.caseimport.CaseImportFacadeEjb.CaseImportFacadeEjbLocal;
import de.symeda.sormas.backend.caze.classification.CaseClassificationFacadeEjb.CaseClassificationFacadeEjbLocal;
import de.symeda.sormas.backend.common.ConfigFacadeEjb.ConfigFacadeEjbLocal;
import de.symeda.sormas.backend.contact.ContactFacadeEjb.ContactFacadeEjbLocal;
import de.symeda.sormas.backend.document.DocumentFacadeEjb.DocumentFacadeEjbLocal;
import de.symeda.sormas.backend.epidata.EpiDataFacadeEjb.EpiDataFacadeEjbLocal;
import de.symeda.sormas.backend.event.EventFacadeEjb.EventFacadeEjbLocal;
import de.symeda.sormas.backend.event.EventParticipantFacadeEjb.EventParticipantFacadeEjbLocal;
import de.symeda.sormas.backend.event.eventimport.EventImportFacadeEjb.EventImportFacadeEjbLocal;
import de.symeda.sormas.backend.externalmessage.ExternalMessageFacadeEjb;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb;
import de.symeda.sormas.backend.geo.GeoShapeProviderEjb.GeoShapeProviderEjbLocal;
import de.symeda.sormas.backend.hospitalization.HospitalizationFacadeEjb.HospitalizationFacadeEjbLocal;
import de.symeda.sormas.backend.immunization.ImmunizationFacadeEjb;
import de.symeda.sormas.backend.importexport.ExportFacadeEjb.ExportFacadeEjbLocal;
import de.symeda.sormas.backend.importexport.ImportFacadeEjb.ImportFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb.CommunityFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.country.CountryFacadeEjb;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb.DistrictFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb.FacilityFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.pointofentry.PointOfEntryFacadeEjb.PointOfEntryFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.region.RegionFacadeEjb.RegionFacadeEjbLocal;
import de.symeda.sormas.backend.outbreak.OutbreakFacadeEjb.OutbreakFacadeEjbLocal;
import de.symeda.sormas.backend.person.PersonFacadeEjb.PersonFacadeEjbLocal;
import de.symeda.sormas.backend.report.WeeklyReportFacadeEjb.WeeklyReportFacadeEjbLocal;
import de.symeda.sormas.backend.sample.PathogenTestFacadeEjb.PathogenTestFacadeEjbLocal;
import de.symeda.sormas.backend.sample.SampleFacadeEjb.SampleFacadeEjbLocal;
import de.symeda.sormas.backend.symptoms.SymptomsFacadeEjb.SymptomsFacadeEjbLocal;
import de.symeda.sormas.backend.task.TaskFacadeEjb.TaskFacadeEjbLocal;
import de.symeda.sormas.backend.travelentry.TravelEntryFacadeEjb;
import de.symeda.sormas.backend.travelentry.travelentryimport.TravelEntryImportFacadeEjb;
import de.symeda.sormas.backend.user.UserFacadeEjb.UserFacadeEjbLocal;
import de.symeda.sormas.backend.user.UserRoleFacadeEjb;
import de.symeda.sormas.backend.vaccination.VaccinationFacadeEjb;
import de.symeda.sormas.backend.visit.VisitFacadeEjb.VisitFacadeEjbLocal;
import info.novatec.beantest.api.BeanProviderHelper;

public final class FacadeProviderMock extends FacadeProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <P> P lookupEjbRemote(Class<P> clazz) {
		BeanProviderHelper bm = BeanProviderHelper.getInstance();
		if (CaseFacade.class == clazz) {
			return (P) bm.getBean(CaseFacadeEjbLocal.class);
		} else if (ContactFacade.class == clazz) {
			return (P) bm.getBean(ContactFacadeEjbLocal.class);
		} else if (EventFacade.class == clazz) {
			return (P) bm.getBean(EventFacadeEjbLocal.class);
		} else if (EventParticipantFacade.class == clazz) {
			return (P) bm.getBean(EventParticipantFacadeEjbLocal.class);
		} else if (VisitFacade.class == clazz) {
			return (P) bm.getBean(VisitFacadeEjbLocal.class);
		} else if (PersonFacade.class == clazz) {
			return (P) bm.getBean(PersonFacadeEjbLocal.class);
		} else if (TaskFacade.class == clazz) {
			return (P) bm.getBean(TaskFacadeEjbLocal.class);
		} else if (SampleFacade.class == clazz) {
			return (P) bm.getBean(SampleFacadeEjbLocal.class);
		} else if (PathogenTestFacade.class == clazz) {
			return (P) bm.getBean(PathogenTestFacadeEjbLocal.class);
		} else if (SymptomsFacade.class == clazz) {
			return (P) bm.getBean(SymptomsFacadeEjbLocal.class);
		} else if (FacilityFacade.class == clazz) {
			return (P) bm.getBean(FacilityFacadeEjbLocal.class);
		} else if (CountryFacade.class == clazz) {
			return (P) bm.getBean(CountryFacadeEjb.CountryFacadeEjbLocal.class);
		} else if (RegionFacade.class == clazz) {
			return (P) bm.getBean(RegionFacadeEjbLocal.class);
		} else if (DistrictFacade.class == clazz) {
			return (P) bm.getBean(DistrictFacadeEjbLocal.class);
		} else if (CommunityFacade.class == clazz) {
			return (P) bm.getBean(CommunityFacadeEjbLocal.class);
		} else if (UserFacade.class == clazz) {
			return (P) bm.getBean(UserFacadeEjbLocal.class);
		} else if (HospitalizationFacade.class == clazz) {
			return (P) bm.getBean(HospitalizationFacadeEjbLocal.class);
		} else if (EpiDataFacade.class == clazz) {
			return (P) bm.getBean(EpiDataFacadeEjbLocal.class);
		} else if (WeeklyReportFacade.class == clazz) {
			return (P) bm.getBean(WeeklyReportFacadeEjbLocal.class);
		} else if (GeoShapeProvider.class == clazz) {
			return (P) bm.getBean(GeoShapeProviderEjbLocal.class);
		} else if (OutbreakFacade.class == clazz) {
			return (P) bm.getBean(OutbreakFacadeEjbLocal.class);
		} else if (ConfigFacade.class == clazz) {
			return (P) bm.getBean(ConfigFacadeEjbLocal.class);
		} else if (ExportFacade.class == clazz) {
			return (P) bm.getBean(ExportFacadeEjbLocal.class);
		} else if (ImportFacade.class == clazz) {
			return (P) bm.getBean(ImportFacadeEjbLocal.class);
		} else if (CaseClassificationFacade.class == clazz) {
			return (P) bm.getBean(CaseClassificationFacadeEjbLocal.class);
		} else if (PointOfEntryFacade.class == clazz) {
			return (P) bm.getBean(PointOfEntryFacadeEjbLocal.class);
		} else if (CampaignFacade.class == clazz) {
			return (P) bm.getBean(CampaignFacadeEjb.CampaignFacadeEjbLocal.class);
		} else if (CampaignFormMetaFacade.class == clazz) {
			return (P) bm.getBean(CampaignFormMetaFacadeEjb.CampaignFormMetaFacadeEjbLocal.class);
		} else if (CampaignFormDataFacade.class == clazz) {
			return (P) bm.getBean(CampaignFormDataFacadeEjb.CampaignFormDataFacadeEjbLocal.class);
		} else if (CaseImportFacade.class == clazz) {
			return (P) bm.getBean(CaseImportFacadeEjbLocal.class);
		} else if (EventImportFacade.class == clazz) {
			return (P) bm.getBean(EventImportFacadeEjbLocal.class);
		} else if (DocumentFacade.class == clazz) {
			return (P) bm.getBean(DocumentFacadeEjbLocal.class);
		} else if (ImmunizationFacade.class == clazz) {
			return (P) bm.getBean(ImmunizationFacadeEjb.ImmunizationFacadeEjbLocal.class);
		} else if (VaccinationFacade.class == clazz) {
			return (P) bm.getBean(VaccinationFacadeEjb.VaccinationFacadeEjbLocal.class);
		} else if (TravelEntryFacade.class == clazz) {
			return (P) bm.getBean(TravelEntryFacadeEjb.TravelEntryFacadeEjbLocal.class);
		} else if (TravelEntryImportFacade.class == clazz) {
			return (P) bm.getBean(TravelEntryImportFacadeEjb.TravelEntryImportFacadeEjbLocal.class);
		} else if (ExternalMessageFacade.class == clazz) {
			return (P) bm.getBean(ExternalMessageFacadeEjb.ExternalMessageFacadeEjbLocal.class);
		} else if (FeatureConfigurationFacade.class == clazz) {
			return (P) bm.getBean(FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal.class);
		} else if (UserRoleFacade.class == clazz) {
			return (P) bm.getBean(UserRoleFacadeEjb.UserRoleFacadeEjbLocal.class);
		}

		return null;
	}

	public static CaseFacadeEjbLocal getCaseFacade() {
		BeanProviderHelper bm = BeanProviderHelper.getInstance();
		return bm.getBean(CaseFacadeEjbLocal.class);
	}

	public static ContactFacadeEjbLocal getContactFacade() {
		BeanProviderHelper bm = BeanProviderHelper.getInstance();
		return bm.getBean(ContactFacadeEjbLocal.class);
	}

	public static EventFacadeEjbLocal getEventFacade() {
		BeanProviderHelper bm = BeanProviderHelper.getInstance();
		return bm.getBean(EventFacadeEjbLocal.class);
	}

	public static EventParticipantFacadeEjbLocal getEventParticipantFacade() {
		BeanProviderHelper bm = BeanProviderHelper.getInstance();
		return bm.getBean(EventParticipantFacadeEjbLocal.class);
	}

	public static SampleFacadeEjbLocal getSampleFacade() {
		BeanProviderHelper bm = BeanProviderHelper.getInstance();
		return bm.getBean(SampleFacadeEjbLocal.class);
	}
}
