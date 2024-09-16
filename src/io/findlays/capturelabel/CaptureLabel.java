package io.findlays.capturelabel;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A Capture Label is a JPanel with a single component, the message label, that 
 * consumes text from a stream and updates the label to display the consumed
 * text.
 * <p>
 * This capture label may be attached to an error stream, and if the {@link #isErr}
 * boolean is set to true, the output will be displayed in red. 
 * <p>
 * The label embeds the consumed text in an HTML string - we do some basic escaping
 * of '<' and '>' characters, but if the output looks weird, more cases may need to 
 * be added to the {@link #escapeText(String)} method.
 * 
 * @author Connor F
 */
public class CaptureLabel<T> extends JPanel implements Consumer<T>{
	private static final long serialVersionUID = -1080882686263282679L;
	/**
	 * This is the JLabel that we will output the consumed text to.
	 */
	protected JLabel output;
	/**
	 * This boolean indicates whether we represent an error stream or not.
	 * If true, consumed text will be displayed in red on the label.
	 */
	protected boolean isErr = false;
	
	/**
	 * Create a new CaptureLabel
	 * <p>
	 * A Capture Label consumes text via the {@link #consume(String) } method,
	 * and displays this text in a {@link JLabel} that it holds in it's
	 * {@link BorderLayout#CENTER} position.
	 */
	public CaptureLabel() {
		setLayout(new BorderLayout());
		this.output = new JLabel("<html>");
		add(this.output, BorderLayout.CENTER);
	}

	/**
	 * Consume text from a publisher
	 * <p>
	 * As text is passed to the consume method, it is transformed into an HTML
	 * string, and the internal {@link JLabel} is updated to display the newly
	 * consumed text.
	 * <p>
	 * If the {@link #isErr} flag is set, the consumed text will be displayed in
	 * red, rather than the default font colour.
	 * 
	 * @param text  The text to be consumed
	 */
	@Override
	public void consume(T text) {
		text = escapeText(text);
		final StringBuilder sb = new StringBuilder("<html>");
		sb.append("<p>").append(isErr() ? "<font color=\"red\">" : "");
		sb.append(text);
		sb.append(isErr() ? "</font>" : "");
		sb.append("</p></html>");
		if( SwingUtilities.isEventDispatchThread() ) {
			output.setText(sb.toString());
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					output.setText(sb.toString());
				}
			});
		}
	}

	/**
	 * Escape '<' and '>' in strings.
	 * <p>
	 * Perform basic character escaping on the given string, removing the '<' and
	 * '>' characters, replacing them with their HTML entities instead. This is
	 * not a security feature, but a conveience one - uses should not need to know
	 * represent the text as HTML, so it is valid for a publisher to include valid HTML
	 * in the published stream we are consuming.
	 * 
	 * @param text  The text to escape
	 * 
	 * @return Valid HTML that looks like the un-escaped text, but will not break the JLabel
	 */
	@SuppressWarnings("unchecked")
	protected T escapeText(T text) {
		Class<String> clazz = String.class;
		if( clazz.isInstance(text) ) {
			String s = clazz.cast(text);
			s = s.replaceAll("<", "&lt;");
			s = s.replaceAll(">", "&gt;");
			return (T) s;
		} else {
			throw new IllegalArgumentException("Cannot process non-String input");
		}
	}

	/**
	 * Get the value of the {@link #isErr} flag.
	 * <p>
	 * Returns the value of the {@link #isErr} flag
	 * 
	 * @return  The value of the {@link #isErr} flag
	 */
	public boolean isErr() {
		return isErr;
	}

	/**
	 * Set the value of the {@link #isErr} flag
	 * <p>
	 * Set the {@link #isErr} flag to the provided value. If the provided value is
	 * {@code true}, the text in the {@link JLabel} will be red. Otherwise, the text
	 * will be displayed in the default foreground colour.
	 * 
	 * @see #getForeground()
	 * @see #isErr()
	 * 
	 * @param isErr  New Value of the isErr flag
	 */
	public void setErr(boolean isErr) {
		this.isErr = isErr;
	}
}
