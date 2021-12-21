package de.symeda.sormas.ui.importer;

import de.symeda.sormas.api.caze.CaseSelectionDto;
import de.symeda.sormas.api.person.SimilarPersonDto;

public class CaseImportSimilarityResult extends PersonImportSimilarityResult {

	private final CaseSelectionDto matchingCase;

	public CaseImportSimilarityResult(SimilarPersonDto matchingPerson, CaseSelectionDto matchingCase, ImportSimilarityResultOption resultOption) {
		super(matchingPerson, resultOption);
		this.matchingCase = matchingCase;
	}

	public CaseSelectionDto getMatchingCase() {
		return matchingCase;
	}
}
