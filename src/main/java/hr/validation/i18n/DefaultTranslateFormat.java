package hr.validation.i18n;

import hr.exception.AppRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of class that translate and format message.
 *
 * @author frano.pecek
 */
public class DefaultTranslateFormat implements TranslateFormat<String> {

    /**
     * Create default instance.
     */
    public static final TranslateFormat<String> INSTANCE = new DefaultTranslateFormat();

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRuntimeException.class);

    private final Formatter<String, String> formatter;
    private final Translator<String> translator;

    /**
     * Create default instance.
     */
    public DefaultTranslateFormat() {
        formatter = new NoExceptionStringFormatter<>(String::format);
        translator = new DefaultTranslator();
    }

    /**
     * Create new instance with given formatter and translator.
     *
     * @param formatter for formatting message
     * @param translator for translating message
     */
    public DefaultTranslateFormat(final Formatter<String, String> formatter, final Translator<String> translator) {
        this.formatter = formatter;
        this.translator = translator;
    }

    @Override
    public String translateAndFormat(final Translatable translatable) {
        String template = null;
        String message = null;
        if (translatable.getMessageCode() != null) {
            template = translator.translate(translatable.getMessageCode(), null);
        }
        if (template != null) {
            message = formatter.format(template, translatable.getMessageParameters());
        }
        if (message == null) {
            message = template;
        }

        if (message == null) {
            //JUST FOR STACKTRACE
            final Exception e = new Exception(MessageCode.NO_CODE);
            LOGGER.error("No code definition: ", e);
            message = MessageCode.NO_CODE;
        }

        return message;

    }

}
