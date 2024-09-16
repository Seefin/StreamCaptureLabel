package io.findlays.capturelabel;

/**
 * A Consumer consumes a text string.
 * <p>
 * This interface specifies the {@link #consume(E)} method, which
 * takes a single String argument. A Consumer is responsible for processing
 * the data associated with the argument, and doing some work with it.
 * <p>
 * For example, a logging facility may be a consumer that takes the data
 * and stores it in the system logs.
 */
public interface Consumer<E> {
	/**
	 * Consume data.
	 * <p>
	 * This method is how a {@link Consumer} will process text. Consumers must
	 * accept text via this interface, and process it according to their implementation.
	 * @param text
	 */
	public void consume(E text);
}
