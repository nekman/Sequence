package se.nekman.sequence.utils;

import se.nekman.sequence.selectors.Predicate;
import se.nekman.sequence.selectors.Condition;

public class TestUtils {

	public static final Predicate<Integer> biggerThanFive = new Predicate<Integer>() {					
		@Override
		public boolean match(Integer item) {
			return item > 5;
		}
	};
	
	public static final Condition<Integer, String> intToString = new Condition<Integer, String>() {
		@Override
		public String map(Integer item) {
			return String.valueOf(item);
		}							
	};
}
