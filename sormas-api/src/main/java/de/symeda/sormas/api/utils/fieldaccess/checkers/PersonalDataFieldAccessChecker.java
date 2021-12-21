package de.symeda.sormas.api.utils.fieldaccess.checkers;

import java.lang.reflect.Field;

import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.EmbeddedPersonalData;
import de.symeda.sormas.api.utils.PersonalData;

public class PersonalDataFieldAccessChecker extends AnnotationBasedFieldAccessChecker {

	private PersonalDataFieldAccessChecker(final boolean hasRight) {
		super(PersonalData.class, EmbeddedPersonalData.class, hasRight);
	}

	public static PersonalDataFieldAccessChecker inJurisdiction(RightCheck rightCheck) {
		return new PersonalDataFieldAccessChecker(rightCheck.check(UserRight.SEE_PERSONAL_DATA_IN_JURISDICTION));
	}

	public static PersonalDataFieldAccessChecker outsideJurisdiction(RightCheck rightCheck) {
		return new PersonalDataFieldAccessChecker(rightCheck.check(UserRight.SEE_PERSONAL_DATA_OUTSIDE_JURISDICTION));
	}

	public static PersonalDataFieldAccessChecker forcedNoAccess() {
		return new PersonalDataFieldAccessChecker(false);
	}

	@Override
	protected boolean isAnnotatedFieldMandatory(Field annotatedField) {
		return annotatedField.getAnnotation(PersonalData.class).mandatoryField();
	}

	public interface RightCheck {

		boolean check(UserRight userRight);
	}
}
