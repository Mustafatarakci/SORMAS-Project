package de.symeda.sormas.ui.configuration.infrastructure;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.infrastructure.community.CommunityDto;
import de.symeda.sormas.api.infrastructure.district.DistrictDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;

public class CommunityEditForm extends AbstractEditForm<CommunityDto> {

	private static final long serialVersionUID = 6726008587163831260L;

	private static final String HTML_LAYOUT =
		loc(CommunityDto.NAME) + fluidRowLocs(CommunityDto.REGION, CommunityDto.DISTRICT) + fluidRowLocs(RegionDto.EXTERNAL_ID);

	private boolean create;

	public CommunityEditForm(boolean create) {

		super(CommunityDto.class, CommunityDto.I18N_PREFIX, false);
		this.create = create;

		setWidth(540, Unit.PIXELS);

		if (create) {
			hideValidationUntilNextCommit();
		}
		addFields();
	}

	@Override
	protected void addFields() {

		addField(CommunityDto.NAME, TextField.class);
		ComboBox region = addInfrastructureField(CommunityDto.REGION);
		ComboBox district = addInfrastructureField(CommunityDto.DISTRICT);
		addField(RegionDto.EXTERNAL_ID, TextField.class);

		setRequired(true, CommunityDto.NAME, CommunityDto.REGION, CommunityDto.DISTRICT);

		region.addValueChangeListener(e -> {
			RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();
			FieldHelper
				.updateItems(district, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
		});

		district.addValueChangeListener(e -> {
			if (e.getProperty().getValue() != null && region.getValue() == null) {
				DistrictDto communityDistrict =
					FacadeProvider.getDistrictFacade().getByUuid(((DistrictReferenceDto) e.getProperty().getValue()).getUuid());
				region.setValue(communityDistrict.getRegion());
			}
		});

		region.addItems(FacadeProvider.getRegionFacade().getAllActiveAsReference());

		// TODO: Workaround until cases and other data is properly transfered when infrastructure data changes
		if (!create) {
			region.setEnabled(false);
			district.setEnabled(false);
		}

	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}
}
