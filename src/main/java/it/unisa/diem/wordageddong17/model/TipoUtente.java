package it.unisa.diem.wordageddong17.model;

/**
 * Enum che rappresenta i tipi di utente.
 * I possibili tipi sono:
 * - amministratore: utente con privilegi di amministrazione
 * - giocatore: utente che partecipa alle partite
 */
public enum TipoUtente {

    /** Utente con privilegi di amministrazione */
    AMMINISTRATORE("amministratore"),

    /** Utente che partecipa alle partite */
    GIOCATORE("giocatore");

    private final String dbValue;

    /**
     * Costruttore privato per l'enum TipoUtente.
     *
     * @param dbValue il valore da utilizzare nel database per rappresentare il tipo
     */
    private TipoUtente(String dbValue) {
        this.dbValue = dbValue;
    }

    /**
     * Restituisce il valore associato al tipo nel database.
     *
     * @return il valore come stringa
     */
    public String getDbValue() {
        return dbValue;
    }

    /**
     * Converte un valore salvato nel database in un oggetto TipoUtente.
     *
     * @param dbValue il valore dal database (non può essere null)
     * @return il corrispondente oggetto TipoUtente
     * @throws IllegalArgumentException se il valore è null o non valido
     */
    public static TipoUtente fromDbValue(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Valore del database non può essere null");
        }

        String trimmedValue = dbValue.trim();

        for (TipoUtente tipo : values()) {
            if (tipo.dbValue.equalsIgnoreCase(trimmedValue)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valore del database non valido per TipoUtente: '" + dbValue + "'");
    }
}
