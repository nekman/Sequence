package se.nekman.sequence.validators;

/**
 * Assertion utility class that assists in validating arguments. 
 * Useful for identifying programmer errors early and clearly at runtime.
 * 
 * @author nekman
 *
 */
public class Assert {
	/**
	 * Throws IllegalArgumentException if the submitted object is null.
	 * 
	 * @param objectToCheck
	 * @param parameterName
	 */
	public static void notNull(final Object objectToCheck, final String parameterName) {
		if (objectToCheck == null) {
			throw new IllegalArgumentException(parameterName + " cannot be null");
		}
	}
}
