package de.symeda.sormas.backend.geo;

public class GeoShapeProviderEjbTest {

//TODO: Re-implement relevant unit tests once shape files can be imported from the ui

//    private static BeanProviderHelper bm;
//    
//    @BeforeClass
//    public static void initialize() {
//        bm = BeanProviderHelper.getInstance();
//        
//		RegionService regionService = getBean(RegionService.class);
//		DistrictService districtService = getBean(DistrictService.class);
//
//		String countryName = getBean(ConfigFacadeEjbLocal.class).getCountryName();
//		List<Region> regions = regionService.getAll();
//
//		regionService.importRegions(countryName, regions);
//		districtService.importDistricts(countryName, regions);
//    }
//
//    @AfterClass
//    public static void cleanUp() {
//        bm.shutdown();
//    }
//    
//    protected static <T> T getBean(Class<T> beanClass, Annotation... qualifiers) {
//        return bm.getBean(beanClass, qualifiers);
//    }
//    
//
//	
//	@Test
//	public void testGetRegionShape() {
//		GeoShapeProvider geoShapeProvider = getBean(GeoShapeProviderEjbLocal.class);
//		RegionFacade regionFacade = getBean(RegionFacadeEjbLocal.class);
//		
//		List<RegionReferenceDto> regions = regionFacade.getAllActiveAsReference();
//		assertThat(regions.size(), greaterThan(1)); // make sure we have some regions
//		for (RegionReferenceDto region : regions) {
//			GeoLatLon[][] regionShape = geoShapeProvider.getRegionShape(region);
//			assertNotNull(regionShape);
//		}
//	}
//
//	@Test
//	public void testGetRegionByCoord() {
//		GeoShapeProvider geoShapeProvider = getBean(GeoShapeProviderEjbLocal.class);
//		RegionReferenceDto region = geoShapeProvider.getRegionByCoord(new GeoLatLon(9.076344, 7.276929));
//		assertEquals("FCT", region.getCaption());
//	}
//
//	@Test
//	public void testGetDistrictShape() {
//		GeoShapeProvider geoShapeProvider = getBean(GeoShapeProviderEjbLocal.class);
//
//		RegionReferenceDto region = geoShapeProvider.getRegionByCoord(new GeoLatLon(9.076344, 7.276929));
//		
//		DistrictFacade districtFacade = getBean(DistrictFacadeEjbLocal.class);
//		List<DistrictReferenceDto> districts = districtFacade.getAllActiveByRegion(region.getUuid());
//		assertThat(districts.size(), greaterThan(1)); // make sure we have some districts
//		
//		for (DistrictReferenceDto district : districts) {
//			GeoLatLon[][] districtShape = geoShapeProvider.getDistrictShape(district);
//			assertNotNull(districtShape);
//		}
//	}
//
//	@Test
//	public void testGetDistrictByCoord() {
//		GeoShapeProvider geoShapeProvider = getBean(GeoShapeProviderEjbLocal.class);
//		DistrictReferenceDto district = geoShapeProvider.getDistrictByCoord(new GeoLatLon(9.076344, 7.276929));
//		assertEquals("Abuja Municipal", district.getCaption());
//	}
//
//	@Test
//	public void testBuildCountryShape() {
//		GeoShapeProvider geoShapeProvider = getBean(GeoShapeProviderEjbLocal.class);
//
//		GeoLatLon[][] countryShape = geoShapeProvider.getCountryShape();
//		assertEquals(4, countryShape.length);
//	}
}
