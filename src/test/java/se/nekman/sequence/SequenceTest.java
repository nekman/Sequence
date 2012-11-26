package se.nekman.sequence;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static se.nekman.sequence.TestHelper.*;
import static se.nekman.sequence.Sequence.from;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

import se.nekman.sequence.Sequence;
import se.nekman.sequence.exceptions.EmptySequenceException;

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
