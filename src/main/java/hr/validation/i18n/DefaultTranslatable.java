package hr.validation.i18n;

/**
 * Default simple translatable implementation.
 *
 * @author frano.pecek
 */
public class DefaultTranslatable implements Translatable {

	private final MessageCode messageCode;
	private final Object[] objects;

	/**
	 * Create new translatable class with given message code
	 * and message parameters.
	 *
	 * @param messageCode message code
	 * @param objects message parameters
	 */
	public DefaultTranslatable(final MessageCode messageCode, final Object...objects) {
		this.messageCode = messageCode;
		this.objects = objects.clone();
	}

	@Override
	public MessageCode getMessageCode() {
		return messageCode;
	}

	@Override
	public Object[] getMessageParameters() {
		return objects;
	}

}
