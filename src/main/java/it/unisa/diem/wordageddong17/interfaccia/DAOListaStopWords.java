package it.unisa.diem.wordageddong17.interfaccia;

import it.unisa.diem.wordageddong17.model.ListaStopWords;

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
     * Questo metodo salva il documento contenente le stopwords utilizzando le informazioni del modello ListaStopWords.
     * L'array di byte {@code documentoStopwords} rappresenta il contenuto del file da salvare.
     *
     * @param listaStopWords       il modello contenente le informazioni della lista di stopwords
     * @param documentoStopwords   il contenuto del documento in formato byte
     * @return {@code true} se il caricamento ha avuto successo, {@code false} altrimenti
     */
    public boolean caricareStopwords(ListaStopWords listaStopWords, byte[] documentoStopwords);

    /**
     * Recupera il documento delle stopwords dal database.
     * 
     * Dato il nome del file, questo metodo restituisce il contenuto del documento in formato byte.
     * Se il documento non viene trovato, il metodo può restituire {@code null}.
     *
     * @param nomeFile il nome del file contenente le stopwords da recuperare
     * @return un array di byte contenente il documento delle stopwords oppure {@code null} se non viene trovato
     */
    public byte[] prendiStopwords(String nomeFile);
    
    /**
     * Recupera le informazioni sulla lista di stopwords dal database.
     * 
     * Dato il nome del file, questo metodo restituisce un oggetto ListaStopWords contenente
     * le informazioni sulla lista di stopwords.
     * Se il documento non viene trovato, il metodo può restituire {@code null}.
     *
     * @param nomeFile il nome del file contenente le stopwords da recuperare
     * @return un oggetto ListaStopWords contenente le informazioni sulla lista di stopwords oppure {@code null} se non viene trovato
     */
    public ListaStopWords prendiInfoStopwords(String nomeFile);
}
