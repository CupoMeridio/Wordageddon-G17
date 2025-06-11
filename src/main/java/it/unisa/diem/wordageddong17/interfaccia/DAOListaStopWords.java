package it.unisa.diem.wordageddong17.interfaccia;

/**
 * Interfaccia DAOListaStopWords
 * Questa interfaccia definisce i metodi necessari per gestire la lista delle stopwords all'interno del sistema.
 * In particolare, permette di:
 * - Caricare (cioè salvare) un documento contenente stopwords associandolo ad un utente.
 * - Recuperare il documento di stopwords in formato byte, dato il nome del file.
 */
public interface DAOListaStopWords {

    /**
     * Carica le stopwords nel database.
     * Questo metodo salva il documento contenente le stopwords associato all'email dell'utente e al nome del file.
     * L'array di byte {@code documentoStopwords} rappresenta il contenuto del file da salvare.
     *
     * @param email                l'email dell'utente che carica il documento
     * @param documentoStopwords   il contenuto del documento in formato byte
     * @param nomeFile             il nome del file contenente le stopwords
     * @return {@code true} se il caricamento ha avuto successo, {@code false} altrimenti
     */
    public boolean CaricareStopwords(String email, byte[] documentoStopwords, String nomeFile);

    /**
     * Recupera il documento delle stopwords dal database.
     * 
     * Dato il nome del file, questo metodo restituisce il contenuto del documento in formato byte.
     * Se il documento non viene trovato, il metodo può restituire {@code null}.
     *
     * @param nomeFile il nome del file contenente le stopwords da recuperare
     * @return un array di byte contenente il documento delle stopwords oppure {@code null} se non viene trovato
     */
    public byte[] PrendiStopwords(String nomeFile);
}
