package de.symeda.sormas.api;

/**
 * This class hides the one in sormas-api.
 * 
 * @deprecated FacadeProvider should not be used by the business logic because the facades can be accessed locally
 */
@Deprecated
public final class FacadeProvider {

	private FacadeProvider() {
		//NOOP
	}

	public static void doNotUseInBackend() {
		throw new java.lang.IllegalStateException();
	}
}
