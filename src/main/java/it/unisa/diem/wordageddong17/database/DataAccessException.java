package it.unisa.diem.wordageddong17.database;

/**
 * Eccezione applicativa non controllata (unchecked) lanciata in caso di
 * errori di accesso, connessione o operazione sul database relazionale.
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova eccezione con il messaggio specificato.
     *
     * @param message messaggio descrittivo dell'errore
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova eccezione con il messaggio specificato e la causa originaria.
     *
     * @param message messaggio descrittivo dell'errore
     * @param cause causa sottostante (es. SQLException)
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
