package it.unisa.diem.wordageddong17.database;

/**
 * Eccezione specifica lanciata quando si tenta di registrare un utente
 * con username o email già presenti nel database.
 */
public class DuplicateUserException extends DataAccessException {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova eccezione con il messaggio specificato.
     *
     * @param message messaggio descrittivo dell'errore di duplicazione
     */
    public DuplicateUserException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova eccezione con il messaggio specificato e la causa originaria.
     *
     * @param message messaggio descrittivo dell'errore
     * @param cause causa sottostante
     */
    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
