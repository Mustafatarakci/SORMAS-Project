package de.symeda.sormas.backend.externalmessage.labmessage;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_BIG;
import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import de.symeda.auditlog.api.Audited;
import de.symeda.sormas.api.sample.PCRTestSpecification;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.externalmessage.ExternalMessage;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;


@Entity(name = TestReport.TABLE_NAME)
@Audited
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class TestReport extends AbstractDomainObject {

	private static final long serialVersionUID = -9164498173635523905L;

	public static final String TABLE_NAME = "testreport";

	public static final String LAB_MESSAGE = "labMessage";
	public static final String TEST_LAB_NAME = "testLabName";
	public static final String TEST_LAB_EXTERNAL_IDS = "testLabExternalIds";
	public static final String TEST_LAB_POSTAL_CODE = "testLabPostalCode";
	public static final String TEST_LAB_CITY = "testLabCity";
	public static final String TEST_TYPE = "testType";
	public static final String TEST_DATE_TIME = "testDateTime";
	public static final String TEST_RESULT = "testResult";
	public static final String TEST_RESULT_VERIFIED = "testResultVerified";
	public static final String TEST_RESULT_TEXT = "testResultText";
	public static final String TEST_PCR_TEST_SPECIFICATION = "testPcrTestSpecification";

	private ExternalMessage labMessage;
	private String testLabName;
	private List<String> testLabExternalIds;
	private String testLabPostalCode;
	private String testLabCity;

	private PathogenTestType testType;
	private Date testDateTime;
	private PathogenTestResultType testResult;
	private Boolean testResultVerified;
	private String testResultText;
	private String typingId;
	private String externalId;
	private String externalOrderId;
	private String testedDiseaseVariant;
	private String testedDiseaseVariantDetails;
	private Boolean preliminary;
	private PCRTestSpecification testPcrTestSpecification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public ExternalMessage getLabMessage() {
		return labMessage;
	}

	public void setLabMessage(ExternalMessage externalMessage) {
		this.labMessage = externalMessage;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getTestLabName() {
		return testLabName;
	}

	public void setTestLabName(String testLabName) {
		this.testLabName = testLabName;
	}

	@Type(type = "list-array")
	@Column(
			name = "testlabexternalids",
			columnDefinition = "VARCHAR(255) ARRAY"
	)
	public List<String> getTestLabExternalIds() {
		return testLabExternalIds;
	}

	public void setTestLabExternalIds(List<String> testLabExternalIds) {
		this.testLabExternalIds = testLabExternalIds;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getTestLabPostalCode() {
		return testLabPostalCode;
	}

	public void setTestLabPostalCode(String testLabPostalCode) {
		this.testLabPostalCode = testLabPostalCode;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getTestLabCity() {
		return testLabCity;
	}

	public void setTestLabCity(String testLabCity) {
		this.testLabCity = testLabCity;
	}

	@Enumerated(EnumType.STRING)
	public PathogenTestType getTestType() {
		return testType;
	}

	public void setTestType(PathogenTestType testType) {
		this.testType = testType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTestDateTime() {
		return testDateTime;
	}

	public void setTestDateTime(Date testDateTime) {
		this.testDateTime = testDateTime;
	}

	@Enumerated(EnumType.STRING)
	public PathogenTestResultType getTestResult() {
		return testResult;
	}

	public void setTestResult(PathogenTestResultType testResult) {
		this.testResult = testResult;
	}

	@Column
	public Boolean isTestResultVerified() {
		return testResultVerified;
	}

	public void setTestResultVerified(Boolean testResultVerified) {
		this.testResultVerified = testResultVerified;
	}

	@Column(length = CHARACTER_LIMIT_BIG)
	public String getTestResultText() {
		return testResultText;
	}

	public void setTestResultText(String testResultText) {
		this.testResultText = testResultText;
	}

	public String getTypingId() {
		return typingId;
	}

	public void setTypingId(String typingId) {
		this.typingId = typingId;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getExternalOrderId() {
		return externalOrderId;
	}

	public void setExternalOrderId(String externalOrderId) {
		this.externalOrderId = externalOrderId;
	}

	public String getTestedDiseaseVariant() {
		return testedDiseaseVariant;
	}

	public void setTestedDiseaseVariant(String diseaseVariant) {
		this.testedDiseaseVariant = diseaseVariant;
	}

	public String getTestedDiseaseVariantDetails() {
		return testedDiseaseVariantDetails;
	}

	public void setTestedDiseaseVariantDetails(String diseaseVariantDetails) {
		this.testedDiseaseVariantDetails = diseaseVariantDetails;
	}

	@Column
	public Boolean getPreliminary() {
		return preliminary;
	}

	public void setPreliminary(Boolean preliminary) {
		this.preliminary = preliminary;
	}

	@Enumerated(EnumType.STRING)
	public PCRTestSpecification getTestPcrTestSpecification() {
		return testPcrTestSpecification;
	}

	public void setTestPcrTestSpecification(PCRTestSpecification testPcrTestSpecification) {
		this.testPcrTestSpecification = testPcrTestSpecification;
	}
}
