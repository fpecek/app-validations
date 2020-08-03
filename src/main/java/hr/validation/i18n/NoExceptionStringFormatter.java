package hr.validation.i18n;

import hr.exception.AppRuntimeException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IllegalFormatException;

/**
 * This is string formatter that replace %s placeholders
 * with given parameters. This formatter will not throw
 * exception.
 *
 * @param <T> returning formatted message type
 * @author frano.pecek
 */
public class NoExceptionStringFormatter<T> implements Formatter<T, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppRuntimeException.class);

	private final Formatter<T, String> formatter;

	/**
	 * Create new instance with given formatter.
	 *
	 * @param formatter for formatting message
	 */
	public NoExceptionStringFormatter(final Formatter<T, String> formatter) {
		this.formatter = formatter;
	}

	@Override
	public String format(final T input, final Object... objects) {
		try {
			return formatter.format(input, objects);
		} catch (IllegalFormatException e) {
			LOGGER.error("FormatException: {}", input, e);
			return input + "; args: " + ArrayUtils.toString(objects);
		}
	}

}
