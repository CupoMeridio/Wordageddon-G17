package it.unisa.diem.wordageddong17.model;

import java.util.Objects;

/**
 * Record che rappresenta un documento di testo utilizzato nel gioco WordAgeddon.
 * 
 * Questo record incapsula tutte le informazioni di un documento
 * testuale caricato nel sistema. Ogni documento è caratterizzato da un nome file,
 * un livello di difficoltà, la lingua del contenuto e l'identificativo dell'amministratore
 * che l'ha caricato nel sistema.
 * 
 * Il record implementa:
 * - Costruttore con tutti i parametri
 * - Metodi getter per tutti i campi
 * - Metodi equals(), hashCode() e toString()
 * 
 * @param nomeFile il nome del file del documento 
 * @param difficolta il livello di difficoltà associato al documento 
 * @param emailAmministratore l'email dell'amministratore che ha caricato il documento 
 * @param lingua la lingua del contenuto del documento 
 */
public record DocumentoDiTesto(
    String nomeFile,
    LivelloPartita difficolta,
    String emailAmministratore,
    Lingua lingua
) {


    /**
     * Restituisce il nome della lingua.
     * 
     * Questo metodo converte l'enum Lingua in una rappresentazione stringa normalizzata (tutto minuscolo).
     * 
     * @return il nome della lingua convertito in minuscolo (es. "italiano", "english")
     */
    public String getNomeLingua() {
        return lingua.name().toLowerCase();
    }
    
    /**
     * Restituisce l'enum Lingua associato al documento.
     * 
     * @return l'enum Lingua che rappresenta la lingua del documento
     */
    public Lingua getLingua(){
        return lingua();
    }
    
    /**
     * Restituisce il nome del file del documento.
     * 
     * @return il nome del file come stringa
     */
    public String getNomeFile(){
        return nomeFile();
    }
    
    /**
     * Restituisce il livello di difficoltà del documento.
     * 
     * @return il LivelloPartita che rappresenta la difficoltà del documento
     */
    public LivelloPartita getDifficolta(){
        return difficolta();
    }
    
     /**
     * Restituisce l'email dell'amministratore che ha caricato il documento.
     * 
     * @return l'email dell'amministratore come stringa
     */
    public String getEmailAmministratore(){
        return emailAmministratore();
    }
    
    
}