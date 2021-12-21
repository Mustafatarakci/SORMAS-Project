package de.symeda.sormas.api.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.utils.Diseases.DiseasesConfiguration;

public class DiseasesConfigurationTest {

	public static class TestClass {

		@SuppressWarnings("unused")
		private String testNone;

		@Diseases()
		@SuppressWarnings("unused")
		private String testEmpty;

		@Diseases(Disease.EVD)
		@SuppressWarnings("unused")
		private String testOne;

		@Diseases({
			Disease.CHOLERA,
			Disease.CSM })
		@SuppressWarnings("unused")
		private String testMultiple;

		@Diseases(value = {
			Disease.CHOLERA,
			Disease.EVD }, hide = true)
		@SuppressWarnings("unused")
		private String testHide;
	}

	@Test
	public void testIsMissing() {

		assertTrue(DiseasesConfiguration.isMissing(TestClass.class, "testNone"));
		assertFalse(DiseasesConfiguration.isMissing(TestClass.class, "testEmpty"));
		assertFalse(DiseasesConfiguration.isMissing(TestClass.class, "testOne"));
		assertFalse(DiseasesConfiguration.isMissing(TestClass.class, "testMultiple"));
	}

	@Test
	public void testIsDefined() {

		assertFalse(DiseasesConfiguration.isDefined(TestClass.class, "testNone", Disease.NEW_INFLUENZA));
		assertFalse(DiseasesConfiguration.isDefined(TestClass.class, "testEmpty", Disease.NEW_INFLUENZA));
		assertFalse(DiseasesConfiguration.isDefined(TestClass.class, "testOne", Disease.NEW_INFLUENZA));
		assertTrue(DiseasesConfiguration.isDefined(TestClass.class, "testOne", Disease.EVD));
		assertFalse(DiseasesConfiguration.isDefined(TestClass.class, "testMultiple", Disease.NEW_INFLUENZA));
		assertTrue(DiseasesConfiguration.isDefined(TestClass.class, "testMultiple", Disease.CHOLERA));
		assertTrue(DiseasesConfiguration.isDefined(TestClass.class, "testMultiple", Disease.CSM));
	}

	@Test
	public void testHide() {

		assertTrue(DiseasesConfiguration.isDefined(TestClass.class, "testHide", Disease.NEW_INFLUENZA));
		assertFalse(DiseasesConfiguration.isDefined(TestClass.class, "testHide", Disease.CHOLERA));
	}

}
