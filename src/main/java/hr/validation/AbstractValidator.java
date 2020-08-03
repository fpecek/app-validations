package hr.validation;

/**
 * The abstract validators implementation.
 *
 * @author frano.pecek
 * @param <T> data to validate.
 */
public abstract class AbstractValidator<T> implements Validator<T> {

    @Override
    public final ValidationResults validate(final T data) {
        final ValidationResults validationResults = new ValidationResults();
		
        return doValidate(data, validationResults);
    }

    /**
     * Method for validating given data.
     *
     * @param data - input data to be validated
     * @param validationResults - validation result
     * @return - validation result
     */
    protected abstract ValidationResults doValidate(T data, ValidationResults validationResults);

}
