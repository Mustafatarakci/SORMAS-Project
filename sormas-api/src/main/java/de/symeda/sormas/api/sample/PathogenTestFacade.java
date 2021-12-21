package de.symeda.sormas.api.sample;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;

@Remote
public interface PathogenTestFacade {

	List<PathogenTestDto> getAllActivePathogenTestsAfter(Date date);

	List<PathogenTestDto> getAllBySample(SampleReferenceDto sampleRef);

	PathogenTestDto getByUuid(String uuid);

	PathogenTestDto savePathogenTest(@Valid PathogenTestDto dto);

	List<String> getAllActiveUuids();

	List<PathogenTestDto> getByUuids(List<String> uuids);

	void deletePathogenTest(String pathogenTestUuid);

	boolean hasPathogenTest(SampleReferenceDto sample);

	void validate(PathogenTestDto pathogenTest) throws ValidationRuntimeException;

	List<String> getDeletedUuidsSince(Date since);

	Date getLatestPathogenTestDate(String sampleUuid);

	List<PathogenTestDto> getBySampleUuids(List<String> sampleUuids);

	PathogenTestDto getLatestPathogenTest(String uuid);

	Page<PathogenTestDto> getIndexPage(PathogenTestCriteria pathogenTestCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

}
