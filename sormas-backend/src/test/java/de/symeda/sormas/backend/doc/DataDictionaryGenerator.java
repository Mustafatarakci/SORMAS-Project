package de.symeda.sormas.backend.doc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.info.InfoFacadeEjb.InfoFacadeEjbLocal;

/**
 * Intentionally named *Generator because we don't want Maven to execute this
 * class automatically.
 */
public class DataDictionaryGenerator extends AbstractBeanTest {

	@Test
	public void generateDataDictionary() throws IOException {
		File output = new File("../sormas-api/src/main/resources/doc/SORMAS_Data_Dictionary.xlsx");
		String documentPath = getBean(InfoFacadeEjbLocal.class).generateDataDictionary();

		Files.copy(Paths.get(documentPath), output.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
}
