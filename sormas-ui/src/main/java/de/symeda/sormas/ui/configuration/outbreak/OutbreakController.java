package de.symeda.sormas.ui.configuration.outbreak;

import java.util.Set;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.CommitListener;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

public class OutbreakController {

	public void openOutbreakConfigurationWindow(Disease disease, OutbreakRegionConfiguration diseaseOutbreakInformation) {
		OutbreakRegionConfigurationForm configurationForm = new OutbreakRegionConfigurationForm(diseaseOutbreakInformation);
		final CommitDiscardWrapperComponent<OutbreakRegionConfigurationForm> configurationComponent =
			new CommitDiscardWrapperComponent<OutbreakRegionConfigurationForm>(configurationForm);
		Window popupWindow = VaadinUiUtil.showModalPopupWindow(
			configurationComponent,
			disease.toShortString() + " " + I18nProperties.getString(Strings.headingOutbreakIn) + " "
				+ diseaseOutbreakInformation.getRegion().toString());

		configurationComponent.addCommitListener(new CommitListener() {

			@Override
			public void onCommit() {
				Set<DistrictReferenceDto> updatedAffectedDistricts = configurationForm.getAffectedDistricts();

				// start an outbreak for every newly affected district
				for (DistrictReferenceDto affectedDistrict : updatedAffectedDistricts) {
					if (!diseaseOutbreakInformation.getAffectedDistricts().contains(affectedDistrict)) {
						FacadeProvider.getOutbreakFacade().startOutbreak(affectedDistrict, disease);
					}
				}

				// stop outbreaks for districts that are not affected anymore
				for (DistrictReferenceDto prevAffectedDistrict : diseaseOutbreakInformation.getAffectedDistricts()) {
					if (!updatedAffectedDistricts.contains(prevAffectedDistrict)) {
						FacadeProvider.getOutbreakFacade().endOutbreak(prevAffectedDistrict, disease);
					}
				}

				popupWindow.close();
				Notification.show(I18nProperties.getString(Strings.messageOutbreakSaved), Type.WARNING_MESSAGE);
				SormasUI.get().getNavigator().navigateTo(OutbreaksView.VIEW_NAME);
			}
		});

		configurationComponent.addDiscardListener(() -> popupWindow.close());
	}
}
