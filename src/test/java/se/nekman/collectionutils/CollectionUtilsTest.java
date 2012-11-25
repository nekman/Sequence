package se.nekman.collectionutils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static se.nekman.collectionutils.CollectionUtils.from;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

import se.nekman.collectionutils.exceptions.EmptySequenceException;

public class CollectionUtilsTest {

	// System under Test
	private CollectionUtils<Integer> sut;

	private static final Predicate<Integer> biggerThanFive = new Predicate<Integer>() {					
		@Override
		public boolean match(Integer item) {
			return item > 5;
		}
	};
	
	private static final Condition<Integer, String> mapToString = new Condition<Integer, String>() {
		@Override
		public String map(Integer item) {
			return String.valueOf(item);
		}							
	};

	@Before
	public void setup() {		
		sut = from(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}
	
	@Test
	public void itShouldReturnOneElementInSequenceAfterTakingOne() {
		List<Integer> first = sut
				.take(1)
				.toList();
		
		assertThat(first.size(), is(1));
		assertThat(first.get(0), is(1));
	}
	
	@Test
	public void itShouldReturnZeroElementList() {
		List<Integer> first = sut
				.take(0)
				.toList();
		
		assertThat(first.size(), is(0));		
	}
	
	@Test
	public void itShouldFilterNumberBiggerThanFive() {
		List<Integer> items = sut
				.filter(biggerThanFive)
				.toList();
		
		assertThat(items.size(), is(5));		
	}
	
	@Test
	public void itShouldMapToStrings() throws EmptySequenceException {
		CollectionUtils<String> items = sut
				.map(mapToString);
		
		assertThat(items.count(), is(10));
		assertThat(items.first(), is("1"));
		assertThat(items.last(), is("10"));
	}
	
	@Test
	public void itShouldTakeAll() {
		List<Integer> all = sut
				.take(1000)
				.toList();
		
		assertThat(all.size(), is(10));
	}
	
	@Test
	public void itShouldSkipTheFirstTwoItems() {
		List<Integer> allButThefirstTwo = sut
				.skip(2)
				.toList();
		
		int firstElement = allButThefirstTwo.get(0);
		
		assertThat(firstElement, is(3));
	}
	
	@Test
	public void itSkipTheFirstTwoItemsAndTakeTheNext() {
		List<Integer> result = sut
				.skip(2)
				.take(1)
				.toList();
		
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is(3));
	}
	
	@Test
	public void itShouldSkipOneAtTheTime() throws EmptySequenceException {
		CollectionUtils<String> items = from("one","two","three");
		
		int counter = 0;
		while((items = items.skip(1)).count() > 0) {
			counter++;
		}
		
		assertThat(counter, is(2));
	}
	
	@Test
	public void itShouldTakeOneAtTheTime() throws EmptySequenceException {
		CollectionUtils<String> items = from("one","two","three");		
		int counter = 3;
		
		while((items = items.take(counter--)).count() > 0);
		
		assertThat(items.count(), is(0));
		assertThat(items.firstOrDefault(), is(nullValue()));
	}
	
	@Test
	public void itShouldConcatTwoCollections() throws EmptySequenceException {
		CollectionUtils<String> items = from("one","two")
				.concat("three", "four", "five");
		
		assertThat(items.count(), is(5));
		assertThat(items.first(), is("one"));
		assertThat(items.last(), is("five"));
	}
	
	@Test
	public void itShouldConcatNullCollection() throws EmptySequenceException {
		CollectionUtils<String> items = from("one","two")
				.concat((String)null);
		
		assertThat(items.count(), is(3));		
		assertThat(items.last(), is(nullValue()));
	}
	
	@Test
	public void itShouldReturnTheFirstElement() throws EmptySequenceException {
		String result = from("one","two","three")
				.skip(2)
				.first();
				
		assertThat(result, is("three"));
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
