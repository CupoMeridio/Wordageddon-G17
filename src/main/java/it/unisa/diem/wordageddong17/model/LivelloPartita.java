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
        for (LivelloPartita livello : values()) {
            if (livello.dbValue.equalsIgnoreCase(dbValue)) {
                return livello;
            }
        }
        return FACILE; // Default value if not found
    }
}