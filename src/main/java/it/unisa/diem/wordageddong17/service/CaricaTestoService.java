package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per il caricamento di un documento di testo nel database.
 * Questa classe utilizza {@link DAODocumentoDiTesto} per memorizzare i documenti
 * associati a un utente e categorizzati per lingua e difficoltà.
 *
 * @author Mattia Sanzari
 */
public class CaricaTestoService extends Service<Void> {
    
    private String email;
    private String nomeFile;
    private String difficoltà;
    private byte[] documento;
    private Lingua lingua;
    private final DAODocumentoDiTesto dbDT;

    /**
     * Costruttore con parametri per il servizio di caricamento del testo.
     * 
     * @param email L'email dell'utente che carica il documento.
     * @param nomeFile Il nome del file del documento di testo.
     * @param difficoltà Il livello di difficoltà del documento.
     * @param documento Il contenuto del documento di testo in formato byte array.
     * @param lingua La lingua del documento.
     */
    public CaricaTestoService(String email, String nomeFile, String difficoltà, byte[] documento, Lingua lingua) {
        this.email = email;
        this.nomeFile = nomeFile;
        this.difficoltà = difficoltà;
        this.documento = documento;
        this.lingua = lingua;
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    /**
     * Costruttore di default che inizializza il database di documenti di testo.
     */
    public CaricaTestoService() {
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    /**
     * Imposta l'email dell'utente associato al documento.
     * 
     * @param email L'email da impostare.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Imposta il nome del file del documento.
     * 
     * @param nomeFile Il nome del file da impostare.
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    /**
     * Imposta il livello di difficoltà del documento.
     * 
     * @param difficoltà La difficoltà da impostare.
     */
    public void setDifficoltà(String difficoltà) {
        this.difficoltà = difficoltà;
    }

    /**
     * Imposta il contenuto del documento di testo.
     * 
     * @param documento Il contenuto del documento in formato byte array.
     */
    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    /**
     * Imposta la lingua del documento.
     * 
     * @param lingua La lingua da impostare.
     */
    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    /**
     * Crea un task asincrono per il caricamento del documento nel database.
     * 
     * @return Un {@link Task} che esegue il caricamento del documento di testo.
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                dbDT.caricaTesto(email, nomeFile, difficoltà, documento, lingua);               
                return null;
            }  
        };
    }
}

