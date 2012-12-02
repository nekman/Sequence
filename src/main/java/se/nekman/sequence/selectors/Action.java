package se.nekman.sequence.selectors;

/**
 * 
 * @author nekman
 *
 * @param <T>
 */
public interface Action<T> {
	/**
	 * 
	 * @param item
	 */
	void execute(final T item);
}
