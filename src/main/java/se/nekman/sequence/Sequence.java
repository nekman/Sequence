package se.nekman.sequence;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import se.nekman.sequence.exceptions.EmptySequenceException;
import se.nekman.sequence.selectors.Action;
import se.nekman.sequence.selectors.Predicate;
import se.nekman.sequence.selectors.Func;
import se.nekman.sequence.validators.Assert;

/**
 * Util class for collections Inspired by LINQ in C# - http://msdn.microsoft.com/en-us/library/bb341635.aspx
 * NOTE: This is just a test/lab version and it may contain bugs.
 * 
 * @author nekman
 *
 * @param <T>
 */
public class Sequence<T> implements Iterable<T> {
	
	/**
	 * The original source.
	 */
	private final Collection<T> source;
	
	/**
	 * Initialize a new sequence.
	 * 
	 * @param source
	 */
	public Sequence(final Collection<T> source) {
		if (source == null) {
			this.source = Collections.emptyList();
			return;
		}
		
		this.source = source;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public Iterator<T> iterator() {
		return source.iterator();
	}
	
	/**
	 * Initialize a new sequence.
	 * 
	 * @param source
	 * @return
	 */
	public static <T> Sequence<T> from(final Collection<T> source) {
		return new Sequence<T>(source);
	}
	
	/**
	 * Initialize a new sequence. 
	 * 
	 * @param source
	 * @return
	 */
	public static <T> Sequence<T> from(final T... source) {
		return from(Arrays.asList(source));
	}
	
	/**
	 * Concatenates two sequences.
	 * 
	 * @param items
	 * @return
	 */
	public Sequence<T> concat(final Collection<T> items) {			
		final Iterator<T> it = from(items).iterator();
		while (!it.hasNext()) {
			return this;
		}
		
		// Cannot add to the source field, due to AbstractList.add will throw UnsupportedException.
		final List<T> newList = from(source).toList();		
		while (it.hasNext()) {
			newList.add(it.next());
		}
		
		return from(newList);	
	}
	
	/**
	 * Concatenates two sequences.
	 * 
	 * @param items
	 * @return
	 */
	public Sequence<T> concat(final T... items) {
		return concat(Arrays.asList(items));
	}
	
	/**
	 * Determines whether a sequence contains any elements.
	 * 
	 * @return
	 */
	public boolean any() {
		return iterator().hasNext();
	}
	
	/**
	 * Determines whether all elements of a sequence satisfy a predicate.
	 * 
	 * @param predicate - the action
	 * @return
	 */
	public boolean all(final Predicate<T> predicate) {
		Assert.notNull(predicate, "predicate");
		
		for (final T item : this) {
			if (predicate.match(item)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Determines whether any element of a sequence satisfies a predicate.
	 * 
	 * @param predicate
	 * @return
	 */
	public boolean any(final Predicate<T> predicate) {
		Assert.notNull(predicate, "predicate");
		
		final Iterator<T> it = iterator();		
		while (it.hasNext()) {
			final T item = it.next();
			if (predicate.match(item)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Filters a sequence of values based on a predicate.
	 * 
	 * @param predicate the predicate to match.
	 * @return
	 */
	public Sequence<T> filter(final Predicate<T> predicate) {
		Assert.notNull(predicate, "predicate");
		
		final Collection<T> items = new ArrayList<T>();
		for (final T item : this) {
			if (predicate.match(item)) {
				items.add(item);
			}
		}
		
		return from(items);
	}
	
	/**
	 * Same as {@link #filter(Predicate)}
	 * @see {@link #map(Predicate)}
	 */
	public Sequence<T> where(final Predicate<T> predicate) {
		return filter(predicate);
	}
		
	/**
	 * Projects each element of a sequence into a new form.
	 * 
	 * @param func the func
	 * @return
	 */
	public <TResult> Sequence<TResult> map(final Func<T, TResult> func) {
		Assert.notNull(func, "func");
		
		final Collection<TResult> items = new ArrayList<TResult>();
		for (final T item : this) {
			items.add(func.map(item));		
		}
		
		return from(items);
	}
	
	/**
	 * Same as {@link #map(Func)}
	 * @see {@link #map(Func)}
	 * 
	 * @param func the func
	 * @return
	 */
	public <TResult> Sequence<TResult> select(final Func<T, TResult> func) {
		return map(func);
	}
	
	/**
	 * Bypasses a specified number of elements in a sequence and then returns the remaining elements.
	 * 
	 * @param count numbers to bypass
	 * @return
	 */
	public Sequence<T> skip(int count) {
		final Iterator<T> it = iterator();
		
		if (!it.hasNext()) {
			return empty();
		}
		
		while (count-- > 0) {
			 if (!it.hasNext()) {				 
				 return empty();
			 }
			 it.next();
		}
		
		final Collection<T> items = new ArrayList<T>();			
		while (it.hasNext()) {		
			items.add(it.next());
		}
		
		return from(items);
	}
	
	/**
	 * Gets elements from the start of a sequence.
	 * 
	 * @param start where in the sequence to start
	 * @return elements from the start of a sequence
	 */
	public Sequence<T> take(final int start) {				
		if (start <= 0) {
			return empty();
		}
		
		final Iterator<T> it = iterator();
		final Collection<T> items = new ArrayList<T>();		
		int itemsAdded = 0;
		while (it.hasNext()) {
			if (itemsAdded++ == start) {
				break;
			}
						
			items.add(it.next());					
		}
		
		return from(items);
	}
	
	/**
	 * 
	 * @return the number of elements in the sequence.
	 */
	public int count() {
		return source.size();
	}

	/**
	 * The last element of a sequence.
	 * 
	 * @return the last element of a sequence. 
	 * @throws EmptySequenceException if the sequence is empty.
	 */
	public T last() throws EmptySequenceException {	
		final List<T> items = toList();
		final int size = items.size();
		if (size < 1) {
			throw new EmptySequenceException();
		}
		
		return items.get(size - 1);
	}
	
	/**
	 * The last element of a sequence, or default if the sequence is empty.
	 * 
	 * @return the last element of a sequence. 
	 */
	public T lastOrDefault() {
		final List<T> items = toList();
		final int size = items.size();
		
		return size < 1 ? null : items.get(size -1);
	}

	/**
	 * The first element of a sequence, or default if the sequence is empty.
	 * 
	 * @return the first element of a sequence. 	 
	 */
	public T firstOrDefault() {
		final List<T> items = toList();
		
		return items.size() < 1 ? null : items.get(0);
	}
	
	/**
	 * The first element of a sequence.
	 * 
	 * @return the first element of a sequence. 	 
	 * @throws EmptySequenceException if the sequence is empty. 
	 */
	public T first() throws EmptySequenceException {
		final List<T> items = toList();
		
		if (items.size() < 1) {
			throw new EmptySequenceException();
		}
		
		return items.get(0);
	}
	
	/**
	 * Generates a sequence of integral numbers within a specified range.
	 * 
	 * @param start
	 * @param count
	 * @return
	 */
	public Sequence<Integer> range(final int start, final int count) {		
		final List<Integer> items = new ArrayList<Integer>();
		for (int index = start; index <= count; index++) {
			items.add(index);
		}
		
		return new Sequence<Integer>(items);
	}
	
	/**
	 * Returns the sequence as an List<T>.
	 * 
	 * @return
	 */
	public List<T> toList() {
		return new ArrayList<T>(source);
	}	
	
	/**
	 * Returns the sequence as an Collection<T>.
	 * 
	 * @return
	 */
	public Collection<T> toCollection() {
		return source;
	}
	
	/**
	 * Returns the sequence as an array.
	 * 
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		final T first = firstOrDefault();
		final Object item = first != null ? first : new Object();
		
		return (T[]) source.toArray((T[])Array.newInstance(item.getClass(), count()));
	}
	
	/**
	 * Returns the sequence as a Set.
	 * 
	 * @return
	 */	
	public Set<T> toSet() {
		return new HashSet<T>(source);
	}
	
	/**
	 * Performs the specified action on each element in the sequence.
	 * 
	 * @param action - the action
	 * @return
	 */
	public Sequence<T> forEach(final Action<T> action) {
		Assert.notNull(action, "action");
		
		for (final T item : this) {
			action.execute(item);
		}
		
		return this;
	}
	
	/**
	 * Filters the elements of an sequence based on a specified type.
	 * 
	 * @param action - the action
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public <TType> Sequence<TType> ofType(final Class<TType> clazz) {	
		Assert.notNull(clazz, "class");
		
		final List<TType> items = new ArrayList<TType>();
		for (final T item : this) {
			if (clazz.isInstance(item)) {
				items.add((TType) item);
			}
		}
		
		return from(items);
	}
	
	/**
	 * 
	 * @return
	 */
	private static <T> Sequence<T> empty() {
		return new Sequence<T>(null);
	}		
}