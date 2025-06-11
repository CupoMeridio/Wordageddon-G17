package it.unisa.diem.wordageddong17.model;

/**
 * Enum che rappresenta i livelli di difficoltà di una partita.
 * Ogni valore enum è associato a una stringa dbValue utilizzata per salvare il livello nel database.
 */
public enum LivelloPartita {
    /** Livello facile */
    FACILE("facile"), 
    /** Livello medio */
    MEDIO("medio"), 
    /** Livello difficile */
    DIFFICILE("difficile");

    private final String dbValue;

    /**
     * Costruttore privato per l'enum LivelloPartita.
     *
     * @param dbValue il valore da utilizzare nel database per rappresentare il livello
     */
    private LivelloPartita(String dbValue) {
        this.dbValue = dbValue;
    }

    /**
     * Restituisce il valore associato al livello nel database.
     *
     * @return il valore come stringa
     */
    public String getDbValue() {
        return dbValue;
    }

    /**
     * Converte un valore salvato nel database in un oggetto LivelloPartita.
     *
     * @param dbValue il valore dal database (non può essere null)
     * @return il corrispondente oggetto LivelloPartita
     * @throws IllegalArgumentException se il valore è null o non valido
     */
    public static LivelloPartita fromDbValue(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Valore del database non può essere null");
        }
        
        String trimmedValue = dbValue.trim();
        
        for (LivelloPartita livello : values()) {
            if (livello.dbValue.equalsIgnoreCase(trimmedValue)) {
                return livello;
            }
        }
        throw new IllegalArgumentException("Valore del database non valido per LivelloPartita: '" + dbValue + "'");
    }
    
    /**
     * Calcola la durata della partita (in secondi) in base al livello.
     * Le regole applicate sono:
     * - livello "facile": 60 secondi
     * - livello "medio": 40 secondi
     * - livello "difficile": 20 secondi
     *
     * @return la durata della partita in secondi
     */
    public int getCalcolaDurataPerLivello() {  
        return switch(this) {
            case FACILE -> 60;
            case MEDIO -> 40;
            case DIFFICILE -> 20;
        };
    }

    /**
     * Restituisce il numero di documenti da utilizzare in una partita, in base al livello di difficoltà.
     * Le regole applicate sono:
     * - "facile": 1 documento
     * - "medio": 2 documenti
     * - "difficile": 3 documenti
     *
     * @return il numero di documenti richiesti
     */
    public int getNumeroDocumenti() {  
        return switch(this.getDbValue()) {
            case "facile" -> 1;
            case "medio" -> 2;
            case "difficile" -> 3;
            default -> 1;
        };
    }
    
    /**
     * Restituisce il numero di domande per una partita, in base al livello di difficoltà.
     * Le regole applicate sono:
     * - "facile": 4 domande
     * - "medio": 6 domande
     * - "difficile": 8 domande
     *
     * @return il numero di domande da proporre durante la partita
     */
    public int getNumeroDomande() {  
        return switch(this.getDbValue()) {
            case "facile" -> 4;
            case "medio" -> 6;
            case "difficile" -> 8;
            default -> 5;
        };
    }
    
    /**
     * Restituisce il moltiplicatore da applicare al punteggio, in base al livello di difficoltà della partita.
     * Le regole applicate sono:
     * - "facile": moltiplicatore pari a 1
     * - "medio": moltiplicatore pari a 0.70
     * - "difficile": moltiplicatore pari a 0.2
     *
     * @return il moltiplicatore come valore double
     */
    public double getMoltiplicatorePerLivello() {  
        return switch(this.getDbValue()) {
            case "facile" -> 1.0;
            case "medio" -> 0.70;
            case "difficile" -> 0.2;
            default -> 1.0;
        };
    }
}
