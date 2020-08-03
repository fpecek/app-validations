package hr.validation.i18n;

import java.util.Locale;

/**
 * Default translator simple implementation.
 * This implementation just return message from message code
 * without translation.
 *
 * @author frano.pecek
 */
public class DefaultTranslator implements Translator<String> {

	@Override
	public String translate(final MessageCode code, final Locale locale) {
		return code.getMessage();
	}

}
