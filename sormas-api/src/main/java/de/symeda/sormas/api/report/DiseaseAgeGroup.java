/*
 *  SORMAS® - Surveillance Outbreak Response Management & Analysis System
 *  Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package de.symeda.sormas.api.report;

import java.io.Serializable;
import java.util.Objects;

public class DiseaseAgeGroup implements Serializable {

	private String disease;
	private String ageGroup;

	public DiseaseAgeGroup(String disease, String ageGroup) {
		this.disease = disease;
		this.ageGroup = ageGroup;
	}

	public String getDisease() {
		return disease;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DiseaseAgeGroup that = (DiseaseAgeGroup) o;
		return Objects.equals(disease, that.disease) && Objects.equals(ageGroup, that.ageGroup);
	}

	@Override public int hashCode() {
		return Objects.hash(disease, ageGroup);
	}
}
