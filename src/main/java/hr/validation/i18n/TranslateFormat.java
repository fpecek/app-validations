package hr.validation.i18n;

/**
 * SPI for translating and formatting translatable object.
 *
 * @param <R> translated object return type
 * @author frano.pecek
 */
@FunctionalInterface
public interface TranslateFormat<R> {

	/**
	 * Translate and format messages.
	 *
	 * @param translatable translatable message
	 * @return translated and formatted message
	 */
	R translateAndFormat(Translatable translatable);

}
