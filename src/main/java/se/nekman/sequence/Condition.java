package se.nekman.sequence;

/**
 * 
 * @author nekman
 *
 * @param <T>
 * @param <V>
 */
public interface Condition<T, V> {
	/**
	 * 
	 * @param item
	 * @return
	 */
	V map(final T item);	
}
