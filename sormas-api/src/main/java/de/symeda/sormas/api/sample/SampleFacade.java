package de.symeda.sormas.api.sample;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;

@Remote
public interface SampleFacade {

	List<SampleDto> getAllActiveSamplesAfter(Date date);

	List<SampleIndexDto> getIndexList(SampleCriteria sampleCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	Page<SampleIndexDto> getIndexPage(SampleCriteria sampleCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<SampleExportDto> getExportList(SampleCriteria sampleCriteria, Collection<String> selectedRows, int first, int max);

	List<SampleExportDto> getExportList(CaseCriteria caseCriteria, Collection<String> selectedRows, int first, int max);

	long count(SampleCriteria sampleCriteria);

	SampleDto getSampleByUuid(String uuid);

	SampleDto saveSample(@Valid SampleDto dto);

	SampleReferenceDto getReferenceByUuid(String uuid);

	SampleReferenceDto getReferredFrom(String sampleUuid);

	List<String> getAllActiveUuids();

	List<SampleDto> getByUuids(List<String> uuids);

	void deleteSample(SampleReferenceDto sampleRef);

	void deleteAllSamples(List<String> sampleUuids);

	List<String> deleteSamples(List<String> sampleUuids);

	void validate(SampleDto sample) throws ValidationRuntimeException;

	List<String> getDeletedUuidsSince(Date since);

	boolean isDeleted(String sampleUuid);

	Map<PathogenTestResultType, Long> getNewTestResultCountByResultType(List<Long> caseIds);

	List<SampleDto> getByCaseUuids(List<String> caseUuids);

	Boolean isSampleEditAllowed(String sampleUuid);

	List<SampleDto> getByContactUuids(List<String> contactUuids);

	List<SampleDto> getSimilarSamples(SampleSimilarityCriteria criteria);

	boolean exists(String uuid);

	List<SampleDto> getByEventParticipantUuids(List<String> asList);

	List<SampleDto> getByLabSampleId(String labSampleId);
}
