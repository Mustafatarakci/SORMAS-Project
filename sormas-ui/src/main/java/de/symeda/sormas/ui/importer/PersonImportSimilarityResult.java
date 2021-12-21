package de.symeda.sormas.ui.importer;

import de.symeda.sormas.api.person.SimilarPersonDto;

public class PersonImportSimilarityResult {

	private final SimilarPersonDto matchingPerson;
	private final ImportSimilarityResultOption resultOption;

	public PersonImportSimilarityResult(SimilarPersonDto matchingPerson, ImportSimilarityResultOption resultOption) {
		this.matchingPerson = matchingPerson;
		this.resultOption = resultOption;
	}

	public SimilarPersonDto getMatchingPerson() {
		return matchingPerson;
	}

	public ImportSimilarityResultOption getResultOption() {
		return resultOption;
	}
}
