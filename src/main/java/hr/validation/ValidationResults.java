package hr.validation;

import hr.enums.ExceptionSeverityLevel;
import hr.ValidationException;
import hr.validation.i18n.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class that contains validation results.
 *
 * @author frano.pecek
 */
public class ValidationResults implements Iterable<ValidationResult>, ValidationResultStep1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationResults.class);

    private final List<ValidationResult> valResults;

    /**
     * Create new ValidationResult object.
     */
    public ValidationResults() {
        this.valResults = new ArrayList<>();
    }

    @Override
    public ValidationResultStep2 add(final MessageCode msg) {
        step2.add(msg);
        return step2;
    }

    @Override
    public ValidationResultStep2 add(final MessageCode msg, final String... fields) {
        step2.add(msg, fields);
        return step2;
    }

    @Override
    public ValidationResults add(final ValidationResult res) {
        step2.add(res);
        return this;
    }

    @Override
    public ValidationResultStep2 add(final MessageCode msg, final ExceptionSeverityLevel severityLevel) {
        step2.add(msg, severityLevel);
        return step2;
    }

    @Override
    public ValidationResultStep2 add(final MessageCode msg, final ExceptionSeverityLevel severityLevel, final String... fields) {
        step2.add(msg, severityLevel, fields);
        return step2;
    }

    @Override
    public Iterator<ValidationResult> iterator() {
        return this.valResults.iterator();
    }

    @Override
    public String toString() {
        return "ValidationResults [valResults=" + valResults + "]";
    }

    @Override
    public ValidationResultStep2 add(final Object bean, final MessageCode msg, final String... fields) {
        step2.add(bean, msg, fields);
        return step2;
    }

    @Override
    public ValidationResultStep2 add(final Object bean, final MessageCode msg, final ExceptionSeverityLevel severityLevel, final String... fields) {
        step2.add(bean, msg, severityLevel, fields);
        return step2;
    }

    private ValidationResults doAdd(final ValidationResult res) {
        this.valResults.add(res);
        return this;
    }

    /**
     * Join validation result with another validation result.
     *
     * @param results - validation result
     * @return - joined validation result
     */
    public ValidationResults join(final ValidationResults results) {
        this.valResults.addAll(results.valResults);
        return this;
    }

    /**
     * Check if validation result is valid.
     *
     * @return true if there is no error
     */
    public boolean isValid() {
        return this.valResults.isEmpty();
    }

    /**
     * Check if validation result is invalid.
     *
     * @return true if there is error
     */
    public boolean isInvalid() {
        return !isValid();
    }

    /**
     * Clear all validation results.
     */
    public void clearValidationResult() {
        this.valResults.clear();
    }

    /**
     * Throw exception if validation is invalid.
     */
    public void throwIfInvalid() {
        if (!isValid()) {
            throw toException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Throw exception if validation is invalid.
     *
     * @param httpStatus http error code
     */
    public void throwIfInvalid(final HttpStatus httpStatus) {
        if (!isValid()) {
            throw toException(httpStatus);
        }
    }

    /**
     * Validation results stream.
     *
     * @return stream of validation results
     */
    public Stream<ValidationResult> stream() {
        return this.valResults.stream();
    }

    /**
     * Create new exception.
     *
     * @return - Validation exception
     */
    private ValidationException toException(final HttpStatus httpStatus) {
        logValidationResults();

        return new ValidationException(this, httpStatus);
    }

    /**
     * Log validation messages after exception is thrown.
     * Work in progress - update needed
     */
    private void logValidationResults() {
        for (final ValidationResult validationResult : this.valResults) {
            LOGGER.error("-----------VALIDATION RESULT START--------------");
            LOGGER.error("MESSAGE CODE >> " + validationResult.getMessageCode());
            LOGGER.error("ERROR MESSAGE >> " + validationResult.getMessage());
            LOGGER.error("SEVERITY LEVEL >> {}", validationResult.getSeverityLevel());
            LOGGER.error("MESSAGE PARAMETERS >> " + Arrays.toString(validationResult.getMessageParameters()));
            LOGGER.error("-----------VALIDATION RESULT END--------------");
        }
    }

    //CHECKSTYLE:OFF
    private final ValidationResultStep2 step2 = new ValidationResultStep2() {

        private ValidationResult lastValidation;

        @Override
        public ValidationResultStep2 add(final MessageCode msg, final String... fields) {
            add(new ValidationResult(msg, fields));
            return this;
        }

        @Override
        public ValidationResultStep2 add(final MessageCode msg) {
            add(new ValidationResult(msg));
            return this;
        }

        @Override
        public ValidationResultStep2 add(final MessageCode msg, final ExceptionSeverityLevel severityLevel, final String... fields) {
            add(new ValidationResult(msg, severityLevel, fields));
            return this;
        }

        @Override
        public ValidationResultStep2 add(final MessageCode msg, final ExceptionSeverityLevel severityLevel) {
            add(new ValidationResult(msg, severityLevel));
            return this;
        }

        @Override
        public ValidationResults withParams(final Object... objects) {
            if (lastValidation != null) {
                lastValidation.withMessageParameters(objects);
            }
            return ValidationResults.this;
        }

        @Override
        public ValidationResults add(final ValidationResult res) {
            lastValidation = res;
            ValidationResults.this.doAdd(res);
            return ValidationResults.this;
        }

        @Override
        public void throwIfInvalid() {
            ValidationResults.this.throwIfInvalid();
        }

        @Override
        public ValidationResults end() {
            return ValidationResults.this;
        }

        @Override
        public ValidationResultStep2 add(final Object bean, final MessageCode msg, final String... fields) {
            add(new ValidationResult(bean, msg, fields));
            return this;
        }

        @Override
        public ValidationResultStep2 add(final Object bean, final MessageCode msg, final ExceptionSeverityLevel severityLevel, final String... fields) {
            add(new ValidationResult(bean, msg, severityLevel, fields));
            return this;
        }
    };
    //CHECKSTYLE:ON

}
