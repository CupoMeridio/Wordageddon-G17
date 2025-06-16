package it.unisa.diem.wordageddong17.model;

/**
 * Enumerazione che rappresenta le lingue supportate nel sistema WordAgeddon.
 * 
 * Questa enum definisce tutte le lingue disponibili per i documenti testuali
 * e le partite del gioco. 
 * 
 * Le lingue supportate sono:
 * - Italiano 
 * - Inglese  
 * - Spagnolo 
 * - Francese 
 * - Tedesco 
 * 
 * L'enum fornisce metodi di utilità per:
 * - Ottenere il codice ISO della lingua
 * - Convertire un codice stringa nell'enum corrispondente
 * - Validazione dei codici lingua
 *
 */
public enum Lingua {

    /** Lingua italiana. */
    ITALIANO("it"),

    /** Lingua inglese. */
    INGLESE("en"),

    /** Lingua spagnola. */
    SPAGNOLO("es"),

    /** Lingua francese. */
    FRANCESE("fr"),

    /** Lingua tedesca. */
    TEDESCO("de");

    private final String codice;

    /**
     * Costruttore dell'enum che associa ogni lingua al suo codice.
     * 
     * @param codice il codice della lingua
     */
    private Lingua(String codice) {
        this.codice = codice;
    }

    /**
     * Restituisce il codice della lingua.
     * 
     * @return il codice a 2 caratteri della lingua (es. "it", "en", "es")
     */
    public String getCodice() {
        return codice;
    }
    
    /**
     * Converte un codice stringa nell'enum Lingua corrispondente.
     * 
     * Questo metodo permette di ottenere l'istanza dell'enum
     * a partire dal codice della lingua.
     * 
     * La ricerca viene effettuata confrontando il codice in input
     * con i codici di tutte le lingue disponibili.
     * 
     * @param codice il codice della lingua da cercare 
     * @return l'istanza di Lingua corrispondente al codice fornito
     * @throws IllegalArgumentException se il codice è null o se non corrisponde
     *                                  a nessuna lingua supportata. 
     */
    public static Lingua fromCodice(String codice) {
        if (codice == null) {
            throw new IllegalArgumentException("Codice lingua non può essere null");
        }

        String trimmed = codice.trim().toLowerCase();
        
        for (Lingua l : values()) {
            if (l.codice.equals(trimmed)) {
                return l;
            }
        }
        throw new IllegalArgumentException("Codice lingua non valido: '" + codice + "'");
    }
}
