package de.symeda.sormas.backend.therapy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.therapy.PrescriptionDto;
import de.symeda.sormas.api.therapy.PrescriptionIndexDto;
import de.symeda.sormas.api.user.DefaultUserRole;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.TestDataCreator.RDCFEntities;

public class PrescriptionFacadeEjbTest extends AbstractBeanTest {

	@Test
	public void testPrescriptionDeletion() {

		RDCFEntities rdcf = creator.createRDCFEntities();
		UserDto user = creator.createUser(
			rdcf,
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR),
			creator.getUserRoleReference(DefaultUserRole.CASE_SUPERVISOR));
		UserDto admin = getUserFacade().getByUserName("admin");
		PersonDto casePerson = creator.createPerson("Case", "Person");
		CaseDataDto caze = creator.createCase(user.toReference(), casePerson.toReference(), rdcf);
		PrescriptionDto prescription = creator.createPrescription(caze);

		// Database should contain the created prescription
		assertNotNull(getPrescriptionFacade().getPrescriptionByUuid(prescription.getUuid()));

		getPrescriptionFacade().deletePrescription(prescription.getUuid());

		// Database should not contain the deleted visit
		assertNull(getPrescriptionFacade().getPrescriptionByUuid(prescription.getUuid()));
	}

	@Test
	public void testPrescriptionIndexListGeneration() {

		RDCFEntities rdcf = creator.createRDCFEntities();
		UserDto user = creator.createUser(
			rdcf,
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR),
			creator.getUserRoleReference(DefaultUserRole.CASE_SUPERVISOR));
		PersonDto casePerson = creator.createPerson("Case", "Person");
		CaseDataDto caze = creator.createCase(user.toReference(), casePerson.toReference(), rdcf);
		creator.createPrescription(caze);

		List<PrescriptionIndexDto> results = getPrescriptionFacade().getIndexList(null);

		assertEquals(1, results.size());
	}
}
