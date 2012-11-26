package se.nekman.sequence;

public class TestHelper {

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
