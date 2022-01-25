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

package de.symeda.sormas.backend.caze.caseaccess;

import static de.symeda.sormas.backend.access.AbstractEntityAccess.ACCESS_TABLE_SUFFIX;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.access.AbstractEntityAccess;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.sample.Sample;

@Entity(name = Case.TABLE_NAME + ACCESS_TABLE_SUFFIX)
public class CaseAccess extends AbstractEntityAccess {

	private static final long serialVersionUID = 4990678419109648779L;

	public static final String TABLE_NAME = Case.TABLE_NAME + ACCESS_TABLE_SUFFIX;

	public static final String CAZE = "caze";
	public static final String CONTACT = "contact";
	public static final String SAMPLE = "sample";

	private Case caze;
	private Contact contact;
	private Sample sample;

	public static CaseAccess build(Case caze, boolean primaryData, boolean pseudonymized) {
		CaseAccess access = new CaseAccess();
		access.setPrimaryData(primaryData);
		access.setCaze(caze);
		access.setPseudonymized(pseudonymized);
		access.setUuid(DataHelper.createUuid());
		access.setCreationDate(new Timestamp(new Date().getTime()));

		return access;
	}

	@ManyToOne
	public Case getCaze() {
		return caze;
	}

	public void setCaze(Case caze) {
		this.caze = caze;
	}

	@ManyToOne
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@ManyToOne
	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
