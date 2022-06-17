package de.symeda.sormas.ui.user;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.SubMenu;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.configuration.DevModeView;
import de.symeda.sormas.ui.configuration.docgeneration.DocumentTemplatesView;
import de.symeda.sormas.ui.configuration.infrastructure.AreasView;
import de.symeda.sormas.ui.configuration.infrastructure.CommunitiesView;
import de.symeda.sormas.ui.configuration.infrastructure.ContinentsView;
import de.symeda.sormas.ui.configuration.infrastructure.CountriesView;
import de.symeda.sormas.ui.configuration.infrastructure.DistrictsView;
import de.symeda.sormas.ui.configuration.infrastructure.FacilitiesView;
import de.symeda.sormas.ui.configuration.infrastructure.PointsOfEntryView;
import de.symeda.sormas.ui.configuration.infrastructure.PopulationDataView;
import de.symeda.sormas.ui.configuration.infrastructure.RegionsView;
import de.symeda.sormas.ui.configuration.infrastructure.SubcontinentsView;
import de.symeda.sormas.ui.configuration.linelisting.LineListingConfigurationView;
import de.symeda.sormas.ui.configuration.outbreak.OutbreaksView;
import de.symeda.sormas.ui.utils.AbstractSubNavigationView;
import de.symeda.sormas.ui.utils.DirtyStateComponent;

public class AbstractUserView extends AbstractSubNavigationView<DirtyStateComponent> {

	protected AbstractUserView(String viewName) {
		super(viewName);
	}

	@Override
	public void refreshMenu(SubMenu menu, String params) {
		menu.removeAllViews();

		if (UserProvider.getCurrent().hasUserRight(UserRight.OUTBREAK_VIEW)
			&& FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.OUTBREAKS)) {
			menu.addView(
				OutbreaksView.VIEW_NAME,
				I18nProperties.getPrefixCaption("View", OutbreaksView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
				params);
		}

		boolean isCaseSurveillanceEnabled = FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.CASE_SURVEILANCE);
		boolean isAnySurveillanceEnabled = FacadeProvider.getFeatureConfigurationFacade().isAnySurveillanceEnabled();

		if (UserProvider.getCurrent().hasUserRight(UserRight.INFRASTRUCTURE_VIEW)) {
			if (FacadeProvider.getFeatureConfigurationFacade().isCountryEnabled()) {
				menu.addView(
					ContinentsView.VIEW_NAME,
					I18nProperties.getPrefixCaption("View", ContinentsView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
					null,
					false);
				menu.addView(
					SubcontinentsView.VIEW_NAME,
					I18nProperties.getPrefixCaption("View", SubcontinentsView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
					null,
					false);
				menu.addView(
					CountriesView.VIEW_NAME,
					I18nProperties.getPrefixCaption("View", CountriesView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
					null,
					false);
			}
			if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.INFRASTRUCTURE_TYPE_AREA)) {
				menu.addView(
					AreasView.VIEW_NAME,
					I18nProperties.getPrefixCaption("View", AreasView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
					null,
					false);
			}
			menu.addView(
				RegionsView.VIEW_NAME,
				I18nProperties.getPrefixCaption("View", RegionsView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
				null,
				false);
			menu.addView(
				DistrictsView.VIEW_NAME,
				I18nProperties.getPrefixCaption("View", DistrictsView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
				null,
				false);
			menu.addView(
				CommunitiesView.VIEW_NAME,
				I18nProperties.getPrefixCaption("View", CommunitiesView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
				null,
				false);
			if (isAnySurveillanceEnabled) {
				menu.addView(
					FacilitiesView.VIEW_NAME,
					I18nProperties.getPrefixCaption("View", FacilitiesView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
					null,
					false);
			}
			if (isCaseSurveillanceEnabled) {
				menu.addView(
					PointsOfEntryView.VIEW_NAME,
					I18nProperties.getPrefixCaption("View", PointsOfEntryView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
					null,
					false);
			}

			if (UserProvider.getCurrent().hasUserRight(UserRight.POPULATION_MANAGE)) {
				menu.addView(
					PopulationDataView.VIEW_NAME,
					I18nProperties.getPrefixCaption("View", PopulationDataView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
					null,
					false);
			}
		}

		//		if (LoginHelper.hasUserRight(UserRight.USER_RIGHTS_MANAGE)) {
		//			menu.addView(UserRightsView.VIEW_NAME, I18nProperties.getPrefixFragment("View",
		//					UserRightsView.VIEW_NAME.replaceAll("/", ".") + ".short", ""), params);
		//		}

		if (isCaseSurveillanceEnabled && UserProvider.getCurrent().hasUserRight(UserRight.LINE_LISTING_CONFIGURE)) {
			RegionReferenceDto region = UserProvider.getCurrent().getUser().getRegion();
			menu.addView(
				LineListingConfigurationView.VIEW_NAME,
				I18nProperties.getPrefixCaption("View", LineListingConfigurationView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
				region != null ? region.getUuid() : null,
				false);
		}
		if (isAnySurveillanceEnabled && UserProvider.getCurrent().hasUserRight(UserRight.DOCUMENT_TEMPLATE_MANAGEMENT)) {
			menu.addView(
				DocumentTemplatesView.VIEW_NAME,
				I18nProperties.getPrefixCaption("View", DocumentTemplatesView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
				null,
				false);
		}
		if (FacadeProvider.getConfigFacade().isDevMode() && UserProvider.getCurrent().hasUserRight(UserRight.DEV_MODE)) {
			menu.addView(
				DevModeView.VIEW_NAME,
				I18nProperties.getPrefixCaption("View", DevModeView.VIEW_NAME.replaceAll("/", ".") + ".short", ""),
				null,
				false);
		}
	}
}
