package it.unisa.diem.wordageddong17.database;

/**
 * Eccezione applicativa non controllata (unchecked) lanciata in caso di
 * errori di accesso, connessione o operazione sul database relazionale.
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
