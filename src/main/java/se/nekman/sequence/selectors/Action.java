package se.nekman.sequence.selectors;

public interface Action<T> {
	void execute(final T item);
}
