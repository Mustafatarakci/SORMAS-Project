package de.symeda.sormas.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.i18n.I18nProperties;

@Singleton(name = "StartupShutdownService")
@Startup
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StartupShutdownService {

	@PostConstruct
	public void startup() {
		I18nProperties.setDefaultLanguage(Language.fromLocaleString(FacadeProvider.getConfigFacade().getCountryLocale()));
	}

	@PreDestroy
	public void shutdown() {

	}
}
