package hr.validation;

import hr.enums.ExceptionSeverityLevel;
import hr.validation.i18n.MessageCode;

/**
 * Creating validation results first step adding validation result.
 *
 * @author frano.pecek
 */
public interface ValidationResultStep1 {

	/**
	 * Add new message in ValidationResults object with given MessageCode.
	 *
	 * @param msg validation message code
	 * @return next step
	 */
	ValidationResultStep2 add(MessageCode msg);

	/**
	 * Add new message with given message code and fields.
	 *
	 * @param msg validation message code
	 * @param fields object invalid fields
	 * @return next step
	 */
	ValidationResultStep2 add(MessageCode msg, String... fields);

	/**
	 * Add new ValidationResult with given bean, message code and fields.
	 *
	 * @param bean object that has been validated
	 * @param msg validation message code
	 * @param fields object invalid fields
	 * @return next step
	 */
	ValidationResultStep2 add(Object bean, MessageCode msg, String... fields);

	/**
	 * Add new ValidationResult with given message code and exception severity level.
	 *
	 * @param msg validation message code
	 * @param severityLevel exception severity level
	 * @return next step
	 */
	ValidationResultStep2 add(MessageCode msg, ExceptionSeverityLevel severityLevel);

	/**
	 * Add new ValidationResult with given message code, exception severity level and error fields.
	 *
	 * @param msg validation message code
	 * @param severityLevel exception severity level
	 * @param fields object invalid fields
	 * @return next step
	 */
	ValidationResultStep2 add(MessageCode msg, ExceptionSeverityLevel severityLevel, String... fields);

	/**
	 * Add new ValidationResult with given bean, message code, exception severity level and error fields.
	 *
	 * @param bean object that has been validated
	 * @param msg validation message code
	 * @param severityLevel exception severity level
	 * @param fields object invalid fields
	 * @return next step
	 */
	ValidationResultStep2 add(Object bean, MessageCode msg, ExceptionSeverityLevel severityLevel, String... fields);

	/**
	 * Add new validation error in ValidationResults using ValidationResult object.
	 *
	 * @param validationResult validation result object
	 * @return next step
	 */
	ValidationResults add(ValidationResult validationResult);

}
