package hr.validation;

import hr.enums.ExceptionSeverityLevel;
import hr.validation.i18n.CacheTranslateFormat;
import hr.validation.i18n.DefaultTranslateFormat;
import hr.validation.i18n.MessageCode;
import hr.validation.i18n.Translatable;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Validation result class.
 *
 * @author frano.pecek
 */
public class ValidationResult implements Translatable {

    private final CacheTranslateFormat<String> cachedTranslateFormat = new CacheTranslateFormat<>(DefaultTranslateFormat.INSTANCE);
    private final MessageCode messageCode;
    private final String[] fields;
    private Object[] messageParameters = new Object[]{};
    private Object bean;
    private ExceptionSeverityLevel severityLevel;

    /**
     * Create new ValidationResult using bean, message code, exception severity level and error fields.
     *
     * @param bean object that has been validated
     * @param msgCode validation message code
     * @param severityLevel exception severity level
     * @param fields object invalid fields
     */
    public ValidationResult(final Object bean, final MessageCode msgCode, final ExceptionSeverityLevel severityLevel,
                            final String... fields) {
        this.messageCode = msgCode;
        this.fields = Arrays.copyOf(fields, fields.length);
        this.bean = bean;
        this.severityLevel = severityLevel;
    }

    /**
     * Create new ValidationResult using message code, exception severity level and error fields.
     *
     * @param msgCode validation message code
     * @param severityLevel exception severity level
     * @param fields object invalid fields
     */
    public ValidationResult(final MessageCode msgCode, final ExceptionSeverityLevel severityLevel, final String... fields) {
        this(new Object(), msgCode, severityLevel, fields);
    }

    /**
     * Create new ValidationResult using message code and exception severity level.
     *
     * @param msgCode validation message code
     * @param severityLevel exception severity level
     */
    public ValidationResult(final MessageCode msgCode, final ExceptionSeverityLevel severityLevel) {
        this(new Object(), msgCode, severityLevel, new String[0]);
    }

    /**
     * Create new ValidationResult using bean, message code and error fields.
     *
     * @param bean object that has been validated
     * @param msgCode validation message code
     * @param fields object invalid fields
     */
    public ValidationResult(final Object bean, final MessageCode msgCode, final String... fields) {
        this(bean, msgCode, ExceptionSeverityLevel.ERROR, fields);
    }

    /**
     * Create new ValidationResult using message code and error fields.
     *
     * @param msgCode validation message code
     * @param fields object invalid fields
     */
    public ValidationResult(final MessageCode msgCode, final String... fields) {
        this(new Object(), msgCode, ExceptionSeverityLevel.ERROR, fields);
    }

    /**
     * Create new ValidationResult using message code.
     *
     * @param msgCode validation message code
     */
    public ValidationResult(final MessageCode msgCode) {
        this(new Object(), msgCode, ExceptionSeverityLevel.ERROR, new String[0]);
    }

    @Override
    public MessageCode getMessageCode() {
        return this.messageCode;
    }

    @Override
    public Object[] getMessageParameters() {
        return messageParameters;
    }

    /**
     * Setter for setting message parameters.
     *
     * @param messageParams objects values that will be place in message code
     * @return current object reference
     */
    public ValidationResult withMessageParameters(final Object... messageParams) {
        this.messageParameters = Arrays.copyOf(messageParams, messageParams.length);
        return this;
    }

    public String getMessage() {
        return cachedTranslateFormat.translateAndFormat(this);
    }

    public String[] getFields() {
        return this.fields;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(final Object bean) {
        this.bean = bean;
    }

    public ExceptionSeverityLevel getSeverityLevel() {
        return severityLevel;
    }

    protected void setSeverityLevel(final ExceptionSeverityLevel newLevel) {
        this.severityLevel = newLevel;
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("; ", "[", "]");
        if (this.fields != null) {
            for (final String s : this.fields) {
                sj.add(s);
            }
        }
        return this.messageCode + " " + sj.toString();
    }
}
