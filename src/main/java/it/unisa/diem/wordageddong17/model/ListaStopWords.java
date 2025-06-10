package it.unisa.diem.wordageddong17.model;

import java.sql.Timestamp;

/**
 * Record che rappresenta una lista di stopwords.
 * <p>
 * Questo record incapsula le informazioni relative ad un file contenente stopwords, 
 * in particolare:
 * <ul>
 *   <li><strong>nomeFile</strong>: il nome del file contenente le stopwords;</li>
 *   <li><strong>amministratore</strong>: l'identificativo o l'email dell'amministratore che ha caricato il file;</li>
 *   <li><strong>dataUltimaModifica</strong>: la data e l'ora dell'ultima modifica apportata al file.</li>
 * </ul>
 * </p>
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
