package hr.validation;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This interface provides methods for validating data.
 * @param <T> - the type of data for validation
 *
 * @author frano.pecek
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * Method for validating single data.
     *
     * @param data input data that needs to be validated
     * @return validation results
     */
    ValidationResults validate(T data);

    /**
     * Repack validation result in another validation result.
     *
     * @param newValidationResults - validation result to repack
     * @return - validator
     */
    default Validator<T> repack(final Supplier<ValidationResults> newValidationResults) {
        return data -> {
            final ValidationResults results = this.validate(data);
            if (results.isInvalid()) {
                return newValidationResults.get();
            }
            return results;
        };
    }

    /**
     * Validate input data and join result with another validation result.
     *
     * @param data - input data that needs to be validated
     * @param previousResults - validation results from another validator
     * @return joined validation result
     */
    default ValidationResults validateAndJoin(final T data, final ValidationResults previousResults) {
        final ValidationResults result = this.validate(data);
        if (previousResults == null) {
            return result;
        } else {
            return previousResults.join(result);
        }
    }

    /**
     * Validate stream of input data and join result with another validation result.
     *
     * @param data - stream of data
     * @param previousResults - validation results from another validator
     * @return joined validation result
     */
    default ValidationResults validateAndJoin(final Stream<T> data, final ValidationResults previousResults) {
        final ValidationResults result = this.validate(data);
        if (previousResults == null) {
            return result;
        } else {
            return previousResults.join(result);
        }
    }

    /**
     * Method for validating stream of data.
     *
     * @param data - input data that needs to be validated
     * @return - validation results
     */
    default ValidationResults validate(final Stream<T> data) {
        final ValidationResults validationResults = new ValidationResults();
        if (data == null) {
            return validationResults;
        }
        data.forEach(t -> validationResults.join(validate(t)));
        return validationResults;
    }

    /**
     * Validate data and throw exception if data is invalid.
     *
     * @param data - input data that needs to be validated
     */
    default void validateAndThrowIfInvalid(final T data) {
        final ValidationResults validationResults = validate(data);
        validationResults.throwIfInvalid();
    }

    /**
     * Chain validator with another validator.
     *
     * @param validator - validator to be chained
     * @return validator chain
     */
    default Validator<T> andThen(final Validator<T> validator) {
        return data -> this.validate(data).join(validator.validate(data));
    }

    /**
     * Chain validator with another validator of different type.
     *
     * @param validator - validator to be chained
     * @param convert - converter for converting value in another type
     * @param <D> - type to be converted
     * @return validator
     */
    default <D> Validator<T> andThen(final Validator<D> validator, final Function<T, D> convert) {
        return data -> this.validate(data).join(validator.validate(convert.apply(data)));
    }

    /**
     * Chain validator with another validator of different type.
     * @param validator - validator to be chained
     * @param convert - converter for converting value in another type
     * @param <D> - data type requested by chaining validator
     * @return chained validator
     */
    default <D> Validator<T> andThenForEach(final Validator<D> validator, final Function<T, Stream<D>> convert) {
        return data -> this.validate(data).join(validator.validate(convert.apply(data)));
    }

    /**
     * Chain validator with another validator only if first validator is valid.
     *
     * @param validator validator to be chained
     * @param convert converter for converting value in another type
     * @param <D> data type requested by chaining validator
     * @return chained validator
     */
    default <D> Validator<T> andThenIfValid(final Validator<D> validator, final Function<T, D> convert) {
        return data -> {
            final ValidationResults rs = this.validate(data);
            if (rs.isValid()) {
                rs.join(validator.validate(convert.apply(data)));
            }
            return rs;
        };
    }

    /**
     * Chain another validator only if executed validation is valid.
     *
     * @param validator validator to be chained
     * @return chained validator
     */
    default Validator<T> andThenIfValid(final Validator<T> validator) {
        return data -> {
            final ValidationResults rs = this.validate(data);
            if (rs.isValid()) {
                rs.join(validator.validate(data));
            }
            return rs;
        };
    }

    /**
     * Chain validator with another validator only with valid data from stream.
     *
     * @param validator validator to be chained
     * @param convert converter for converting value in another type
     * @param <D> data type requested by chaining validator
     * @return chained validator
     */
    default <D> Validator<T> andThenForEachIfValid(final Validator<D> validator, final Function<T, Stream<D>> convert) {
        return data -> {
            final ValidationResults rs = this.validate(data);
            if (rs.isValid()) {
                rs.join(validator.validate(convert.apply(data)));
            }
            return rs;
        };
    }
}
