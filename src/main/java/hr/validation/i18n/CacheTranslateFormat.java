package hr.validation.i18n;

/**
 * Class that translate and format message and then cache
 * returned value so there is no need to translate and
 * format again.
 *
 * @param <R> translated object return type
 * @author frano.pecek
 */
public class CacheTranslateFormat<R> implements TranslateFormat<R> {

	private R value;
	private final TranslateFormat<R> translateFormat;

	/**
	 * Create new instance of {@link CacheTranslateFormat}.
	 * @param translateFormat translator and formatter instance
	 */
	public CacheTranslateFormat(final TranslateFormat<R> translateFormat) {
		this.translateFormat = translateFormat;
	}

	@Override
	public R translateAndFormat(final Translatable t) {
		if (value != null) {
			return value;
		}
		value = translateFormat.translateAndFormat(t);
		return value;
	}

}
