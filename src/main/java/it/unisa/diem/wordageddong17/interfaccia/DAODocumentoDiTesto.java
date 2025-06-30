package it.unisa.diem.wordageddong17.interfaccia;

import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interfaccia DAODocumentoDiTesto
 * Questa interfaccia definisce i metodi necessari per gestire i documenti testuali nel sistema.
 * Le operazioni previste includono l'eliminazione, il caricamento, il recupero del contenuto,
 * il recupero di tutti i documenti e il filtraggio dei documenti in base al livello di difficoltà e alla lingua.
 *
 * @see it.unisa.diem.wordageddong17.model.DocumentoDiTesto
 * @see it.unisa.diem.wordageddong17.model.Lingua
 * @see it.unisa.diem.wordageddong17.model.LivelloPartita
 */
public interface DAODocumentoDiTesto {

    /**
     * Elimina un documento in base al suo nome.
     *
     * @param nomeDocumento il nome del documento da eliminare
     * @return {@code true} se l'eliminazione ha avuto successo, {@code false} altrimenti
     */
    public boolean cancellaTesto(String nomeDocumento);

    /**
     * Carica un documento testuale nel sistema.
     * Permette di salvare un documento fornendo un oggetto DocumentoDiTesto e il contenuto del documento.
     *
     * @param documento l'oggetto DocumentoDiTesto contenente le informazioni del documento da caricare
     * @param file      il contenuto del documento in formato {@code byte[]}
     * @return {@code true} se il caricamento ha avuto successo, {@code false} in caso contrario
     */
    public boolean caricaTesto(DocumentoDiTesto documento, byte[] file);

    /**
     * Recupera il contenuto testuale di un documento.
     *
     * @param nomeDocumento il nome del documento di cui si vuole ottenere il contenuto
     * @return un array di byte contenente il testo del documento; se il documento non esiste o si verifica un errore, viene restituito {@code null}
     */
    public byte[] prendiTesto(String nomeDocumento);

    /**
     * Recupera tutti i documenti presenti nel sistema.
     *
     * @return un {@link ArrayList} di {@link DocumentoDiTesto} contenente tutti i documenti
     */
    public ArrayList<DocumentoDiTesto> prendiTuttiIDocumenti();

    /**
     * Recupera i nomi dei documenti filtrati in base al livello di difficoltà e alle lingue specificate.
     * Viene applicato un filtro per selezionare solo i documenti che corrispondono al livello di partita
     * indicato e che sono scritti in una delle lingue presenti nella lista.
     *
     * @param livello il livello di difficoltà in base al quale filtrare i documenti
     * @param lingue  una lista di oggetti {@link Lingua} che specifica le lingue da includere nel filtro
     * @return un {@link ArrayList} di {@link String} contenente i nomi dei documenti filtrati
     */
    public  Map<String,Lingua> prendiNomiDocumentiFiltrati(LivelloPartita livello, ArrayList<Lingua> lingue);
}
