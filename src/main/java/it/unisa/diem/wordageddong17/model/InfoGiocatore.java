package it.unisa.diem.wordageddong17.model;

import java.util.List;

/**
 * Questo record rappresenta le informazioni di un giocatore.
 * 
 * Incapsula:
 * - La cronologia delle partite giocate (lista di {@link Classifica}).
 * - Il numero di partite giocate per ciascun livello di difficoltà (facile, medio, difficile).
 * - Il miglior punteggio ottenuto per ciascun livello di difficoltà.
 *
 * @param cronologiaPartiteList la lista delle partite giocate
 * @param facileCount numero di partite giocate in modalità facile
 * @param medioCount numero di partite giocate in modalità medio
 * @param difficileCount numero di partite giocate in modalità difficile
 * @param facilePunteggio miglior punteggio in modalità facile
 * @param medioPunteggio miglior punteggio in modalità medio
 * @param difficilePunteggio miglior punteggio in modalità difficile
 */
public record InfoGiocatore(
    List<Classifica> cronologiaPartiteList,
    int facileCount,
    int medioCount,
    int difficileCount,
    float facilePunteggio,
    float medioPunteggio,
    float difficilePunteggio       
) {
    
    /**
     * Restituisce la lista della cronologia delle partite giocate.
     *
     * @return la lista di oggetti {@link Classifica} rappresentante la cronologia delle partite
     */
    public List<Classifica> getCronologiaPartiteList(){
        return cronologiaPartiteList();
    }
    
    /**
     * Restituisce il numero di partite giocate in modalità facile.
     *
     * @return il conteggio delle partite giocate in modalità facile
     */
    public int getFacileCount(){
        return facileCount();
    }
    
    /**
     * Restituisce il numero di partite giocate in modalità medio.
     *
     * @return il conteggio delle partite giocate in modalità medio
     */
    public int getMedioCount(){
        return medioCount();
    }
    
    /**
     * Restituisce il numero di partite giocate in modalità difficile.
     *
     * @return il conteggio delle partite giocate in modalità difficile
     */
    public int getDifficileCount(){
        return difficileCount();
    }
    
    /**
     * Restituisce il miglior punteggio ottenuto in modalità facile.
     *
     * @return il miglior punteggio in modalità facile
     */
    public float getFacilePunteggio(){
        return facilePunteggio();
    }
    
    /**
     * Restituisce il miglior punteggio ottenuto in modalità medio.
     *
     * @return il miglior punteggio in modalità medio
     */
    public float getMedioPunteggio(){
        return medioPunteggio();
    }
    
    /**
     * Restituisce il miglior punteggio ottenuto in modalità difficile.
     *
     * @return il miglior punteggio in modalità difficile
     */
    public float getDifficilePunteggio(){
        return difficilePunteggio();
    }
}
