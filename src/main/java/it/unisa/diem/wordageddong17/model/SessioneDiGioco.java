package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rappresenta una sessione di gioco nel sistema.
 * 
 * La sessione di gioco incapsula le informazioni relative all'utente, alle domande, alle risposte fornite,
 * ai documenti utilizzati, ai punteggi, alla durata e al livello di difficoltà della partita.
 * Include inoltre funzionalità per il salvataggio esplicito e il caricamento della sessione
 * all'interno della directory applicativa ~/.wordageddon/saves.
 *
 * @author Mattia Sanzari
 */
public class SessioneDiGioco implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessioneDiGioco.class.getName());
    private static final long serialVersionUID = 4L;

    /** L'utente associato a questa sessione di gioco */
    private Utente utente;

    /** Lista delle domande della sessione */
    private List<Domanda> Domande;

    /** Mappa delle risposte date, associando ogni domanda all'indice della risposta selezionata */
    private Map<Domanda, Integer> risposte;

    /** Documenti associati alla sessione: chiave = nome documento, valore = contenuto in byte */
    private Map<String, byte[]> Documenti;

    /** Numero totale di domande per la sessione */
    private int numeroDomande;

    /** Punteggio ottenuto nella sessione */
    private float punteggioFatto;

    /** Durata rimanente della sessione in secondi */
    private int durata;

    /** Numero totale di documenti */
    private int numeroDocumenti;

    /** Array di byte contenente le stop words usate nell'elaborazione */
    private byte[] stopWords;

    /** Durata iniziale della sessione */
    private int durataIniziale;

    /** Livello della partita (facile, medio, difficile) */
    private LivelloPartita livello;

    /**
     * Costruttore di default. Inizializza le strutture dati vuote.
     */
    public SessioneDiGioco() {
        this.Domande = new ArrayList<>();
        this.Documenti = new HashMap<>();
        this.risposte = new HashMap<>();
        this.durata = 0;
        this.durataIniziale = 0;
        this.punteggioFatto = 0;
    }

    /**
     * Costruttore che inizializza la sessione con i parametri specificati.
     *
     * @param numeroDomande   il numero di domande per la sessione
     * @param durata          la durata iniziale della sessione
     * @param utente          l'utente proprietario della sessione
     * @param numeroDocumenti il numero di documenti da utilizzare
     */
    public SessioneDiGioco(int numeroDomande, int durata, Utente utente, int numeroDocumenti) {
        this();
        this.numeroDomande = numeroDomande;
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
        this.utente = utente;
    }

    /**
     * Restituisce il livello di difficoltà della partita.
     *
     * @return il livello di gioco impostato
     */
    public LivelloPartita getLivello() {
        return livello;
    }

    /**
     * Imposta il livello di difficoltà della partita.
     *
     * @param livello il livello di gioco da impostare
     */
    public void setLivello(LivelloPartita livello) {
        this.livello = livello;
    }

    /**
     * Restituisce l'utente associato a questa sessione.
     *
     * @return l'utente corrente
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Associa un utente alla sessione di gioco.
     *
     * @param utente l'utente da associare
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Imposta il numero totale di domande della sessione.
     *
     * @param numeroDomande numero di quesiti
     */
    public void setNumeroDomande(int numeroDomande) {
        this.numeroDomande = numeroDomande;
    }

    /**
     * Imposta il punteggio totalizzato fino a questo momento.
     *
     * @param punteggioFatto punteggio cumulativo
     */
    public void setPunteggioFatto(float punteggioFatto) {
        this.punteggioFatto = punteggioFatto;
    }

    /**
     * Imposta la durata residua della sessione in secondi.
     *
     * @param durata durata residua del timer
     */
    public void setDurata(int durata) {
        this.durata = durata;
    }

    /**
     * Imposta il numero di documenti di testo utilizzati per generare le domande.
     *
     * @param numeroDocumenti numero di testi
     */
    public void setNumeroDocumenti(int numeroDocumenti) {
        this.numeroDocumenti = numeroDocumenti;
    }

    /**
     * Restituisce l'archivio ZIP contenente le stopwords serializzate.
     *
     * @return array di byte del file stopwords
     */
    public byte[] getStopWords() {
        return stopWords;
    }

    /**
     * Imposta l'archivio contenente le stopwords.
     *
     * @param stopWords array di byte delle stopwords
     */
    public void setStopWords(byte[] stopWords) {
        this.stopWords = stopWords;
    }

    /**
     * Restituisce la durata residua in secondi.
     *
     * @return secondi rimanenti
     */
    public int getDurata() {
        return durata;
    }

    /**
     * Restituisce il numero di documenti di testo caricati nella sessione.
     *
     * @return quantità di documenti
     */
    public int getNumeroDocumenti() {
        return numeroDocumenti;
    }

    /**
     * Restituisce la mappa delle risposte date dal giocatore per ciascuna domanda.
     *
     * @return mappa con associazione domanda - indice risposta
     */
    public Map<Domanda, Integer> getRisposte() {
        return risposte;
    }

    /**
     * Restituisce il punteggio maturato finora nella partita.
     *
     * @return punteggio attuale
     */
    public float getPunteggioFatto() {
        return punteggioFatto;
    }

    /**
     * Restituisce l'elenco delle domande generate per la sessione.
     *
     * @return lista di domande
     */
    public List<Domanda> getDomande() {
        return Domande;
    }

    /**
     * Restituisce la durata iniziale in secondi assegnata alla partita in base alla difficoltà.
     *
     * @return durata iniziale in secondi
     */
    public int getDurataIniziale() {
        return durataIniziale;
    }

    /**
     * Imposta la mappa dei documenti caricati per l'analisi.
     *
     * @param Documenti mappa titolo - contenuto binario dei documenti
     */
    public void setDocumenti(Map<String, byte[]> Documenti) {
        this.Documenti = Documenti;
    }

    /**
     * Imposta l'elenco delle domande della partita.
     *
     * @param Domande lista di domande
     */
    public void setDomande(List<Domanda> Domande) {
        this.Domande = Domande;
    }

    /**
     * Aggiunge un documento di testo alla mappa dei documenti della sessione.
     *
     * @param chiave titolo o identificativo del documento
     * @param Documento contenuto binario del testo
     */
    public void addDocumenti(String chiave, byte[] Documento) {
        this.Documenti.put(chiave, Documento);
    }

    /**
     * Restituisce la mappa dei documenti di testo della sessione.
     *
     * @return mappa titolo - contenuto binario
     */
    public Map<String, byte[]> getDocumenti() {
        return Documenti;
    }

    /**
     * Restituisce il numero complessivo di domande previste dal quiz.
     *
     * @return numero di quesiti
     */
    public int getNumeroDomande() {
        return numeroDomande;
    }

    /**
     * Imposta la durata iniziale in secondi assegnata alla partita.
     *
     * @param durataIniziale durata di partenza
     */
    public void setDurataIniziale(int durataIniziale) {
        this.durataIniziale = durataIniziale;
    }

    // -------------------------------------------------------------------------
    // Gestione Cartella di Salvataggio Applicativa (~/.wordageddon/saves)
    // -------------------------------------------------------------------------

    /**
     * Restituisce il percorso della cartella applicativa dedicata ai salvataggi utente.
     *
     * @return percorso directory salvataggi
     */
    public static Path getSaveDirectory() {
        String userHome = System.getProperty("user.home", ".");
        Path dir = Paths.get(userHome, ".wordageddon", "saves");
        try {
            Files.createDirectories(dir);
            return dir;
        } catch (IOException e) {
            return Paths.get(".");
        }
    }

    /**
     * Restituisce il descrittore del file di salvataggio associato a una determinata email.
     *
     * @param email indirizzo email del giocatore
     * @return file del salvataggio serializzato
     */
    public static File getSaveFile(String email) {
        if (email == null) return new File("save_anonymous.ser");
        String safeEmail = email.replaceAll("[^a-zA-Z0-9.-]", "_");
        return getSaveDirectory().resolve("save_" + safeEmail + ".ser").toFile();
    }

    /**
     * Verifica l'esistenza di un salvataggio valido su disco per l'utente specificato.
     *
     * @param email indirizzo email dell'utente
     * @return {@code true} se esiste un salvataggio non vuoto, {@code false} altrimenti
     */
    public static boolean esisteSalvataggio(String email) {
        if (email == null) return false;
        File appSave = getSaveFile(email);
        if (appSave.exists() && appSave.length() > 0) return true;

        // Retrocompatibilità con i salvataggi nella CWD
        return new File("SalvataggioDi" + email + ".ser").exists() ||
               new File("SalvataggioFaseGenerazioneDi" + email + ".ser").exists();
    }

    /**
     * Elimina il file di salvataggio dell'utente da disco al termine o annullamento della partita.
     *
     * @param email indirizzo email dell'utente
     * @return {@code true} se almeno un file di salvataggio è stato cancellato con successo
     */
    public static boolean eliminaSalvataggio(String email) {
        if (email == null) return false;
        boolean b1 = getSaveFile(email).delete();
        boolean b2 = new File("SalvataggioDi" + email + ".ser").delete();
        boolean b3 = new File("SalvataggioFaseGenerazioneDi" + email + ".ser").delete();
        return b1 || b2 || b3;
    }

    /**
     * Salva esplicitamente lo stato della sessione su disco.
     */
    public synchronized void salvaSessioneDiGioco() {
        if (this.utente == null || this.utente.getEmail() == null) {
            LOGGER.warning("Impossibile salvare la sessione: utente o email non definiti.");
            return;
        }
        File targetFile = getSaveFile(this.utente.getEmail());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile))) {
            oos.writeObject(this);
            LOGGER.info(() -> "Sessione salvata con successo in " + targetFile.getAbsolutePath());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Errore salvataggio sessione di gioco su file", ex);
        }
    }

    /**
     * Metodo di retrocompatibilità: accetta un nome file e salva nella directory designata.
     *
     * @param nomeFile nome o percorso del file di destinazione (ignorato in favore del path standard applicativo)
     */
    public synchronized void salvaSessioneDiGioco(String nomeFile) {
        salvaSessioneDiGioco();
    }

    /**
     * Carica lo stato della sessione dal file di salvataggio per l'utente specificato.
     *
     * @param email email dell'utente
     * @throws IOException se il caricamento fallisce
     */
    public synchronized void caricaSessioneDiGiocoPerEmail(String email) throws IOException {
        File fileDaLeggere = getSaveFile(email);
        if (!fileDaLeggere.exists()) {
            File fOld1 = new File("SalvataggioDi" + email + ".ser");
            File fOld2 = new File("SalvataggioFaseGenerazioneDi" + email + ".ser");
            if (fOld1.exists()) {
                fileDaLeggere = fOld1;
            } else if (fOld2.exists()) {
                fileDaLeggere = fOld2;
            }
        }

        if (!fileDaLeggere.exists()) {
            throw new IOException("Nessun file di salvataggio trovato per l'utente " + email);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileDaLeggere))) {
            SessioneDiGioco s = (SessioneDiGioco) ois.readObject();
            this.utente = s.getUtente();
            this.Domande = s.getDomande() != null ? s.getDomande() : new ArrayList<>();
            this.risposte = s.getRisposte() != null ? s.getRisposte() : new HashMap<>();
            this.Documenti = s.getDocumenti() != null ? s.getDocumenti() : new HashMap<>();
            this.numeroDomande = s.numeroDomande;
            this.punteggioFatto = s.getPunteggioFatto();
            this.durata = s.getDurata();
            this.numeroDocumenti = s.getNumeroDocumenti();
            this.stopWords = s.getStopWords();
            this.durataIniziale = s.getDurataIniziale();
            this.livello = s.getLivello();
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Classe non trovata durante la deserializzazione della sessione", ex);
            throw new IOException("Formato di salvataggio non compatibile", ex);
        }
    }

    /**
     * Retrocompatibilità: delega a caricaSessioneDiGiocoPerEmail estraendo l'email o usandola come path.
     *
     * @param nomeFile nome o percorso del file di salvataggio da cui caricare lo stato
     * @throws IOException se si verifica un errore durante la lettura o il caricamento del file
     */
    public synchronized void caricaSessioneDiGioco(String nomeFile) throws IOException {
        if (this.utente != null && this.utente.getEmail() != null) {
            caricaSessioneDiGiocoPerEmail(this.utente.getEmail());
        } else {
            File f = new File(nomeFile);
            if (!f.exists()) {
                f = getSaveDirectory().resolve(nomeFile).toFile();
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                SessioneDiGioco s = (SessioneDiGioco) ois.readObject();
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
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Errore deserializzazione", ex);
                throw new IOException(ex);
            }
        }
    }

    /**
     * Registra la risposta fornita dall'utente per una determinata domanda.
     *
     * @param numeroDomanda l'indice della domanda nella lista
     * @param indiceRisposta l'indice della risposta selezionata
     */
    public synchronized void aggiornaRisposte(int numeroDomanda, int indiceRisposta) {
        if (numeroDomanda >= 0 && numeroDomanda < this.Domande.size()) {
            this.risposte.put(this.Domande.get(numeroDomanda), indiceRisposta);
        }
    }

    // PARAMETRI REGOLABILI
    private static final float PUNTI_BASE_PER_DOMANDA = 1000.0f;
    private static final float PENALITA_PER_ERRORE = 0.5f;
    private static final float BONUS_TEMPO_MAX_PERC = 0.15f;
    private static final float ESPONENTE_BONUS_TEMPO = 0.5f;
    private static final float SOGLIA_MINIMA_PRECISIONE = 0.01f;

    /**
     * Aggiorna il punteggio fatto dall'utente in base alle risposte corrette e al tempo rimanente.
     *
     * @param tempoRimasto il tempo rimasto (in secondi) al termine della sessione
     */
    public void aggiornaPuntiFatti(int tempoRimasto) {
        int numDomande = this.Domande.size();
        if (numDomande == 0) {
            this.punteggioFatto = 0;
            return;
        }

        int numRisposteCorrette = 0;
        for (Domanda d : this.Domande) {
            Integer data = this.risposte.get(d);
            if (data != null && data == d.rispostaCorretta) {
                numRisposteCorrette++;
            }
        }
        int numRisposteSbagliate = numDomande - numRisposteCorrette;

        float precisioneLorda = (float) numRisposteCorrette / numDomande;
        float penalizzazione = (float) numRisposteSbagliate / numDomande * PENALITA_PER_ERRORE;
        float punteggioNetto = Math.max(0f, precisioneLorda - penalizzazione);

        float puntiMassimi = PUNTI_BASE_PER_DOMANDA * numDomande;
        float puntBase = puntiMassimi * punteggioNetto;

        float bonusTempo = 0;
        if (this.durataIniziale > 0 && punteggioNetto >= SOGLIA_MINIMA_PRECISIONE) {
            float tempoRatio = Math.max(0f, (float) tempoRimasto / this.durataIniziale);
            float fattoreTempo = (float) Math.pow(tempoRatio, ESPONENTE_BONUS_TEMPO);
            bonusTempo = puntiMassimi * BONUS_TEMPO_MAX_PERC * fattoreTempo * punteggioNetto;
        }

        this.punteggioFatto = Math.round(puntBase + bonusTempo);
    }

    @Override
    public String toString() {
        return "SessioneDiGioco{" +
                "utente=" + (utente != null ? utente.getEmail() : "null") +
                ", numeroDomande=" + numeroDomande +
                ", punteggioFatto=" + punteggioFatto +
                ", durata=" + durata +
                ", livello=" + livello +
                '}';
    }
}
