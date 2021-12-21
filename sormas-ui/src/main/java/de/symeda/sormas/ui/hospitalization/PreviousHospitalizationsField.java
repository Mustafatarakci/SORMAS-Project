package de.symeda.sormas.ui.hospitalization;

import java.util.function.Consumer;

import com.vaadin.ui.Window;
import com.vaadin.v7.ui.Table;

import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.caze.AbstractTableField;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.CommitListener;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.DeleteListener;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

@SuppressWarnings("serial")
public class PreviousHospitalizationsField extends AbstractTableField<PreviousHospitalizationDto> {

	private static final String PERIOD = Captions.CasePreviousHospitalization_prevHospPeriod;
	private static final String COMMUNITY = Captions.community;
	private static final String DISTRICT = Captions.district;

	private FieldVisibilityCheckers fieldVisibilityCheckers;

	public PreviousHospitalizationsField(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
		super(fieldAccessCheckers);
		this.fieldVisibilityCheckers = fieldVisibilityCheckers;
	}

	@Override
	public Class<PreviousHospitalizationDto> getEntryType() {
		return PreviousHospitalizationDto.class;
	}

	@Override
	protected void updateColumns() {

		Table table = getTable();

		table.addGeneratedColumn(PERIOD, new Table.ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				PreviousHospitalizationDto prevHospitalization = (PreviousHospitalizationDto) itemId;
				if (prevHospitalization.getAdmissionDate() == null && prevHospitalization.getDischargeDate() == null) {
					return I18nProperties.getString(Strings.notSpecified);
				} else {
					StringBuilder periodBuilder = new StringBuilder();
					periodBuilder.append(
						prevHospitalization.getAdmissionDate() != null ? DateFormatHelper.formatDate(prevHospitalization.getAdmissionDate()) : "?");
					periodBuilder.append(" - ");
					periodBuilder.append(
						prevHospitalization.getDischargeDate() != null ? DateFormatHelper.formatDate(prevHospitalization.getDischargeDate()) : "?");
					return periodBuilder.toString();
				}
			}
		});

		table.addGeneratedColumn(COMMUNITY, (Table.ColumnGenerator) (source, itemId, columnId) -> {
			PreviousHospitalizationDto prevHospitalization = (PreviousHospitalizationDto) itemId;
			return prevHospitalization.getCommunity();
		});

		table.addGeneratedColumn(DISTRICT, (Table.ColumnGenerator) (source, itemId, columnId) -> {
			PreviousHospitalizationDto prevHospitalization = (PreviousHospitalizationDto) itemId;
			DistrictReferenceDto district = prevHospitalization.getDistrict();

			return district != null ? district.getCaption() : I18nProperties.getCaption(Captions.unknown);
		});

		table.addGeneratedColumn(PreviousHospitalizationDto.HEALTH_FACILITY, (Table.ColumnGenerator) (source, itemId, columnId) -> {
			PreviousHospitalizationDto prevHospitalization = (PreviousHospitalizationDto) itemId;
			FacilityReferenceDto healthFacility = prevHospitalization.getHealthFacility();

			return healthFacility != null ? healthFacility.getCaption() : I18nProperties.getCaption(Captions.unknown);
		});

		table.setVisibleColumns(
			EDIT_COLUMN_ID,
			PERIOD,
			PreviousHospitalizationDto.HEALTH_FACILITY,
			COMMUNITY,
			DISTRICT,
			PreviousHospitalizationDto.DESCRIPTION,
			PreviousHospitalizationDto.ISOLATED);

		table.setColumnExpandRatio(EDIT_COLUMN_ID, 0);
		table.setColumnExpandRatio(PERIOD, 0);
		table.setColumnExpandRatio(PreviousHospitalizationDto.HEALTH_FACILITY, 0);
		table.setColumnExpandRatio(COMMUNITY, 0);
		table.setColumnExpandRatio(DISTRICT, 0);
		table.setColumnExpandRatio(PreviousHospitalizationDto.DESCRIPTION, 0);
		table.setColumnExpandRatio(PreviousHospitalizationDto.ISOLATED, 0);

		for (Object columnId : table.getVisibleColumns()) {

			if (columnId.equals(EDIT_COLUMN_ID)) {
				table.setColumnHeader(columnId, "&nbsp");
			} else {
				table.setColumnHeader(columnId, I18nProperties.getPrefixCaption(PreviousHospitalizationDto.I18N_PREFIX, (String) columnId));
			}
		}
	}

	@Override
	protected boolean isEmpty(PreviousHospitalizationDto entry) {

		// has required fields, no empty objects possible
		return false;
	}

	@Override
	protected boolean isModified(PreviousHospitalizationDto oldEntry, PreviousHospitalizationDto newEntry) {

		if (isModifiedObject(oldEntry.getAdmissionDate(), newEntry.getAdmissionDate()))
			return true;
		if (isModifiedObject(oldEntry.getDischargeDate(), newEntry.getDischargeDate()))
			return true;
		if (isModifiedObject(oldEntry.getHealthFacility(), newEntry.getHealthFacility()))
			return true;
		if (isModifiedObject(oldEntry.getIsolated(), newEntry.getIsolated()))
			return true;
		if (isModifiedObject(oldEntry.getDescription(), newEntry.getDescription()))
			return true;

		return false;
	}

	@Override
	protected void editEntry(PreviousHospitalizationDto entry, boolean create, Consumer<PreviousHospitalizationDto> commitCallback) {
		if (create && entry.getUuid() == null) {
			entry.setUuid(DataHelper.createUuid());
		}

		PreviousHospitalizationEditForm editForm = new PreviousHospitalizationEditForm(create, fieldVisibilityCheckers, fieldAccessCheckers);
		editForm.setValue(entry);

		final CommitDiscardWrapperComponent<PreviousHospitalizationEditForm> editView =
			new CommitDiscardWrapperComponent<PreviousHospitalizationEditForm>(
				editForm,
				UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT),
				editForm.getFieldGroup());
		editView.getCommitButton().setCaption(I18nProperties.getString(Strings.done));

		Window popupWindow = VaadinUiUtil.showModalPopupWindow(editView, I18nProperties.getCaption(PreviousHospitalizationDto.I18N_PREFIX));

		editView.addCommitListener(new CommitListener() {

			@Override
			public void onCommit() {
				if (!editForm.getFieldGroup().isModified()) {
					commitCallback.accept(editForm.getValue());
				}
			}
		});

		if (!isEmpty(entry)) {
			editView.addDeleteListener(new DeleteListener() {

				@Override
				public void onDelete() {
					popupWindow.close();
					PreviousHospitalizationsField.this.removeEntry(entry);
				}
			}, I18nProperties.getCaption(PreviousHospitalizationDto.I18N_PREFIX));
		}
	}
}
