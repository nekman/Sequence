package se.nekman.sequence.selectors;

/**
 * 
 * @author nekman
 *
 * @param <TSource>
 * @param <TResult>
 */
public interface Func<TSource, TResult> {
	/**
	 * 
	 * @param item
	 * @return
	 */
	TResult map(final TSource item);	
}
