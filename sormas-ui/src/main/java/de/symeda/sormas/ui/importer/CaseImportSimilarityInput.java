package de.symeda.sormas.ui.importer;

import java.util.List;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseSelectionDto;
import de.symeda.sormas.api.person.PersonDto;

public class CaseImportSimilarityInput {

	private final CaseDataDto caze;
	private final PersonDto person;
	private final List<CaseSelectionDto> similarCases;

	public CaseImportSimilarityInput(CaseDataDto caze, PersonDto person, List<CaseSelectionDto> similarCases) {
		this.similarCases = similarCases;
		this.caze = caze;
		this.person = person;
	}

	public List<CaseSelectionDto> getSimilarCases() {
		return similarCases;
	}

	public CaseDataDto getCaze() {
		return caze;
	}

	public PersonDto getPerson() {
		return person;
	}
}
