package it.unisa.diem.wordageddong17.model;

import java.util.Objects;

public record DocumentoDiTesto(
    String nomeFile,
    LivelloPartita difficolta,
    String emailAmministratore,
    Lingua lingua
) {
    public DocumentoDiTesto {
        Objects.requireNonNull(nomeFile, "Il nome del file non può essere null");
        Objects.requireNonNull(emailAmministratore, "L'email dell'amministratore non può essere null");
        Objects.requireNonNull(lingua, "La lingua non può essere null");
        Objects.requireNonNull(difficolta, "La difficoltà non può essere null");
    }

    public String getNomeLingua() {
        return lingua.name().toLowerCase();
    }
    
    public Lingua getLingua(){
        return lingua();
    }
    
    public String getNomeFile(){
        return nomeFile();
    }
    
    public LivelloPartita getDifficolta(){
        return difficolta();
    }
    
    public String getEmailAmministratore(){
        return emailAmministratore();
    }
    
    
}