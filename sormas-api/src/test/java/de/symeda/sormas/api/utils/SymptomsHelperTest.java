package de.symeda.sormas.api.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.symptoms.SymptomsHelper;

public class SymptomsHelperTest {

	@Test
	public void testUpdateIsSymptomatic() {

		SymptomsDto symptoms = SymptomsDto.build();
		SymptomsHelper.updateIsSymptomatic(symptoms);

		assertFalse(symptoms.getSymptomatic());

		symptoms.setBackache(SymptomState.YES);
		SymptomsHelper.updateIsSymptomatic(symptoms);

		assertTrue(symptoms.getSymptomatic());
	}

	@Test
	public void testUpdateSymptoms() {

		SymptomsDto sourceSymptoms = SymptomsDto.build();
		SymptomsDto targetSymptoms = SymptomsDto.build();

		// Set a previously unset symptom to YES
		sourceSymptoms.setAbdominalPain(SymptomState.YES);
		SymptomsHelper.updateSymptoms(sourceSymptoms, targetSymptoms);
		assertEquals(SymptomState.YES, targetSymptoms.getAbdominalPain());

		// Don't set a previously set symptom to NO
		sourceSymptoms.setAbdominalPain(SymptomState.NO);
		SymptomsHelper.updateSymptoms(sourceSymptoms, targetSymptoms);
		assertEquals(SymptomState.YES, targetSymptoms.getAbdominalPain());

		// Always set the reported symptom if it is the first time the symptom is reported
		sourceSymptoms.setHeadache(SymptomState.NO);
		SymptomsHelper.updateSymptoms(sourceSymptoms, targetSymptoms);
		assertEquals(SymptomState.NO, targetSymptoms.getHeadache());

		// Update temperature if higher
		targetSymptoms.setTemperature(36.5f);
		sourceSymptoms.setTemperature(36.3f);
		SymptomsHelper.updateSymptoms(sourceSymptoms, targetSymptoms);
		assertEquals(new Float(36.5f), targetSymptoms.getTemperature());

		sourceSymptoms.setTemperature(37.5f);
		SymptomsHelper.updateSymptoms(sourceSymptoms, targetSymptoms);
		assertEquals(new Float(37.5f), targetSymptoms.getTemperature());

		// Update symptoms String
		String string = "Test";
		sourceSymptoms.setOtherHemorrhagicSymptomsText(string);
		SymptomsHelper.updateSymptoms(sourceSymptoms, targetSymptoms);
		assertEquals(string, targetSymptoms.getOtherHemorrhagicSymptomsText());

		String anotherString = "Tset";
		sourceSymptoms.setOtherHemorrhagicSymptomsText(anotherString);
		SymptomsHelper.updateSymptoms(sourceSymptoms, targetSymptoms);
		assertEquals(string + ", " + anotherString, targetSymptoms.getOtherHemorrhagicSymptomsText());
	}
}
