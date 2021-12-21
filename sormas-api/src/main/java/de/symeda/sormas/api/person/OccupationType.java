package de.symeda.sormas.api.person;

import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.HideForCountries;
import de.symeda.sormas.api.utils.HideForCountriesExcept;

public enum OccupationType {

	// Switzerland NOGA specific Occupation Types
	/**
	 * G. WHOLESALE AND RETAIL TRADE; REPAIR OF MOTOR VEHICLES AND MOTORCYCLES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	RETAIL_AND_REPAIR_SERVICE,
	/**
	 * C.MANUFACTURING
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	MANUFACTURING,
	/**
	 * F.CONSTRUCTION
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	CONSTRUCTION,
	/**
	 * H.TRANSPORTATION AND STORAGE
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	TRANSPORT_AND_STORAGE,
	/**
	 * Q.HUMAN HEALTH AND SOCIAL WORK ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	HEALTH_AND_SOCIAL,
	/**
	 * P.EDUCATION
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	EDUCATION,
	/**
	 * I.ACCOMMODATION AND FOOD SERVICE ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	ACCOMMODATION_AND_FOOD_SERVICES,
	/**
	 * R.ARTS, ENTERTAINMENT AND RECREATION
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	ARTS_ENTERTAINMENT_AND_RECREATION,
	/**
	 * O.PUBLIC ADMINISTRATION AND DEFENCE; COMPULSORY SOCIAL SECURITY
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	PUBLIC_ADMINISTRATION_AND_DEFENCE,
	/**
	 * K.FINANCIAL AND INSURANCE ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	FINANCE_AND_INSURANCE,
	/**
	 * J.INFORMATION AND COMMUNICATION
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	INFORMATION_AND_COMMUNICATION,
	/**
	 * M.PROFESSIONAL, SCIENTIFIC AND TECHNICAL ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	PROFESSIONAL_SCIENTIFIC_AND_TECHNICAL,
	/**
	 * N.ADMINISTRATIVE AND SUPPORT SERVICE ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	ADMINISTRATIVE_AND_SUPPORT,
	/**
	 * S.OTHER SERVICE ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	SERVICE_OTHER,
	/**
	 * L.REAL ESTATE ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	REAL_ESTATE,
	/**
	 * D.ELECTRICITY, GAS, STEAM AND AIR-CONDITIONING SUPPLY
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	ENERGY_SUPPLY,
	/**
	 * E.WATER SUPPLY; SEWERAGE, WASTE MANAGEMENT AND REMEDIATION ACTIVITIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	WATER_SUPPLY_AND_WASTE,
	/**
	 * U.ACTIVITIES OF EXTRATERRITORIAL ORGANISATIONS AND BODIES
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	EXTRATERRITORIAL_ORGANIZATIONS,
	/**
	 * A.AGRICULTURE, FORESTRY AND FISHING
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	AGRICULTURE,
	/**
	 * B.MINING AND QUARRYING
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	MINING,
	/**
	 * T.ACTIVITIES OF HOUSEHOLDS AS EMPLOYERS; UNDIFFERENTIATED GOODS- AND SERVICES-PRODUCING ACTIVITIES OF HOUSEHOLDS FOR OWN USE
	 */
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	PRIVATE_HOUSEHOLD,

	// Generic Occupation Types
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	FARMER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	BUTCHER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	HUNTER_MEAT_TRADER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	MINER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	RELIGIOUS_LEADER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	HOUSEWIFE,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	PUPIL_STUDENT,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	CHILD,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	BUSINESSMAN_WOMAN,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	TRANSPORTER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	HEALTHCARE_WORKER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	TRADITIONAL_SPIRITUAL_HEALER,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	WORKING_WITH_ANIMALS,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	LABORATORY_STAFF,
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	OTHER;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

}
