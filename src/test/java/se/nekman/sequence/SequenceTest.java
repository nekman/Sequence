package se.nekman.sequence;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static se.nekman.sequence.Sequence.from;
import static se.nekman.sequence.utils.TestUtils.biggerThanFive;
import static se.nekman.sequence.utils.TestUtils.intToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import se.nekman.sequence.exceptions.EmptySequenceException;
import se.nekman.sequence.selectors.Action;

public class SequenceTest {

	private Sequence<Integer> sequence;

	@Before
	public void setup() {		
		sequence = from(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}
	
	@Test
	public void itShouldReturnOneElementInSequenceAfterTakingOne() {
		List<Integer> first = sequence
				.take(1)
				.toList();
		
		assertThat(first.size(), is(1));
		assertThat(first.get(0), is(1));
	}
	
	@Test
	public void itShouldReturnZeroElementList() {
		List<Integer> first = sequence
				.take(0)
				.toList();
		
		assertThat(first.size(), is(0));		
	}
	
	@Test
	public void itShouldFilterNumberBiggerThanFive() {
		Integer[] items = sequence
				.filter(biggerThanFive)
				.toArray();
		
		assertThat(items.length, is(5));		
	}
	
	@Test
	public void itShouldMapToStrings() throws EmptySequenceException {
		Sequence<String> items = sequence
				.map(intToString);
		
		assertThat(items.count(), is(10));
		assertThat(items.first(), is("1"));
		assertThat(items.last(), is("10"));
	}
	
	@Test
	public void itShouldMuliplyNumbersWithForeach() throws EmptySequenceException {		
		Sequence<AtomicInteger> all = from(new AtomicInteger(10), new AtomicInteger(20))
				.forEach(new Action<AtomicInteger>() {					
					@Override
					public void execute(AtomicInteger item) {
						item.set(item.get()*2);
					}
				});
				
		
		assertThat(all.count(), is(2));
		assertThat(all.first().intValue(), is(20));
		assertThat(all.last().intValue(), is(40));
	}
	
	@Test
	public void itShouldGenerateRange() throws EmptySequenceException {
		Sequence<Integer> range = from()
				.range(1, 1000);
		
		assertThat(range.count(), is(1000));
		assertThat(range.first(), is(1));
		assertThat(range.last(), is(1000));
	}
	
	@Test
	public void itShouldHandleEmpty() throws EmptySequenceException {
		Sequence<String> seq = from()
				.range(1, 10)
				.map(intToString);
	
		assertThat(seq.firstOrDefault(), is("1"));
	}
	
	@Test
	public void itShouldTakeAll() {
		List<Integer> all = sequence
				.take(1000)
				.toList();
		
		assertThat(all.size(), is(10));
	}

	@Test
	public void itShouldSkipTheFirstTwoItems() {
		List<Integer> allButThefirstTwo = sequence
				.skip(2)
				.toList();
		
		int firstElement = allButThefirstTwo.get(0);
		
		assertThat(firstElement, is(3));
	}
	
	@Test
	public void itSkipTheFirstTwoItemsAndTakeTheNext() {
		List<Integer> result = sequence
				.skip(2)
				.take(1)
				.toList();
		
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is(3));
	}
	
	@Test(timeout=100)
	public void itShouldSkipOneAtTheTime() throws EmptySequenceException {
		Sequence<String> items = from("one","two","three");		
		int counter = 0;
		
		while((items = items.skip(counter++)).count() > 0);
		
		assertThat(items.count(), is(0));
		assertThat(counter, is(3));
	}
	
	@Test(timeout=100)
	public void itShouldTakeOneAtTheTime() throws EmptySequenceException {
		Sequence<String> items = from("one","two","three");		
		int counter = 3;
		
		while((items = items.take(counter--)).count() > 0);
		
		assertThat(items.count(), is(0));
		assertThat(items.firstOrDefault(), is(nullValue()));
	}
	
	@Test
	public void itShouldConcatTwoCollections() throws EmptySequenceException {
		Sequence<String> items = from("one","two")
				.concat("three", "four", "five");
		
		assertThat(items.count(), is(5));
		assertThat(items.first(), is("one"));
		assertThat(items.last(), is("five"));
	}
	
	@Test
	public void itShouldConcatNullCollection() throws EmptySequenceException {
		Sequence<String> items = from("one","two")
				.concat((String)null);
		
		assertThat(items.count(), is(3));		
		assertThat(items.last(), is(nullValue()));
	}
	
	@Test
	public void itShouldReturnTheFirstElement() throws EmptySequenceException  {
		String result = from("one","two","three")
				.skip(2)
				.first();
				
		assertThat(result, is("three"));
	}
	
	@Test
	public void itShouldConvertToArray() {
		int expected = sequence.count();
		Integer[] numbers = sequence.toArray();
		
		assertThat(numbers.length, is(expected));
		assertThat(1, is(numbers[0]));
		assertThat(10, is(numbers[9]));
	}
	
	@Test
	public void itShouldHaveAnyElements() {
		assertTrue(sequence.any());
		assertTrue(sequence.any(biggerThanFive));
	}
	
	@Test
	public void itShouldTakeElementsOfType() {
		Sequence<Object> seq = from(1, "string", 1.0f, new Object());
		
		assertThat(seq.ofType(Integer.class).count(), is(1));
		assertThat(seq.ofType(String.class).count(), is(1));
		assertThat(seq.ofType(Float.class).count(), is(1));
		assertThat(seq.ofType(byte.class).count(), is(0));
	}
	
	@Test
	public void itShouldIterateOverElementsOfTypeString() {
		@SuppressWarnings("serial")
		Properties p = new Properties() {{
			put("s1", "");
			put(1111, "");
			put("s2", "");
			put("s3", "");
			put(2222, "");
		}};
		
		for (String s : from(p.keySet()).ofType(String.class)) {
			assertThat(s, isA(String.class));
		}
	}
	
	@Test(expected=EmptySequenceException.class)
	public void itShouldThrowEmptySequenceErrorWhenGettingFirst() throws EmptySequenceException {
		from("one","two","three")
		.skip(5)
		.first();		
	}
	
	@Test(expected=EmptySequenceException.class)
	public void itShouldThrowEmptySequenceErrorWhenGettingLast() throws EmptySequenceException {
		from("one","two","three")
		.skip(5)
		.last();		
	}
}
