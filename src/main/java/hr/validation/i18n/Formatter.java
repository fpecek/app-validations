package hr.validation.i18n;

/**
 * Interface used to format messages.
 *
 * @param <T> input type
 * @param <R> return formatted value type
 * @author frano.pecek
 */
@FunctionalInterface
public interface Formatter<T, R>{

	/**
	 * Format input message. For example, formatter can replace
	 * placeholders with concrete values.
	 *
	 * Example of string formatting
	 * "Some message %s" ---> "Some message parameter"
	 *
	 * @param input message to be formatted
	 * @param objects message parameters
	 * @return formatted message
	 */
	R format(T input, Object... objects);

}
