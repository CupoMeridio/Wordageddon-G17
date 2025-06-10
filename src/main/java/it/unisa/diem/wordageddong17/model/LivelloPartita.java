package it.unisa.diem.wordageddong17.model;

/**
 * Enum che rappresenta i livelli di difficoltà di una partita.
 * <p>
 * Ogni valore enum è associato a una stringa (dbValue) che viene utilizzata per salvare il livello nel database.
 * </p>
 */
public enum LivelloPartita {
    FACILE("facile"), 
    MEDIO("medio"), 
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
     * <p>
     * Le regole applicate sono:
     * <ul>
     *   <li>livello "facile": 60 secondi</li>
     *   <li>livello "medio": 40 secondi</li>
     *   <li>livello "difficile": 20 secondi</li>
     * </ul>
     * Attenzione: il metodo confronta il valore di {@code this.name()}, che restituisce i nomi in maiuscolo.
     * È responsabilità dello sviluppatore assicurarsi che il confronto funzioni correttamente.
     * </p>
     *
     * @return la durata della partita in secondi
     */
    public int getCalcolaDurataPerLivello(){  
        int durata = switch(this.name()){
            case "facile" -> 60;
            case "medio" -> 40;
            case "difficile" -> 20;
            default -> 60;
        };
        return durata;
    }
    
    /**
     * Restituisce il numero di documenti da utilizzare in una partita, in base al livello di difficoltà.
     * <p>
     * Le regole applicate sono:
     * <ul>
     *   <li>"facile": 1 documento</li>
     *   <li>"medio": 2 documenti</li>
     *   <li>"difficile": 3 documenti</li>
     * </ul>
     * </p>
     *
     * @return il numero di documenti richiesti
     */
    public int getNumeroDocumenti(){  
        int numeroDocumenti = switch(this.getDbValue()){
            case "facile" -> 1;
            case "medio" -> 2;
            case "difficile" -> 3;
            default -> 1;
        };
        return numeroDocumenti;
    }
    
    /**
     * Restituisce il numero di domande per una partita, in base al livello di difficoltà.
     * <p>
     * Le regole applicate sono:
     * <ul>
     *   <li>"facile": 4 domande</li>
     *   <li>"medio": 6 domande</li>
     *   <li>"difficile": 8 domande</li>
     * </ul>
     * </p>
     *
     * @return il numero di domande da proporre durante la partita
     */
    public int getNumeroDomande(){  
        int numeroDomande = switch(this.getDbValue()){
            case "facile" -> 4;
            case "medio" -> 6;
            case "difficile" -> 8;
            default -> 5;
        };
        return numeroDomande;
    }
    
    /**
     * Restituisce il moltiplicatore da applicare al punteggio, in base al livello di difficoltà della partita.
     * <p>
     * Le regole applicate sono:
     * <ul>
     *   <li>"facile": moltiplicatore pari a 1</li>
     *   <li>"medio": moltiplicatore pari a 0.70</li>
     *   <li>"difficile": moltiplicatore pari a 0.2</li>
     * </ul>
     * </p>
     *
     * @return il moltiplicatore come valore double
     */
    public double getMoltiplicatorePerLivello(){  
        double moltiplicatore = switch(this.getDbValue()){
            case "facile" -> 1;
            case "medio" -> 0.70;
            case "difficile" -> 0.2;
            default -> 1;
        };
        return moltiplicatore;
    }
}
