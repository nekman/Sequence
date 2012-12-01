package se.nekman.sequence.selectors;

import org.hamcrest.Matcher;

import se.nekman.sequence.validators.Assert;

/**
 * Should be imported static, to to be able to create easy predicates by 
 * using a {@link org.hamcrest.Matcher}.
 * 
 * @author nekman
 *
 */
public class PredicateFactory {
	/**
	 * 
	 * @param matcher
	 * @return a Predicate from a {@link org.hamcrest.Matcher}
	 */
	public static <T> Predicate<T> create(final Matcher<T> matcher) {
		Assert.notNull(matcher, "matcher");
		
		return new Predicate<T>() {
			@Override
			public boolean match(final T item) {
				return matcher.matches(item);
			}
		};
	}
}
