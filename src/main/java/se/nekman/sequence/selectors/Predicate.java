package se.nekman.sequence.selectors;

/**
 * 
 * @author nekman
 *
 * @param <T>
 */
public interface Predicate<T> {
	/**
	 * 
	 * @param item
	 * @return true if match.
	 */
	boolean match(final T item);
}
