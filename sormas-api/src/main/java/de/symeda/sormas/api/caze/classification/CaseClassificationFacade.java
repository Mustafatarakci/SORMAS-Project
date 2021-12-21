package de.symeda.sormas.api.caze.classification;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;

@Remote
public interface CaseClassificationFacade {

	CaseClassification getClassification(CaseDataDto caze);

	DiseaseClassificationCriteriaDto getByDisease(Disease disease);

	List<DiseaseClassificationCriteriaDto> getAllSince(Date changeDate);
}
