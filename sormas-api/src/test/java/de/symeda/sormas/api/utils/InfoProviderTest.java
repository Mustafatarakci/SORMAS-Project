/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
 *******************************************************************************/
package de.symeda.sormas.api.utils;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class InfoProviderTest {

	@Before
	public void prepareTest() {

		try {
			Field instance = InfoProvider.class.getDeclaredField("instance");
			instance.setAccessible(true);
			instance.set(null, spy(InfoProvider.class));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsCompatibleToApiString() {

		Mockito.when(InfoProvider.get().getVersion()).thenReturn("0.7.0");
		Mockito.when(InfoProvider.get().getMinimumRequiredVersion()).thenReturn("0.5.0");

		// testMatchingVersionCompatibility
		assertEquals(CompatibilityCheckResponse.COMPATIBLE, InfoProvider.get().isCompatibleToApi("0.5.0"));
		assertEquals(CompatibilityCheckResponse.COMPATIBLE, InfoProvider.get().isCompatibleToApi("0.7.0"));

		// testHotfixVersionCompatibility
		assertEquals(CompatibilityCheckResponse.COMPATIBLE, InfoProvider.get().isCompatibleToApi("0.5.99"));

		// testTooOldVersionIncompatibility
		assertEquals(CompatibilityCheckResponse.TOO_OLD, InfoProvider.get().isCompatibleToApi("0.4.0"));
		assertEquals(CompatibilityCheckResponse.TOO_OLD, InfoProvider.get().isCompatibleToApi("0.0.7"));

		// testTooNewVersionIncompatibility
		assertEquals(CompatibilityCheckResponse.TOO_NEW, InfoProvider.get().isCompatibleToApi("0.7.1"));
		assertEquals(CompatibilityCheckResponse.TOO_NEW, InfoProvider.get().isCompatibleToApi("0.8.0"));
		assertEquals(CompatibilityCheckResponse.TOO_NEW, InfoProvider.get().isCompatibleToApi("1.0.0"));

		// testMalformedVersionReturnsError
		try {
			InfoProvider.get().isCompatibleToApi("1.0");
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			InfoProvider.get().isCompatibleToApi("wrong.format.test");
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testCreateLastCommitHistoryUrl() {

		String commitFullId = "eb4364e0cea411d90c772a7b0168eee732f8bc87";
		String expectedUrl = "https://github.com/hzi-braunschweig/SORMAS-Project/commits/eb4364e0cea411d90c772a7b0168eee732f8bc87";

		// 1. HTTPS git checkout
		String httpsOriginUrl = "https://github.com/hzi-braunschweig/SORMAS-Project.git";
		assertThat(InfoProvider.createLastCommitHistoryUrl(httpsOriginUrl, commitFullId), equalTo(expectedUrl));

		// 2. SSH git checkout
		String sshOriginUrl = "git@github.com:hzi-braunschweig/SORMAS-Project.git";
		assertThat(InfoProvider.createLastCommitHistoryUrl(sshOriginUrl, commitFullId), equalTo(expectedUrl));
	}

	@Test
	public void testGetLastCommitShortId() {

		InfoProvider cut = InfoProvider.get();
		assertThat(cut.getLastCommitShortId(), hasLength(7));
	}

	@Test
	public void testGetLastCommitHistoryUrl() {

		InfoProvider cut = InfoProvider.get();
		assertThat(cut.getLastCommitHistoryUrl(), startsWith("https://"));
		assertThat(cut.getLastCommitHistoryUrl(), containsString(cut.getLastCommitShortId()));
	}

	@Test
	public void testIsSnapshotVersion() {

		InfoProvider cut = InfoProvider.get();
		assertThat(cut.isSnapshotVersion(), equalTo(cut.getVersion().endsWith("SNAPSHOT")));
	}

	@Test
	public void testIsSnapshot() {

		InfoProvider cut = InfoProvider.get();
		assertTrue(cut.isSnapshot("1.69.0-SNAPSHOT"));
		assertTrue(cut.isSnapshot("1.69.1-SNAPSHOT"));
		assertFalse(cut.isSnapshot("1.69.0"));
		assertFalse(cut.isSnapshot("1.69.1"));
	}
}
