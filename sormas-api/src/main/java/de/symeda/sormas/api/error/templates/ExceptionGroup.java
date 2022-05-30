package de.symeda.sormas.api.error.templates;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;

public enum ExceptionGroup {

    RECORD_NOT_FOUND_MSG;

    public String toString() {
        return I18nProperties.getCaption(Captions.Exception_recordNotFoundMsg);
    }
}
