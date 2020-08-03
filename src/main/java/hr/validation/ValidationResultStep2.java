package hr.validation;

import hr.exception.ValidationException;

/**
 * Validation result final step.
 *
 * @author frano.pecek
 */
public interface ValidationResultStep2 extends ValidationResultStep1 {

	/**
	 * Method throws {@link ValidationException} if there is invalid data.
	 */
	void throwIfInvalid();

	/**
	 * Method to set variable data that will be replaced in message codes.
	 *
	 * @param messageParameters objects values that will be place in message code
	 * @return validation result object
	 */
	ValidationResults withParams(Object... messageParameters);

	/**
	 * Method for finish creating ValidationResults.
	 *
	 * @return created ValidationResults
	 */
	ValidationResults end();
}
