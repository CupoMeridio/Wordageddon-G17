package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rappresenta una sessione di gioco nel sistema.
 * 
 * La sessione di gioco incapsula le informazioni relative all'utente, alle domande, alle risposte fornite,
 * ai documenti utilizzati, ai punteggi, alla durata e al livello di difficoltà della partita.
 * Include inoltre funzionalità di salvataggio e caricamento della sessione in modo da consentire il ripristino
 * in caso di interruzioni inaspettate.
 *
 * @author Mattia Sanzari
 */
public class SessioneDiGioco implements Serializable {
    private static final long serialVersionUID = 3L; // Versione per serializzazione

    /** L'utente associato a questa sessione di gioco */
    private Utente utente;

    /** Lista delle domande della sessione */
    private List<Domanda> Domande;

    /** Mappa delle risposte date, associando ogni domanda al valore intero risposta */
    private Map<Domanda, Integer> risposte;

    /** Documenti associati alla sessione: chiave = nome documento, valore = contenuto in byte */
    private Map<String, byte[]> Documenti;

    /** Numero totale di domande per documento */
    private int numeroDomande;

    /** Punteggio ottenuto nella sessione */
    private float punteggioFatto;

    /** Durata della sessione di gioco (in secondi o altro unità) */
    private int durata;

    /** Numero totale di documenti */
    private int numeroDocumenti;

    /** Array di byte contenente le stop words usate nell'elaborazione */
    private byte[] stopWords;

    /** Durata iniziale della sessione */
    private int durataIniziale;

    /** Livello della partita (es. facile, medio, difficile) */
    private LivelloPartita livello;


    /**
     * Costruttore di default.
     * 
     * Inizializza le liste e le mappe necessarie e imposta la durata a 0. 
     * Aggiunge anche un shutdown hook per salvare automaticamente la sessione in caso di interruzione brusca,
     * qualora non tutte le risposte siano state registrate.
     * 
     */
    public SessioneDiGioco() {
        this.Domande = new ArrayList<>();
        this.Documenti = new HashMap<>();
        this.risposte = new HashMap<>();
        this.durata = 0;
        this.durataIniziale = 0;
        // Salvataggio automatico se la sessione viene interrotta bruscamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Thread salva = new Thread(() -> {
                System.out.println("Inizio salvataggio");
                this.salvaSessioneDiGioco("SalvataggioDi" + this.utente.getEmail() + ".ser");
                System.out.println("Fine salvataggio");
            });

            // Salva la sessione solo se il numero di domande non corrisponde al numero di risposte registrate
            if (this.numeroDomande != this.risposte.size())
                salva.start();
            try {
                salva.join(1000);
                if (salva.isAlive()) {
                    System.out.println("Non hai avuto tempo");
                    salva.interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
    }

    /**
     * Costruttore che inizializza la sessione con i parametri specificati.
     *
     * @param numeroDomande   il numero di domande per documento
     * @param durata          la durata iniziale della sessione
     * @param utente          l'utente proprietario della sessione
     * @param numeroDocumenti il numero di documenti da utilizzare
     */
    public SessioneDiGioco(int numeroDomande, int durata, Utente utente, int numeroDocumenti) {
        this.numeroDomande = numeroDomande;
        this.Domande = new ArrayList<>();
        this.Documenti = new HashMap<>();
        this.risposte = new HashMap<>();
        this.stopWords = null;
        this.punteggioFatto = 0;
        this.durata = durata;
        this.durataIniziale = durata;
        this.utente = utente;
        this.numeroDocumenti = numeroDocumenti;
    }

    /**
     * Costruttore che crea una sessione per un dato utente.
     *
     * @param utente l'utente per cui viene creata la sessione
     */
    public SessioneDiGioco(Utente utente) {
        this();
        this.stopWords = null;
        this.utente = utente;
        this.durata = 0;
        this.durataIniziale = 0;
    }

    /**
     * Imposta il livello di difficoltà della sessione.
     *
     * @param livello il livello di partita da impostare
     */
    public void setLivello(LivelloPartita livello) {
        this.livello = livello;
    }

    /**
     * Restituisce il livello di difficoltà della sessione.
     *
     * @return il livello di partita
     */
    public LivelloPartita getLivello() {
        return livello;
    }

    /**
     * Imposta il numero di domande per documento.
     *
     * @param numeroDomande il numero di domande
     */
    public void setNumeroDomande(int numeroDomande) {
        this.numeroDomande = numeroDomande;
    }

    /**
     * Imposta il numero di documenti da utilizzare.
     *
     * @param numeroDocumenti il numero di documenti
     */
    public void setNumeroDocumenti(int numeroDocumenti) {
        this.numeroDocumenti = numeroDocumenti;
    }

    /**
     * Imposta la durata rimanente della sessione.
     * 
     * Se la durata corrente e quella iniziale sono entrambe pari a zero, imposta la durata iniziale.
     * Questa operazione è sincronizzata per garantire la consistenza dei dati in un ambiente concorrente.
     *
     * @param durata la nuova durata della sessione
     */
    public synchronized void setDurata(int durata) {
        if (this.durata == 0 && this.durataIniziale == 0)
            this.durataIniziale = durata;
        System.out.println("Dopo :" + this.durata);
        this.durata = durata;
        System.out.println("Dopo :" + this.durata);
    }

    /**
     * Restituisce le stopwords associate alla sessione.
     *
     * @return un array di byte contenente le stopwords
     */
    public byte[] getStopWords() {
        return stopWords;
    }

    /**
     * Restituisce l'utente associato alla sessione.
     *
     * @return l'oggetto Utente della sessione
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Restituisce la durata rimanente della sessione.
     *
     * @return la durata in secondi
     */
    public int getDurata() {
        return durata;
    }

    /**
     * Restituisce il numero di documenti utilizzati nella sessione.
     *
     * @return il numero di documenti
     */
    public int getNumeroDocumenti() {
        return numeroDocumenti;
    }

    /**
     * Restituisce la mappa delle risposte fornite.
     *
     * @return una mappa in cui le chiavi sono le domande e i valori sono gli indici delle risposte selezionate
     */
    public Map<Domanda, Integer> getRisposte() {
        return risposte;
    }

    /**
     * Restituisce il punteggio totale accumulato nella sessione.
     *
     * @return il punteggio fatto
     */
    public float getPunteggioFatto() {
        return punteggioFatto;
    }

    /**
     * Restituisce la lista delle domande della sessione.
     *
     * @return la lista di Domanda
     */
    public List<Domanda> getDomande() {
        return Domande;
    }

    /**
     * Restituisce la durata iniziale della sessione.
     *
     * @return la durata iniziale
     */
    public int getDurataIniziale() {
        return durataIniziale;
    }

    /**
     * Imposta la mappa dei documenti della sessione.
     *
     * @param Documenti una mappa in cui la chiave è il nome del documento e il valore è il contenuto in byte
     */
    public void setDocumenti(Map<String, byte[]> Documenti) {
        this.Documenti = Documenti;
    }

    /**
     * Imposta la lista delle domande della sessione.
     *
     * @param Domande la lista delle domande da impostare
     */
    public void setDomande(List<Domanda> Domande) {
        this.Domande = Domande;
    }

    /**
     * Aggiunge un documento alla mappa dei documenti della sessione.
     *
     * @param chiave    il nome del documento
     * @param Documento il contenuto del documento in byte
     */
    public void addDocumenti(String chiave, byte[] Documento) {
        this.Documenti.put(chiave, Documento);
    }

    /**
     * Restituisce la mappa dei documenti della sessione.
     *
     * @return una mappa in cui le chiavi sono i nomi dei documenti e i valori sono i relativi contenuti in byte
     */
    public Map<String, byte[]> getDocumenti() {
        return Documenti;
    }

    /**
     * Restituisce il numero di domande configurate per la sessione.
     *
     * @return il numero di domande
     */
    public int getNumeroDomande() {
        return numeroDomande;
    }

    /**
     * Salva la sessione di gioco su file, utilizzando il nome di file specificato.
     *
     * @param NomeFile il nome del file in cui salvare la sessione
     */
    public void salvaSessioneDiGioco(String NomeFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NomeFile))) {
            System.out.println("Inizio salvataggio");
            oos.writeObject(this);
            System.out.println("Fine salvataggio");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Salva la sessione di gioco su file, utilizzando un nome di file predefinito in base all'email dell'utente.
     */
    public void salvaSessioneDiGioco() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SalvataggioFaseGenerazioneDi" + this.utente.getEmail() + ".ser"))) {
            System.out.println("Inizio salvataggio");
            oos.writeObject(this);
            System.out.println("Fine salvataggio");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Registra la risposta fornita dall'utente per una determinata domanda.
     *
     * @param numeroDomanda l'indice della domanda nella lista
     * @param indiceRisposta l'indice della risposta selezionata
     */
    public void aggiornaRisposte(int numeroDomanda, int indiceRisposta) {
        this.risposte.put(this.Domande.get(numeroDomanda), indiceRisposta);
    }
    
    //PARAMETRI REGOLABILI (possono essere modificati per bilanciare il gioco)
    /** Punteggio attribuito ad ogni risposta corretta */
    private static final float PUNTI_BASE_PER_DOMANDA = 1000.0f;
    /** Punteggio detratto per ogni risposta errata */
    private static final float PENALITA_PER_ERRORE = 0.5f; // 0 = no penalty, 1 = full penalty
    /** Limite superiore per il bonus attributo al tempo rimanente */
    private static final float BONUS_TEMPO_MAX_PERC = 0.15f; // % del punteggio massimo
    /** Andamento del bonus per il tempo rimanente */
    private static final float ESPONENTE_BONUS_TEMPO = 0.5f; // 0.5=sqrt, 1=lineare, 2=quadratico
    /** Limite inferiore per l'attribuzione del bonus basato sulla precisione */
    private static final float SOGLIA_MINIMA_PRECISIONE = 0.01f; // Precisione minima per avere bonus
    
    /**
     * Aggiorna il punteggio fatto dall'utente in base alle risposte corrette e al tempo rimanente.
     * 
     * Il metodo itera sulle domande della sessione e verifica se la risposta fornita corrisponde a quella corretta.
     * In base al numero di risposte corrette, calcola un punteggio di base e un bonus in base al tempo rimanente.
     * Se nessuna risposta è corretta, il punteggio viene impostato a zero.
     *
     * @param tempoRimasto il tempo rimasto (in secondi) al termine della sessione
     */
    public void aggiornaPuntiFatti(int tempoRimasto) {
        int numDomande = this.Domande.size();
        if (numDomande == 0) {
            this.punteggioFatto = 0;
            return;
        }

        // Calcolo risposte corrette/errate (tutte le domande hanno risposta)
        int numRisposteCorrette = 0;
        for (Domanda d : this.Domande) {
            if (this.risposte.get(d) == d.rispostaCorretta) {
                numRisposteCorrette++;
            }
        }
        int numRisposteSbagliate = numDomande - numRisposteCorrette;

        // 1. CALCOLO PRECISIONE CON PENALITÀ REGOLABILE
        float precisioneLorda = (float) numRisposteCorrette / numDomande;
        float penalizzazione = (float) numRisposteSbagliate / numDomande * PENALITA_PER_ERRORE;
        float punteggioNetto = Math.max(0f, precisioneLorda - penalizzazione);

        // 2. PUNTEGGIO BASE SCALABILE
        float puntiMassimi = PUNTI_BASE_PER_DOMANDA * numDomande;
        float puntBase = puntiMassimi * punteggioNetto;

        // 3. BONUS TEMPO CON PARAMETRI REGOLABILI
        float bonusTempo = 0;
        if (this.durataIniziale > 0 && punteggioNetto >= SOGLIA_MINIMA_PRECISIONE) {
            float tempoRatio = (float) tempoRimasto / this.durataIniziale;
            float fattoreTempo = (float) Math.pow(tempoRatio, ESPONENTE_BONUS_TEMPO);
            bonusTempo = puntiMassimi * BONUS_TEMPO_MAX_PERC * fattoreTempo * punteggioNetto;
        }

        this.punteggioFatto = Math.round(puntBase + bonusTempo);
    }

    /**
     * Carica la sessione di gioco da un file serializzato.
     *
     * @param NomeFile il nome del file da cui caricare la sessione
     * @throws IOException se si verifica un errore durante la lettura del file
     */
    public void caricaSessioneDiGioco(String NomeFile) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NomeFile))) {
            System.out.println("Inizio caricamento");
            SessioneDiGioco s = (SessioneDiGioco) ois.readObject();
            System.out.println("s:" + s.toString());
            this.utente = s.getUtente();
            this.Domande = s.getDomande();
            this.risposte = s.getRisposte();
            this.Documenti = s.getDocumenti();
            this.numeroDomande = s.numeroDomande;
            this.punteggioFatto = s.getPunteggioFatto();
            this.durata = s.getDurata();
            this.numeroDocumenti = s.getNumeroDocumenti();
            this.stopWords = s.getStopWords();
            this.durataIniziale = s.getDurataIniziale();
            this.livello = s.getLivello();
            System.out.println("Fine caricamento");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Salva un documento in locale come file.
     * 
     * Questo metodo è attualmente non utilizzato; può essere eliminato in futuro se non necessario.
     *
     * @param nomedoc    il nome del file da creare
     * @param documento  il contenuto del documento da salvare in byte
     */
    private void SalvaDocumentiInLocale(String nomedoc, byte[] documento) {
        try (FileOutputStream fos = new FileOutputStream(nomedoc)) {
            fos.write(documento);
            System.out.println("Scrittura completata!");
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }

    /**
     * Restituisce una rappresentazione stringa della sessione di gioco.
     *
     * @return una stringa contenente i dettagli della sessione, inclusi utente, domande, risposte, documenti, punteggio e durata.
     */
    @Override
    public String toString() {
        return "SessioneDiGioco{" +
               "utente=" + utente +
               ", Domande=" + Domande +
               ", risposte=" + risposte +
               ", Documenti=" + Documenti +
               ", numeroDomande=" + numeroDomande +
               ", punteggioFatto=" + punteggioFatto +
               ", durata=" + durata +
               ", numeroDocumenti=" + numeroDocumenti +
               ", stopWords=" + stopWords +
               ", durataIniziale=" + durataIniziale +
               ", livello=" + livello +
               '}';
    }
}
