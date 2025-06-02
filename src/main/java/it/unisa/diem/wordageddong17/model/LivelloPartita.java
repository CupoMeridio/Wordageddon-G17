package it.unisa.diem.wordageddong17.model;

public enum LivelloPartita {
    FACILE("facile"), 
    MEDIO("medio"), 
    DIFFICILE("difficile");

    private final String dbValue;

    private LivelloPartita(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

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
}