package se.nekman.collectionutils;

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
