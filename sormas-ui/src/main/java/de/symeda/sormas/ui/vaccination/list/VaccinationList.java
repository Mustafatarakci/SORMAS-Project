/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.ui.vaccination.list;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.vaadin.ui.Label;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.vaccination.VaccinationListEntryDto;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.utils.PaginationList;

public class VaccinationList extends PaginationList<VaccinationListEntryDto> {

	private static final int MAX_DISPLAYED_ENTRIES = 5;

	private Disease disease;
	private final Function<Integer, List<VaccinationListEntryDto>> vaccinationListSupplier;
	private final Consumer<Runnable> actionCallback;

	public VaccinationList(
		Disease disease,
		Function<Integer, List<VaccinationListEntryDto>> vaccinationListSupplier,
		Consumer<Runnable> actionCallback) {
		super(MAX_DISPLAYED_ENTRIES);
		this.vaccinationListSupplier = vaccinationListSupplier;
		this.disease = disease;
		this.actionCallback = actionCallback;
	}

	@Override
	public void reload() {
		List<VaccinationListEntryDto> list = vaccinationListSupplier.apply(maxDisplayedEntries * 20);
		setEntries(list);
		if (!list.isEmpty()) {
			showPage(1);
		} else {
			updatePaginationLayout();
			Label noVaccinationsLabel = new Label(
				I18nProperties.getCaption(
					disease != null ? Captions.vaccinationNoVaccinationsForPersonAndDisease : Captions.vaccinationNoVaccinationsForPerson));
			listLayout.addComponent(noVaccinationsLabel);
		}
	}

	@Override
	protected void drawDisplayedEntries() {
		for (VaccinationListEntryDto entryDto : getDisplayedEntries()) {
			VaccinationListEntry listEntry = new VaccinationListEntry(entryDto, disease == null);
			listEntry.addEditButton(
				"edit-vaccination-" + listEntry.getVaccination().getUuid(),
				e -> actionCallback.accept(
					() -> ControllerProvider.getVaccinationController()
						.edit(
							FacadeProvider.getVaccinationFacade().getByUuid(listEntry.getVaccination().getUuid()),
							listEntry.getVaccination().getDisease(),
							UiFieldAccessCheckers.getDefault(listEntry.getVaccination().isPseudonymized()),
							true,
							v -> SormasUI.refreshView(),
							deleteCallback())));
			listLayout.addComponent(listEntry);
		}
	}
}
