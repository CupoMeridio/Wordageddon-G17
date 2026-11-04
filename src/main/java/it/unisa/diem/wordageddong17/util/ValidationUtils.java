package it.unisa.diem.wordageddong17.util;

/**
 * Utility per la validazione dei dati di input dell'utente.
 */
public final class ValidationUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private ValidationUtils() {}

    /**
     * Verifica la validità sintattica di un indirizzo email.
     *
     * @param email Indirizzo email da verificare.
     * @return {@code true} se rispetta lo standard, {@code false} altrimenti.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
