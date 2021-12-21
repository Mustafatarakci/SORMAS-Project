package de.symeda.sormas.backend.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;

import de.symeda.sormas.api.utils.InfoProvider;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.MockProducer;

public class ConfigFacadeEjbTest extends AbstractBeanTest {

	@Test
	public void testValidateExternalUrls() {
		MockProducer.getProperties().setProperty(ConfigFacadeEjb.INTERFACE_SYMPTOM_JOURNAL_URL, "https://www.google.com");
		getConfigFacade().validateExternalUrls();

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.INTERFACE_SYMPTOM_JOURNAL_URL, "http://www.google.com");
		getConfigFacade().validateExternalUrls();

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.INTERFACE_SYMPTOM_JOURNAL_URL, "http://my-docker-service:12345/route/path");
		getConfigFacade().validateExternalUrls();

		try {
			MockProducer.getProperties().setProperty(ConfigFacadeEjb.INTERFACE_SYMPTOM_JOURNAL_URL, "htps://www.google.com#");
		} catch (IllegalArgumentException ignored) {
		}
	}

	@Test
	public void testValidateAppUrls() {

		Mockito.when(InfoProvider.get().getVersion()).thenReturn("0.7.0");
		Mockito.when(InfoProvider.get().getMinimumRequiredVersion()).thenReturn("0.5.0");
		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_URL, "https://www.sormas.org/downloads/sormas-0.7.0-release.apk");
		getConfigFacade().validateAppUrls();

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_URL, "https://www.sormas.org/downloads-0.4.0-test/sormas-0.7.0-release.apk");
		getConfigFacade().validateAppUrls();

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_URL, "https://www.sormas.org/downloads/sormas-release.apk");
		try {
			getConfigFacade().validateAppUrls();
			fail();
		} catch (IllegalArgumentException e) {
		}

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_URL, "https://www.sormas.org/downloads/sormas-0.4.0-release.apk");
		try {
			getConfigFacade().validateAppUrls();
			fail();
		} catch (IllegalArgumentException e) {
		}

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_URL, "https://www.sormas.org/downloads/sormas-0.8.0-release.apk");
		try {
			getConfigFacade().validateAppUrls();
			fail();
		} catch (IllegalArgumentException e) {
		}

		Mockito.when(InfoProvider.get().getVersion()).thenReturn("1.0.0");
		getConfigFacade().validateAppUrls();

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_LEGACY_URL, "https://www.sormas.org/downloads/sormas-0.8.0-release.apk");
		try {
			getConfigFacade().validateAppUrls();
			fail();
		} catch (IllegalArgumentException e) {
		}

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_LEGACY_URL, "https://www.sormas.org/downloads/sormas-0.7.0-release.apk");
		getConfigFacade().validateAppUrls();

		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_URL, "https://www.sormas.org/downloads/sormas-1.0.0-release.apk");
		getConfigFacade().validateAppUrls();

		// below minimum
		MockProducer.getProperties().setProperty(ConfigFacadeEjb.APP_LEGACY_URL, "https://www.sormas.org/downloads/sormas-0.4.0-release.apk");
		try {
			getConfigFacade().validateAppUrls();
			fail();
		} catch (IllegalArgumentException e) {
		}

	}

	@Test
	public void testNormalizeLocaleString() {

		assertThat(ConfigFacadeEjb.normalizeLocaleString("  "), isEmptyString());
		assertThat(ConfigFacadeEjb.normalizeLocaleString("en"), is("en"));
		assertThat(ConfigFacadeEjb.normalizeLocaleString("En"), is("en"));
		assertThat(ConfigFacadeEjb.normalizeLocaleString("en-CA"), is("en-CA"));
		assertThat(ConfigFacadeEjb.normalizeLocaleString("en-cA"), is("en-CA"));
	}
}
