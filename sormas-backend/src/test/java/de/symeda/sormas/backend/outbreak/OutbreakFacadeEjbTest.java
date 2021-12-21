package de.symeda.sormas.backend.outbreak;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.MockProducer;
import de.symeda.sormas.backend.TestDataCreator.RDCFEntities;

public class OutbreakFacadeEjbTest extends AbstractBeanTest {

	private RDCFEntities rdcf;

	@Override
	public void init() {
		super.init();

		rdcf = creator.createRDCFEntities("Region", "District", "Community", "Facility");
		creator.createUser(rdcf.region.getUuid(), rdcf.district.getUuid(), rdcf.facility.getUuid(), "Surv", "Sup", UserRole.SURVEILLANCE_SUPERVISOR);

		when(MockProducer.getPrincipal().getName()).thenReturn("SurvSup");
	}

	@Test
	public void testOutbreakCreationAndDeletion() {

		DistrictReferenceDto district = new DistrictReferenceDto(rdcf.district.getUuid(), null, null);
		Disease disease = Disease.EVD;

		getOutbreakFacade().startOutbreak(district, disease);
		// outbreak should be active
		assertNotNull(getOutbreakFacade().getActiveByDistrictAndDisease(district, disease));

		getOutbreakFacade().endOutbreak(district, disease);
		// Database should contain no outbreak
		assertNull(getOutbreakFacade().getActiveByDistrictAndDisease(district, disease));
	}
}
