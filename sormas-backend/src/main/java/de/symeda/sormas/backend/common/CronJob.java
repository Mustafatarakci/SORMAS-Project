package de.symeda.sormas.backend.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.symeda.sormas.api.utils.DateHelper;

/**
 * Diese Oberklasse bietet folgende Funktionen:
 * <ul>
 * <li>Prüfung auf aktivierte automatische Ausführung ({@link #isJobEnabled()})</li>
 * <li>Exception Handling: Ein auftretender Fehler lässt den Job nicht explungen.</li>
 * <li>Logging: Start, Ende, Fehler, Zeitmessung</li>
 * </ul>
 * 
 * @author Stefan Kock
 */
public abstract class CronJob {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Startet die Ausführung des Jobs (inkl. Prüfung, ob die automatische Ausführung aktiviert ist).
	 */
	public void run() {

		final long startTime = DateHelper.startTime();
		logger.info("Started ...");

		try {
			if (isJobEnabled()) {

				final Integer count = execute();
				final long duration = DateHelper.durationSeconds(startTime);

				if (count == null) {
					// Es liegt keine Information über Anzahl verarbeiteter Elemente vor
					logger.info("Erfolgreich beendet. {} s", duration);
				} else {
					logger.info("Erfolgreich beendet. n: {}, {} s", count, duration);
				}
			} else {
				logger.info("Abbruch: Jobausführung ist nicht freigeschaltet.");
			}
		} catch (RuntimeException e) {
			/*
			 * Damit beim Fehlschlag der Methode der Timer nicht deaktiviert wird,
			 * wird hier eine RuntimeException gefangen und nicht weitergeworfen.
			 * Die Exception stammt von einer mit @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
			 * annotierten Methode.
			 * Im Logger wird der Fehler protokolliert.
			 */
			logger.error("Unerwarteter Fehler, Job abgebrochen.", e);
		}
	}

	/**
	 * Prüft, ob die automatische Ausführung des Jobs aktiviert ist.
	 * 
	 * @return {@code true}, wenn die automatische Ausführung des Jobs aktiviert ist.
	 */
	protected abstract boolean isJobEnabled();

	/**
	 * Beinhaltet die auszuführende Logik für einen bestimmten Job.<br />
	 * Diese Methode wird nur aufgerufen, wenn <code>{@link #isJobEnabled()} == true</code> gilt.<br />
	 * <strong>Achtung:</strong> Die hier aufgerufene Logik muss mit
	 * <code>@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)</code> annotiert werden,
	 * damit ein Rollback der Methode den Timer nicht deaktiviert.
	 * 
	 * @return Number of processed elements, {@code null} if there is no information about processed elements
	 */
	protected abstract Integer execute();
}
