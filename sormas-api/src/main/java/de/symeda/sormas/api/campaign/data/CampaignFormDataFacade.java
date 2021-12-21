package de.symeda.sormas.api.campaign.data;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.campaign.diagram.CampaignDiagramCriteria;
import de.symeda.sormas.api.campaign.diagram.CampaignDiagramDataDto;
import de.symeda.sormas.api.campaign.diagram.CampaignDiagramSeries;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface CampaignFormDataFacade {

	CampaignFormDataDto saveCampaignFormData(@Valid CampaignFormDataDto dto);

	List<CampaignFormDataDto> getByUuids(List<String> uuids);

	CampaignFormDataDto getCampaignFormDataByUuid(String campaignFormDataUuid);

	void deleteCampaignFormData(String campaignFormDataUuid);

	boolean isArchived(String campaignFormDataUuid);

	boolean exists(String uuid);

	CampaignFormDataReferenceDto getReferenceByUuid(String uuid);

	List<CampaignFormDataIndexDto> getIndexList(CampaignFormDataCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties);

	CampaignFormDataDto getExistingData(CampaignFormDataCriteria criteria);

	long count(CampaignFormDataCriteria criteria);

	List<CampaignDiagramDataDto> getDiagramData(List<CampaignDiagramSeries> diagramSeries, CampaignDiagramCriteria campaignDiagramCriteria);

	List<CampaignDiagramDataDto> getDiagramDataByAgeGroup(
		CampaignDiagramSeries diagramSeriesTotal,
		CampaignDiagramSeries diagramSeries,
		CampaignDiagramCriteria campaignDiagramCriteria);

	List<String> getAllActiveUuids();

	List<CampaignFormDataDto> getAllActiveAfter(Date date);

	void overwriteCampaignFormData(CampaignFormDataDto existingData, CampaignFormDataDto newData);
}
