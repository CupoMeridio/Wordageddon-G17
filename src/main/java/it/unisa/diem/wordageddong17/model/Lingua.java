package it.unisa.diem.wordageddong17.model;

public enum Lingua {
    ITALIANO("it"),
    INGLESE("en"),
    SPAGNOLO("es"),
    FRANCESE("fr"),
    TEDESCO("de");

    private final String codice;

    private Lingua(String codice) {
        this.codice = codice;
    }

    public String getCodice() {
        return codice;
    }

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
