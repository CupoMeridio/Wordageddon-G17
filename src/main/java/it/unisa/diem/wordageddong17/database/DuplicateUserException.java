package it.unisa.diem.wordageddong17.database;

/**
 * Eccezione specifica lanciata quando si tenta di registrare un utente
 * con username o email già presenti nel database.
 */
public class DuplicateUserException extends DataAccessException {

    private static final long serialVersionUID = 1L;

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
