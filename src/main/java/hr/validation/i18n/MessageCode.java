package hr.validation.i18n;

/**
 * This interface should implement all classes that
 * contains validation messages.
 *
 * @author frano.pecek
 */
public interface MessageCode {

    /**
     * No code default value.
     */
    String NO_CODE = "_NO_CODE_";

    /**
     * Get message key if message is defined in enum.
     *
     * @return message key as string if message is enum type, otherwise null
     */
    default String getKey() {
        if (this instanceof Enum) {
            return ((Enum<?>) this).name();
        } else {
            return null;
        }
    }

    /**
     * Get message value from enum as string.
     *
     * @return string message
     */
    String getMessage();

}
