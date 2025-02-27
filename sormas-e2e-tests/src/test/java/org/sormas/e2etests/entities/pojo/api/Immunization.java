/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.sormas.e2etests.entities.pojo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderClassName = "builder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Immunization {
  String uuid;
  Boolean pseudonymized;
  String disease;
  Person person;
  Long reportDate;
  Long positiveTestResultDate;
  Long recoveryDate;
  Long startDate;
  Long endDate;
  String externalId;
  String reportingUser;
  Boolean archived;
  String immunizationStatus;
  String meansOfImmunization;
  String immunizationManagementStatus;
  String responsibleRegion;
  String responsibleDistrict;
  String responsibleCommunity;
}
