package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
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
    
    private DocumentoDiTesto documento;
    private byte[] contenutoDocumento;
    private final DAODocumentoDiTesto dbDT;

    /**
     * Costruttore con parametri per il servizio di caricamento del testo.
     * 
     * @param email L'email dell'utente che carica il documento.
     * @param nomeFile Il nome del file del documento di testo.
     * @param difficoltà Il livello di difficoltà del documento.
     * @param contenutoDocumento Il contenuto del documento di testo in formato byte array.
     * @param lingua La lingua del documento.
     */
    public CaricaTestoService(String email, String nomeFile, String difficoltà, byte[] contenutoDocumento, Lingua lingua) {
        LivelloPartita livello = LivelloPartita.valueOf(difficoltà.toUpperCase());
        this.documento = new DocumentoDiTesto(nomeFile, livello, email, lingua);
        this.contenutoDocumento = contenutoDocumento;
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
        if (this.documento == null) {
            this.documento = new DocumentoDiTesto("", LivelloPartita.FACILE, email, Lingua.ITALIANO);
        } else {
            this.documento = new DocumentoDiTesto(
                this.documento.getNomeFile(), 
                this.documento.getDifficolta(), 
                email, 
                this.documento.getLingua()
            );
        }
    }

    /**
     * Imposta il nome del file del documento.
     * 
     * @param nomeFile Il nome del file da impostare.
     */
    public void setNomeFile(String nomeFile) {
        if (this.documento == null) {
            this.documento = new DocumentoDiTesto(nomeFile, LivelloPartita.FACILE, "", Lingua.ITALIANO);
        } else {
            this.documento = new DocumentoDiTesto(
                nomeFile, 
                this.documento.getDifficolta(), 
                this.documento.getEmailAmministratore(), 
                this.documento.getLingua()
            );
        }
    }

    /**
     * Imposta il livello di difficoltà del documento.
     * 
     * @param difficoltà La difficoltà da impostare.
     */
    public void setDifficoltà(String difficoltà) {
        LivelloPartita livello = LivelloPartita.valueOf(difficoltà.toUpperCase());
        if (this.documento == null) {
            this.documento = new DocumentoDiTesto("", livello, "", Lingua.ITALIANO);
        } else {
            this.documento = new DocumentoDiTesto(
                this.documento.getNomeFile(), 
                livello, 
                this.documento.getEmailAmministratore(), 
                this.documento.getLingua()
            );
        }
    }

    /**
     * Imposta il contenuto del documento di testo.
     * 
     * @param contenutoDocumento Il contenuto del documento in formato byte array.
     */
    public void setDocumento(byte[] contenutoDocumento) {
        this.contenutoDocumento = contenutoDocumento;
    }

    /**
     * Imposta la lingua del documento.
     * 
     * @param lingua La lingua da impostare.
     */
    public void setLingua(Lingua lingua) {
        if (this.documento == null) {
            this.documento = new DocumentoDiTesto("", LivelloPartita.FACILE, "", lingua);
        } else {
            this.documento = new DocumentoDiTesto(
                this.documento.getNomeFile(), 
                this.documento.getDifficolta(), 
                this.documento.getEmailAmministratore(), 
                lingua
            );
        }
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
                dbDT.caricaTesto(documento, contenutoDocumento);               
                return null;
            }  
        };
    }
}

