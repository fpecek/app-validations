package hr.validator;

import hr.exception.message.GlobalExceptionMessageCode;
import hr.validation.ValidationResult;
import hr.validation.ValidationResults;
import hr.validation.Validator;
import hr.exception.ValidationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Validator implementations.
 * Test class test and show all possible usage of Validator class.
 *
 * @author frano.pecek
 */
public class ValidatorTest {

    /**
     * Exception rule used for check if exception is thrown.
     */
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Dummy class used for testing.
     */
    private static final class ClassToValidate{
        private final String value;
        private final List<String> values;

        private ClassToValidate() {
            value = "Test Value";
            values = new LinkedList<>();
            values.add("Test Value 1");
            values.add("Test Value 2");
        }

        public String getValue() {
            return value;
        }

        public Stream<String> getStreamValues() {
            return values.stream();
        }
    }

    /**
     * Dummy validator that validate dummy object and simulate NOT_AUTHORIZED exception.
     */
    private static class ValidationFirst implements Validator<ClassToValidate> {
        @Override
        public ValidationResults validate(final ClassToValidate data) {
            return new ValidationResults().add(GlobalExceptionMessageCode.NOT_AUTHORIZED).end();
        }
    }

    /**
     * Dummy validator that validate dummy object and simulate DATA_NOT_FOUND exception.
     */
    private static class ValidationSecond implements Validator<ClassToValidate> {
        @Override
        public ValidationResults validate(final ClassToValidate data) {
            return new ValidationResults().add(GlobalExceptionMessageCode.DATA_NOT_FOUND).end();
        }
    }

    /**
     * Dummy validator that validate string value and simulate INVALID_PARAMETER exception.
     */
    private static class ValidationString implements Validator<String> {
        @Override
        public ValidationResults validate(final String data) {
            return new ValidationResults().add(GlobalExceptionMessageCode.INVALID_PARAMETER).end();
        }
    }

    private ValidationFirst validationFirst;
    private ValidationSecond validationSecond;
    private ValidationString validationString;
    private ClassToValidate objectToValidate;
    private Stream<ClassToValidate> objectsToValidate;

    /**
     * Initialize dummy object and validators used in tests.
     */
    @Before
    public void setUp() {
        validationFirst = new ValidationFirst();
        validationSecond = new ValidationSecond();
        validationString = new ValidationString();
        objectToValidate = new ClassToValidate();
        final List<ClassToValidate> objectsToBeValidated = new LinkedList<>();
        objectsToBeValidated.add(new ClassToValidate());
        objectsToBeValidated.add(new ClassToValidate());
        this.objectsToValidate = objectsToBeValidated.stream();
    }

    /**
     * Test running single validator on given data.
     * Expected result is to has one message code in validation result.
     */
    @Test
    public void testValidateSingle() {
        final ValidationResults vr = validationFirst.validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(1);
        assertThat(vr).extracting(ValidationResult::getMessageCode).contains(GlobalExceptionMessageCode.NOT_AUTHORIZED);
    }

    /**
     * Test running single validator on data stream.
     * There are two same test data in stream.
     * Expected two validation message codes of same type because we validate same object two times.
     */
    @Test
    public void testValidateStream() {
        final int expectedValidationResultSize = 2;
        final ValidationResults vr = validationFirst.validate(objectsToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(expectedValidationResultSize);
        assertThat(vr).extracting(ValidationResult::getMessageCode).containsOnly(GlobalExceptionMessageCode.NOT_AUTHORIZED);
    }

    /**
     * Test repacking existing validation result in another validation result.
     * Validation result will be replaced with given in repack method.
     * Validation result is repacked only if validation result is invalid.
     * Expected result is one validation message that is repacked from vr2.
     */
    @Test
    public void testRepack() {
        final ValidationResults vr2 = new ValidationResults().add(GlobalExceptionMessageCode.NOT_NULL).end();
        final ValidationResults vr = validationFirst.repack(()->vr2).validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(1);
        assertThat(vr).extracting(ValidationResult::getMessageCode).contains(GlobalExceptionMessageCode.NOT_NULL);
    }

    /**
     * Test single validator that will be joined with another
     * validation results (from any other validator or peace of code).
     * Join will keep values from all validation results.
     * Expected result is two validation messages.
     */
    @Test
    public void testValidateSingleAndJoin() {
        final int expectedValidationResultSize = 2;
        final ValidationResults vr2 = new ValidationResults().add(GlobalExceptionMessageCode.NOT_NULL).end();
        final ValidationResults vr = validationFirst.validateAndJoin(objectToValidate, vr2);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(expectedValidationResultSize);
        assertThat(vr).extracting(ValidationResult::getMessageCode)
                .containsOnly(GlobalExceptionMessageCode.NOT_NULL, GlobalExceptionMessageCode.NOT_AUTHORIZED);
    }

    /**
     * Test running single validator on data stream and join result with another
     * validation results.
     * Expected result is two have three validation messages, one from previous validation result,
     * a two from validating data stream.
     */
    @Test
    public void testValidateStreamAndJoin() {
        final int expectedValidationResultSize = 3;
        final ValidationResults vr2 = new ValidationResults().add(GlobalExceptionMessageCode.NOT_NULL).end();
        final ValidationResults vr = validationFirst.validateAndJoin(objectsToValidate, vr2);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(expectedValidationResultSize);
        assertThat(vr).extracting(ValidationResult::getMessageCode)
                .containsOnly(GlobalExceptionMessageCode.NOT_NULL, GlobalExceptionMessageCode.NOT_AUTHORIZED);
    }

    /**
     * Test validation result throw if invalid method throws exception of type {@link ValidationException}.
     */
    @Test
    public void testValidateAndThrowIfInvalid() {
        expectedEx.expect(ValidationException.class);
        validationFirst.validateAndThrowIfInvalid(objectToValidate);
    }

    /**
     * Test chaining two validators together. Both validators needs to validate data of same type.
     * First will be executed first validation and then will be executed second validation and
     * result will be joined in one validation result.
     * Expected result is two validation message codes from two different validators.
     */
    @Test
    public void testThen() {
        final int expectedValidationResultSize = 2;
        final ValidationResults vr = validationFirst.andThen(validationSecond).validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(expectedValidationResultSize);
        assertThat(vr).extracting(ValidationResult::getMessageCode)
                .containsOnly(GlobalExceptionMessageCode.NOT_AUTHORIZED, GlobalExceptionMessageCode.DATA_NOT_FOUND);
    }

    /**
     * Test chaining two validators together but with converter.
     * Converter is used when we want to pass another type of data
     * in other validator in chain. For example, first validator can
     * validate object of some type, and second validator validates
     * date from that object. Because all validators in chain should
     * validate date of same type, we can use converter to validate
     * data of different types.
     * Expected result is two validation message codes from two different validators validating different data.
     */
    @Test
    public void testThenWithConvert() {
        final int expectedValidationResultSize = 2;
        final ValidationResults vr = validationFirst.andThen(validationString, ClassToValidate::getValue).validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(expectedValidationResultSize);
        assertThat(vr).extracting(ValidationResult::getMessageCode)
                .containsOnly(GlobalExceptionMessageCode.NOT_AUTHORIZED, GlobalExceptionMessageCode.INVALID_PARAMETER);
    }

    /**
     * Test chaining two validators with stream of converted values.
     * First validator validates object and second validates stream of data from that object.
     * Expected result is three validation messages, one from first validator validating object,
     * and two from second validator validating stream of data.
     */
    @Test
    public void testThenForEachWithConvert() {
        final int expectedValidationResultSize = 3;
        final ValidationResults vr =
                validationFirst.andThenForEach(validationString, ClassToValidate::getStreamValues).validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(expectedValidationResultSize);
        assertThat(vr).extracting(ValidationResult::getMessageCode)
                .containsExactlyInAnyOrder(GlobalExceptionMessageCode.NOT_AUTHORIZED,
                        GlobalExceptionMessageCode.INVALID_PARAMETER, GlobalExceptionMessageCode.INVALID_PARAMETER);
    }

    /**
     * Test chaining two validators together with different input data,
     * but only when first validation executed without errors.
     * Expected result is one validation message because there is error on first
     * and second will not be executed.
     */
    @Test
    public void testThenIfValidWithConvert() {
        final ValidationResults vr =
                validationFirst.andThenIfValid(validationString, ClassToValidate::getValue).validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(1);
        assertThat(vr).extracting(ValidationResult::getMessageCode).containsOnly(GlobalExceptionMessageCode.NOT_AUTHORIZED);
    }

    /**
     * Test chaining two validators with stream of converted values but only if first validator is
     * executed without errors. First validator validates object and second validates stream of data from that object.
     * Expected result is one validation message because there is error on first
     * and second will not be executed.
     */
    @Test
    public void testThenIfValidForEachWithConvert() {
        final ValidationResults vr =
                validationFirst.andThenForEachIfValid(validationString, ClassToValidate::getStreamValues).validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(1);
        assertThat(vr).extracting(ValidationResult::getMessageCode).containsExactlyInAnyOrder(GlobalExceptionMessageCode.NOT_AUTHORIZED);
    }

    /**
     * Test chaining two validators together but only when first validation executed without errors.
     * First will be executed first validation and second will be executed only if there was no error in the first validator.
     * If there was error in first validator, result will contain only validation result from first validator.
     * IF there was no error in first, then result will be joined in one validation result.
     * Expected result is one validation message codes from first validator because it will return error message
     * so second validator will not be executed.
     */
    @Test
    public void testThenIfValid() {
        final ValidationResults vr = validationFirst.andThenIfValid(validationSecond).validate(objectToValidate);

        assertThat(vr).isNotEmpty();
        assertThat(vr).hasSize(1);
        assertThat(vr).extracting(ValidationResult::getMessageCode).contains(GlobalExceptionMessageCode.NOT_AUTHORIZED);
    }

}
