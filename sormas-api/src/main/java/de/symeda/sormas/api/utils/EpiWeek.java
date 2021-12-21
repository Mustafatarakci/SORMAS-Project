package de.symeda.sormas.api.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;

@SuppressWarnings("serial")
public class EpiWeek implements Serializable, Comparable<EpiWeek>, StatisticsGroupingKey {

	private final Integer year;
	private final Integer week;

	public EpiWeek(Integer year, Integer week) {
		this.year = year;
		this.week = week;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getWeek() {
		return week;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EpiWeek other = (EpiWeek) obj;
		if (week == null) {
			if (other.week != null)
				return false;
		} else if (!week.equals(other.week))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(year, week);
	}

	@Override
	public String toString() {
		return toString(I18nProperties.getUserLanguage());
	}

	public String toString(Language language) {

		return I18nProperties.getString(Strings.weekShort) + " " + week
			+ (year != null
				? ("-" + year + " (" + DateHelper.formatDateWithoutYear(DateHelper.getEpiWeekStart(this), language) + " - "
					+ DateHelper.formatDateWithoutYear(DateHelper.getEpiWeekEnd(this), language) + ")")
				: "");
	}

	/**
	 * Returns a human-readable representation of the epi week, starting from the first day of the week until the specified end date. This
	 * should ideally lay within the same week.
	 */
	public String toString(Date endDate, Language language) {

		return I18nProperties.getString(Strings.weekShort) + " " + week
			+ (year != null
				? ("-" + year + " (" + DateHelper.formatDateWithoutYear(DateHelper.getEpiWeekStart(this), language) + " - "
					+ DateHelper.formatLocalDate(endDate, language) + ")")
				: "");
	}

	/**
	 * Returns a human-readable representation of the epi week, starting from the first day of the week for as many days as specified by epi
	 * week length.
	 */
	public String toString(int epiWeekLength, Language language) {

		return I18nProperties.getString(Strings.weekShort) + " " + week
			+ (year != null
				? ("-" + year + " (" + DateHelper.formatDateWithoutYear(DateHelper.getEpiWeekStart(this), language) + " - "
					+ DateHelper.formatLocalDate(DateHelper.addDays(DateHelper.getEpiWeekStart(this), epiWeekLength), language) + ")")
				: "");
	}

	public String toShortString() {
		return I18nProperties.getString(Strings.weekShort) + " " + week + (year != null ? ("-" + year) : "");
	}

	public String toUrlString() {
		return "year-" + year + "-week-" + week;
	}

	public static EpiWeek fromUrlString(String urlString) {

		int dashIndex = urlString.indexOf("-");
		int year = Integer.valueOf(urlString.substring(dashIndex + 1, urlString.indexOf("-", dashIndex + 1)));
		int week = Integer.valueOf(urlString.substring(urlString.lastIndexOf("-") + 1));
		return new EpiWeek(year, week);
	}

	@Override
	public int keyCompareTo(StatisticsGroupingKey o) {

		EpiWeek other = (EpiWeek) o;
		if (other == null) {
			throw new NullPointerException("Can't compare to null.");
		}
		if ((this.getYear() == null && other.getYear() != null) || (this.getYear() != null && other.getYear() == null)) {
			throw new UnsupportedOperationException("Can't compare an epi week with a year to an epi week without a year");
		}

		if (this.equals(other)) {
			return 0;
		}
		if (this.getYear() != null) {
			if (this.getYear() < other.getYear()) {
				return -1;
			} else if (this.getYear().equals(other.getYear()) && this.getWeek() < other.getWeek()) {
				return -1;
			} else {
				return 1;
			}
		} else {
			if (this.getWeek() < other.getWeek()) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	@Override
	public int compareTo(EpiWeek o) {
		return keyCompareTo(o);
	}
}
