package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.database.MancanzaDiDocumenti;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.AnalisiDocumenti;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per il caricamento della sessione di gioco, inclusa l'analisi dei documenti 
 * e la generazione delle domande. Utilizza database di documenti di testo e stopwords 
 * per elaborare le informazioni necessarie alla sessione.
 * 
 */
public class CaricaSessioneDiGiocoService extends Service<List<GeneratoreDomande.Domanda>> {
    
    private SessioneDiGioco sessione;
    private LivelloPartita livello;
    private DAOListaStopWords dbSW;
    private final DAODocumentoDiTesto dbDT;
    private ArrayList<Lingua> lingua;
    private AnalisiDocumenti analisi;

    /**
     * Costruttore con parametri per inizializzare la sessione di gioco.
     *
     * @param sessione La sessione di gioco da caricare.
     * @param livello Il livello della partita.
     * @param lingua La lista delle lingue supportate.
     */
    public CaricaSessioneDiGiocoService(SessioneDiGioco sessione, LivelloPartita livello, ArrayList<Lingua> lingua) {
        this();
        this.sessione = sessione;
        this.livello = livello;
        this.lingua = lingua; 
    }

    /**
     * Costruttore di default che inizializza le risorse di database e analisi.
     */
    public CaricaSessioneDiGiocoService() {
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
        this.dbSW = DatabaseStopWords.getInstance();
        this.analisi = new AnalisiDocumenti();
    }
    
    // Metodi getter e setter
    
    /**
     * Restituisce l'istanza dell'analisi documenti.
     *
     * @return L'analisi dei documenti.
     */
    public AnalisiDocumenti getAnalisi() {
        return analisi;
    }

    /**
     * Restituisce la sessione di gioco.
     *
     * @return La sessione di gioco.
     */
    public SessioneDiGioco getSessione() {
        return sessione;
    }

    /**
     * Calcola la durata della sessione in base al livello e al numero di parole nei documenti.
     *
     * @param l Il livello della partita.
     * @return La durata calcolata della sessione.
     */
    private int calcolaDurata(LivelloPartita l) {
        int durata = livello.getCalcolaDurataPerLivello();
        int numeroParole = sessione.getDocumenti().values().stream()
                          .mapToInt(b -> (new String(b).split("[\\s,\\n.;!?]+").length))
                          .sum();
        return (int) (durata + (numeroParole * l.getMoltiplicatorePerLivello()));
    }

    /**
     * Seleziona documenti casuali tra quelli disponibili in base al livello specificato.
     *
     * @param nomiDocumenti Lista dei nomi dei documenti.
     * @param l Livello della partita.
     * @return Mappa dei documenti selezionati.
     */
    private Map<String, byte[]> prendiDocumentiCasuali(ArrayList<String> nomiDocumenti, LivelloPartita l) {
        Map<String, byte[]> documentiEstratti = new HashMap<>();
        Collections.shuffle(nomiDocumenti);
        int numeroDoc = Math.min(l.getNumeroDocumenti(), nomiDocumenti.size());
        
        for (int i = 0; i < numeroDoc; i++) {
            documentiEstratti.put(nomiDocumenti.get(i), dbDT.prendiTesto(nomiDocumenti.get(i)));
            generaAnalisi(nomiDocumenti.get(i), documentiEstratti.get(nomiDocumenti.get(i)));
        }
        return documentiEstratti;
    }

    /**
     * Genera l'analisi per un documento specifico.
     *
     * @param NomeDocumento Il nome del documento da analizzare.
     * @param doc Il contenuto del documento.
     */
    public void generaAnalisi(String NomeDocumento, byte[] doc) {
        try {
            analisi.analisiUnDocumento(doc, NomeDocumento);
        } catch (IOException ex) {
            System.getLogger(CaricaSessioneDiGiocoService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    /**
     * Genera un elenco di domande basate sui documenti disponibili.
     *
     * @param Documenti Mappa contenente i documenti.
     * @param analisi Istanza dell'analisi documenti.
     * @param numeroDomande Numero totale di domande da generare.
     * @param docFiltrati Mappa dei documenti filtrati per difficoltà e per lingua in base alle scelte del giocatore
     * @return Una lista di domande generate.
     * @throws MancanzaDiDocumenti Se non ci sono documenti disponibili.
     */
    public List<GeneratoreDomande.Domanda> generaDomande(Map<String, byte[]> Documenti, AnalisiDocumenti analisi, int numeroDomande,Map<String, Lingua> docFiltrati) throws MancanzaDiDocumenti {
        GeneratoreDomande gen = new GeneratoreDomande(analisi);
        String[] nomiDocumenti = Documenti.keySet().toArray(new String[0]);
        
        if (nomiDocumenti.length == 0) {
            throw new MancanzaDiDocumenti("Non ci sono abbastanza documenti");
        }

        List<GeneratoreDomande.Domanda> Domande = new ArrayList<>();
        int domandePerDocumento = numeroDomande / nomiDocumenti.length;
        int documentiExtra = numeroDomande % nomiDocumenti.length;
        
        for (int i = 0; i < nomiDocumenti.length; i++) {
            Domande.addAll(gen.getRaccoltaDiDomande(domandePerDocumento + (i < documentiExtra ? 1 : 0), nomiDocumenti[i],docFiltrati.get(nomiDocumenti[i])));
        }
        return Domande;
    }

    /**
     * Crea un task per il caricamento della sessione di gioco.
     *
     * @return Un task di tipo {@link Task} che esegue il caricamento della sessione.
     */
    @Override
    protected Task<List<GeneratoreDomande.Domanda>> createTask() {
        return new Task<>() {
            @Override
            protected List<GeneratoreDomande.Domanda> call() throws Exception {
                updateProgress(0, 100);
                sessione.setNumeroDocumenti(livello.getNumeroDocumenti());
                sessione.setNumeroDomande(livello.getNumeroDomande());
                updateProgress(30, 100);
                analisi.setStopWords(dbSW.prendiStopwords("ListaStopwords"));
                updateProgress(40, 100);
                Map<String, Lingua> docFiltrati = dbDT.prendiNomiDocumentiFiltrati(livello, lingua);
                ArrayList<String> nomiDoc = new ArrayList<>(docFiltrati.keySet());
                Map<String, byte[]> Documenti = prendiDocumentiCasuali(nomiDoc, livello);
                sessione.setDocumenti(Documenti);
                updateProgress(65, 100);
                List<GeneratoreDomande.Domanda> Domande = generaDomande(sessione.getDocumenti(), analisi, sessione.getNumeroDomande(),docFiltrati);
                sessione.setDomande(Domande);
                updateProgress(70, 100);
                sessione.setDurata(calcolaDurata(livello));
                updateProgress(80, 100);
                sessione.salvaSessioneDiGioco();
                updateProgress(100, 100);
                System.out.println("Matrice: "+ analisi.getMatrice());
                return sessione.getDomande();
            }
        };
    }
}

