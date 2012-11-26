package se.nekman.sequence;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import se.nekman.sequence.exceptions.EmptySequenceException;

/**
 * Util class for collections Inspired by LINQ in C# - http://msdn.microsoft.com/en-us/library/bb341635.aspx
 *
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
	
	private Sequence(final Collection<T> source) {
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
	 * The from method, initialize a new sequence.
	 * 
	 * @param source
	 * @return
	 */
	public static <T> Sequence<T> from(final Collection<T> source) {
		return new Sequence<T>(source);
	}
	
	/**
	 * The from method, initialize a new sequence. 
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
	 * 
	 * 
	 * @param predicate the predicate to match.
	 * @return
	 */
	public Sequence<T> filter(final Predicate<T> predicate) {
		final Collection<T> items = new ArrayList<T>();
		for (final T item : source) {
			if (predicate.match(item)) {
				items.add(item);
			}
		}
		
		return from(items);
	}
	
	/**
	 * 
	 * 
	 * @param condition the condition
	 * @return
	 */
	public <V> Sequence<V> map(final Condition<T, V> condition) {
		final Collection<V> items = new ArrayList<V>();
		for (final T item : source) {
			items.add(condition.map(item));		
		}
		
		return from(items);
	}
	
	/**
	 * Bypasses a specified number of elements in a sequence and then returns the remaining elements.
	 * 
	 * @param count numbers to bypass
	 * @return
	 */
	public Sequence<T> skip(int count) {
		final Iterator<T> it = source.iterator();
		
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
		final Iterator<T> it = source.iterator();
		if (start <= 0) {
			return empty();
		}
		
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
	 * The last element of a sequence.
	 * 
	 * @return the last element of a sequence. 
	 * @throws EmptySequenceException if the sequence is empty.
	 */
	public T lastOrDefault() {
		final List<T> items = toList();
		final int size = items.size();
		if (size < 1) {
			//TODO: return null?
			return null;
		}
		
		return items.get(size - 1);
	}

	/**
	 * The first element of a sequence.
	 * 
	 * @return the first element of a sequence. 	 
	 */
	public T firstOrDefault() {
		final List<T> items = toList();
		
		if (items.size() < 1) {
			//TODO: return null?
			return (T)null;
		}
		
		return items.get(0);
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
		final T one = firstOrDefault();
		return (T[]) source.toArray((T[])Array.newInstance(one.getClass(), count()));
	}
	
	/**
	 * 
	 * @return
	 */
	private static <T> Sequence<T> empty() {
		return new Sequence<T>(null);
	}	
}