package de.symeda.sormas.backend.caze;

import javax.inject.Inject;

import de.symeda.sormas.backend.caze.CaseFacadeEjb.CaseFacadeEjbLocal;
import de.symeda.sormas.backend.common.CronJob;

public class CalculateCaseCompletionJob extends CronJob {

	@Inject
	private CaseFacadeEjbLocal caseFacade;

	@Override
	protected boolean isJobEnabled() {
		return true;
	}

	@Override
	protected Integer execute() {
		return caseFacade.updateCompleteness();
	}
}
