package it.unisa.diem.wordageddong17.model;

import java.sql.Timestamp;

/**
 * Record che rappresenta una lista di stopwords.
 * Questo record incapsula le informazioni relative a un file contenente stopwords.
 *
 * @param nomeFile           il nome del file contenente le stopwords
 * @param amministratore     l'identificativo o l'email dell'amministratore che ha caricato il file
 * @param dataUltimaModifica la data e l'ora dell'ultima modifica apportata al file
 */
public record ListaStopWords(
    String nomeFile,
    String amministratore,
    Timestamp dataUltimaModifica
) {
    /**
     * Restituisce il nome del file contenente le stopwords.
     *
     * @return il nome del file
     */
    public String getNomeFile(){
        return nomeFile();
    }
    
    /**
     * Restituisce l'amministratore associato al file di stopwords.
     *
     * @return l'amministratore
     */
    public String getAmministratore(){
        return amministratore();
    }
    
    /**
     * Restituisce la data dell'ultima modifica apportata al file di stopwords.
     *
     * @return la data dell'ultima modifica
     */
    public Timestamp getDataUltimaModifica(){
        return dataUltimaModifica();
    }
}
