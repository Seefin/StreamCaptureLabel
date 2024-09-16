package io.findlays.capturelabel;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A {@link CaptureStream} extends the standard {@link OutputStream} by sending
 * the buffered stream to a {@link Consumer} to consume.
 * <p>
 * Data is prefixed by a specified prefix, that can be chosen at instantiation.
 * <p>
 * Data is considered to be a stream of {@link Character}, and we buffer the
 * input on newline; when we recieve a newline the stream is flushed to the
 * consumer.
 * <p>
 * Optionally, the output can be sent to a specified {@link PrintStream} that is
 * included in the constructor invocation as well. This allows for a re-direction
 * of a stream while still sending data to the original location. If this object
 * is null, it is ignored.
 * 
 * @author Connor F
 */
public class CaptureStream extends OutputStream {
	/**
	 * Internal buffer for the Character stream; initial capacity is set as 128 characters,
	 * but this can grow or shrink throughout the application lifetime.
	 */
	private StringBuilder buffer;
	/**
	 * Optional prefix for the data stream; this will be pre-pended to all data sent to the
	 * consumer
	 */
	private String prefix;
	/**
	 * Consumer for this CaptureStream, that is responsible for consuming the buffered data stream
	 */
	private Consumer<String> consumer;
	/**
	 * Optional secondary {@link PrintStream} to send data to as well as the {@link Consumer}.
	 */
	private PrintStream alternativeStream;
	
	/**
	 * Create a new CaptureStream
	 * <p>
	 * Creates a new {@link CaptureStream} that feeds data to the specified consumer
	 * (e.g. a {@link CaptureLabel}) and {@link PrintStream}.
	 * <p>
	 * Data will be buffered, and optionally prepended with the value of the prefix
	 * argument.
	 * 
	 * @param prefix    Optional prefix to prepend to the buffered data stream
	 * @param consumer  Consumer that takes buffered messages
	 * @param old       Alternative {@link PrintStream} to also print to
	 * 
	 * @throws IllegalArgumentException  If the consumer argument is null
	 */
	public CaptureStream(String prefix, Consumer<String> consumer, PrintStream old) {
		if( consumer == null ) {
			throw new IllegalArgumentException("Consumer cannot be null");
		}
		this.prefix = prefix == null ? "" : prefix;
		this.alternativeStream = old;
		this.consumer = consumer;
		
		buffer = new StringBuilder(128);
		buffer.append("[").append(prefix).append("] ");
	}
	
	/**
	 * Writes the specified byte to this output stream.
	 * <p>
	 * The general contract for write is that one byte is written to the output stream. The byte
	 * to be written is the eight low-order bits of the argument b. The 24 high-order bits of b
	 * are ignored.
	 * <p>
	 * In this case, the data is buffered until an '\n' character is seen in the stream,
	 * at which point it will be flushed to the consumer and alternate stream and the internal 
	 * buffer will be drained.
	 * 
	 * @param b  The byte
	 */
	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
		String value = Character.toString(c);
		buffer.append(value);
		//Flush to consumer
		if( value.equals("\n") ) {
			consumer.consume(buffer.toString());
			buffer.delete(0,  buffer.length());
			buffer.append("[").append(prefix).append("] ");
		}
		//Flush to alternate stream
		if( alternativeStream != null ) {
			alternativeStream.print(c);
		}
	}
}
