
package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.ListaStopWords;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per il caricamento di un file contenente stopwords nel database.
 * Questa classe utilizza {@link DAOListaStopWords} per memorizzare le stopwords 
 * associate a un utente identificato da email.
 *
 * @author Mattia Sanzari
 */
public class CaricaStopWordsService extends Service<Void> {
    
    private final DAOListaStopWords dbSW;
    private ListaStopWords listaStopWords;
    private byte[] contenutoDocumento;

    /**
     * Costruttore per il servizio di caricamento delle stopwords.
     * 
     * @param nomeFile Il nome del file contenente le stopwords.
     * @param email L'email dell'utente associato alle stopwords.
     * @param documento Il contenuto del documento di stopwords in formato byte array.
     */
    public CaricaStopWordsService(String nomeFile, String email, byte[] documento) {
        this.dbSW = DatabaseStopWords.getInstance();
        this.listaStopWords = new ListaStopWords(nomeFile, email, Timestamp.valueOf(LocalDateTime.now()));
        this.contenutoDocumento = documento;
    }

    /**
     * Imposta l'email dell'utente associato alle stopwords.
     * 
     * @param email L'email da impostare.
     */
    public void setEmail(String email) {
        if (this.listaStopWords == null) {
            this.listaStopWords = new ListaStopWords("", email, Timestamp.valueOf(LocalDateTime.now()));
        } else {
            this.listaStopWords = new ListaStopWords(
                this.listaStopWords.nomeFile(), 
                email, 
                Timestamp.valueOf(LocalDateTime.now())
            );
        }
    }

    /**
     * Imposta il contenuto del documento contenente le stopwords.
     * 
     * @param documento Il documento da caricare in formato byte array.
     */
    public void setDocumento(byte[] documento) {
        this.contenutoDocumento = documento;
    }

    /**
     * Imposta il nome del file contenente le stopwords.
     * 
     * @param nomeFile Il nome del file da impostare.
     */
    public void setNomeFile(String nomeFile) {
        if (this.listaStopWords == null) {
            this.listaStopWords = new ListaStopWords(nomeFile, "", Timestamp.valueOf(LocalDateTime.now()));
        } else {
            this.listaStopWords = new ListaStopWords(
                nomeFile, 
                this.listaStopWords.amministratore(), 
                Timestamp.valueOf(LocalDateTime.now())
            );
        }
    }

    /**
     * Crea un task asincrono per il caricamento delle stopwords nel database.
     * 
     * @return Un {@link Task} che esegue il caricamento delle stopwords.
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                dbSW.caricareStopwords(listaStopWords, contenutoDocumento);
                return null;
            }
        };
    }   
}

