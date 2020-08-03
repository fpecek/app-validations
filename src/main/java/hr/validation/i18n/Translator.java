package hr.validation.i18n;

import java.util.Locale;

/**
 * SPI to translate given {@link MessageCode}.
 *
 * @param <R> translated object return type
 * @author frano.pecek
 */
@FunctionalInterface
public interface Translator<R> {

	/**
	 * Method translate message code to given locale/language.
	 *
	 * @param messageCode message that will be translated
	 * @param locale language to translate
	 * @return translated message
	 */
	R translate(MessageCode messageCode, Locale locale);

}
