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
    
    public int getCalcolaDurataPerLivello(){  
    int durata=    
        switch(this.name()){
            case "facile" -> 60;
            case "medio"-> 40;
            case "difficile" ->20;
            default -> 60;
        };
      return durata;
    }
    public int getNumeroDocumenti(){  
    int numeroDocumenti=    
        switch(this.getDbValue()){
            case "facile" -> 1;
            case "medio"-> 2;
            case "difficile" ->3;
            default -> 1;
        };
      return numeroDocumenti;
    }
    
    public int getNumeroDomande(){  
    int numeroDomande=    
        switch(this.getDbValue()){
            case "facile" -> 4;
            case "medio"-> 6;
            case "difficile" ->8;
            default -> 5;
        };
      return numeroDomande;
    }
    
    public double getMoltiplicatorePerLivello( ){  
    double moltiplicatore=    
        switch(this.getDbValue()){
            case "facile" -> 1;
            case "medio"-> 0.70;
            case "difficile" ->0.2;
            default -> 1;
        };
      return moltiplicatore;
    }
}