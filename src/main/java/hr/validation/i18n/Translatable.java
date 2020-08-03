package hr.validation.i18n;

/**
 * This interface should implement all classes that
 * has translatable messages. For example, ValidationResult,
 * AppRuntimeException and etc.
 *
 * @author frano.pecek
 */
public interface Translatable {

	/**
	 * Get translatable message code.
	 *
	 * @return message code
	 */
	MessageCode getMessageCode();

	/**
	 * Get message code parameters.
	 *
	 * @return message code parameters
	 */
	Object[] getMessageParameters();

}
