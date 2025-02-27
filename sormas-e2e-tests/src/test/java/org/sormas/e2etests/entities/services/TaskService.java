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

package org.sormas.e2etests.entities.services;

import com.github.javafaker.Faker;
import com.google.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import org.sormas.e2etests.entities.pojo.web.Task;
import org.sormas.e2etests.enums.immunizations.StatusValues;

public class TaskService {
  private final Faker faker;

  @Inject
  public TaskService(Faker faker) {
    this.faker = faker;
  }

  public Task buildGeneratedTask() {
    long timeStamp = System.currentTimeMillis();
    return Task.builder()
        .taskContext("GENERAL")
        .taskType("other task as described in comments")
        .suggestedStartDate(LocalDate.now())
        .suggestedStartTime(LocalTime.of(11, 30))
        .dueDateDate(LocalDate.now().plusDays(1))
        .dueDateTime(LocalTime.of(11, 30))
        .assignedTo("Case SUPERVISOR")
        .priority("Normal")
        .commentsOnTask("Task comment-" + timeStamp)
        .commentsOnExecution("Execution comment-" + timeStamp)
        .taskStatus(StatusValues.PENDING.getValue())
        .build();
  }

  public Task buildGeneratedTaskDE() {
    long timeStamp = System.currentTimeMillis();
    return Task.builder()
        .taskContext("GENERAL")
        .taskType("Andere Aufgabe (siehe Kommentar)")
        .suggestedStartDate(LocalDate.now())
        .suggestedStartTime(LocalTime.of(11, 30))
        .dueDateDate(LocalDate.now().plusDays(1))
        .dueDateTime(LocalTime.of(11, 30))
        .assignedTo("Ad MIN")
        .priority("Normal")
        .commentsOnTask("Task comment-" + timeStamp)
        .commentsOnExecution("Execution comment-" + timeStamp)
        .taskStatus(StatusValues.PENDING.getValue())
        .build();
  }

  public Task buildEditTask(String currentTaskContext, String currentStatus) {
    long timeStamp = System.currentTimeMillis();
    return Task.builder()
        .taskContext(currentTaskContext)
        .taskType("contact tracing")
        .suggestedStartDate(LocalDate.now().plusDays(1))
        .suggestedStartTime(LocalTime.of(12, 30))
        .dueDateDate(LocalDate.now().plusDays(2))
        .dueDateTime(LocalTime.of(13, 30))
        .assignedTo("Surveillance OFFICER")
        .priority("High")
        .commentsOnTask("Task comment-" + timeStamp)
        .commentsOnExecution("Execution comment-" + timeStamp)
        .taskStatus(currentStatus)
        .build();
  }

  public Task buildGeneratedTaskForEvent() {
    return buildGeneratedTask().toBuilder()
        .taskContext("EVENT")
        .taskType("vaccination activities")
        .assignedTo("National USER")
        .build();
  }
}
