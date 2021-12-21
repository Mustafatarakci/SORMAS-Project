package de.symeda.sormas.ui.configuration.infrastructure;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.infrastructure.district.DistrictDto;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import de.symeda.sormas.ui.utils.AbstractEditForm;

public class DistrictEditForm extends AbstractEditForm<DistrictDto> {

	private static final long serialVersionUID = 7573666294384000190L;

	private static final String HTML_LAYOUT =
		fluidRowLocs(DistrictDto.NAME, DistrictDto.EPID_CODE) + fluidRowLocs(DistrictDto.REGION) + fluidRowLocs(RegionDto.EXTERNAL_ID); // ,DistrictDto.GROWTH_RATE);

	private final boolean create;

	public DistrictEditForm(boolean create) {

		super(DistrictDto.class, DistrictDto.I18N_PREFIX, false);
		this.create = create;

		setWidth(540, Unit.PIXELS);

		if (create) {
			hideValidationUntilNextCommit();
		}
		addFields();
	}

	@Override
	protected void addFields() {

		addField(DistrictDto.NAME, TextField.class);
		addField(DistrictDto.EPID_CODE, TextField.class);
		ComboBox region = addInfrastructureField(DistrictDto.REGION);
		addField(RegionDto.EXTERNAL_ID, TextField.class);
//		TextField growthRate = addField(DistrictDto.GROWTH_RATE, TextField.class);
//		growthRate.setConverter(new StringToFloatConverter());
//		growthRate.setConversionError(I18nProperties.getValidationError(Validations.onlyDecimalNumbersAllowed, growthRate.getCaption()));

		setRequired(true, DistrictDto.NAME, DistrictDto.EPID_CODE, DistrictDto.REGION);

		region.addItems(FacadeProvider.getRegionFacade().getAllActiveAsReference());

		// TODO: Workaround until cases and other data is properly transfered when infrastructure data changes
		if (!create) {
			region.setEnabled(false);
		}
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}
}
