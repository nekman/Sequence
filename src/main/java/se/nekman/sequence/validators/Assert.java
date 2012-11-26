package se.nekman.sequence.validators;

/**
 * 
 * @author nekman
 *
 */
public class Assert {
	/**
	 * Throws IllegalArgumentException if the submited object is null.
	 * 
	 * @param o
	 * @param name
	 */
	public static void notNull(final Object o, final String name) {
		if (o == null) {
			throw new IllegalArgumentException(name + " cannot be null");
		}
	}
}
