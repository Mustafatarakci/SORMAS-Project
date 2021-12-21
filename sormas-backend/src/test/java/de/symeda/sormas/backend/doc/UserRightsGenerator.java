package de.symeda.sormas.backend.doc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

import de.symeda.sormas.backend.AbstractBeanTest;

/**
 * Intentionally named *Generator because we don't want Maven to execute this class automatically.
 */
public class UserRightsGenerator extends AbstractBeanTest {

	@Test
	public void generateUserRights() throws IOException {
		File output = new File("../sormas-api/src/main/resources/doc/SORMAS_User_Rights.xlsx");

		String documentPath = getUserRightsFacade().generateUserRightsDocument(false);

		Files.copy(Paths.get(documentPath), output.toPath(), StandardCopyOption.REPLACE_EXISTING);

//		Desktop.getDesktop().open(new File(filePath));
	}
}
