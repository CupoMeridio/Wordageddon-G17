package it.unisa.diem.wordageddong17.util;

/**
 * Utility per la validazione dei dati di input dell'utente.
 */
public final class ValidationUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /** Lunghezza massima consentita per lo username, allineata alla colonna {@code username VARCHAR(30)}. */
    public static final int USERNAME_MAX_LENGTH = 30;

    /** Lunghezza minima richiesta per una password. */
    public static final int PASSWORD_MIN_LENGTH = 8;

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

    /**
     * Verifica che uno username non sia vuoto e non ecceda la lunghezza massima ammessa dal database.
     *
     * @param username Username da verificare.
     * @return {@code true} se lo username è valido, {@code false} altrimenti.
     */
    public static boolean isValidUsername(String username) {
        return username != null && !username.isBlank() && username.length() <= USERNAME_MAX_LENGTH;
    }

    /**
     * Verifica che una password rispetti la policy minima di sicurezza: almeno
     * {@value #PASSWORD_MIN_LENGTH} caratteri, contenente almeno una lettera e una cifra.
     *
     * @param password Password da verificare.
     * @return {@code true} se la password rispetta la policy, {@code false} altrimenti.
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < PASSWORD_MIN_LENGTH) {
            return false;
        }
        boolean contieneLettera = false;
        boolean contieneCifra = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLetter(c)) {
                contieneLettera = true;
            } else if (Character.isDigit(c)) {
                contieneCifra = true;
            }
        }
        return contieneLettera && contieneCifra;
    }
}
