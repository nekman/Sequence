package se.nekman.sequence;

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
	 * @return true if matches.
	 */
	boolean match(final T item);
}
