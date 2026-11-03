package it.unisa.diem.wordageddong17.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe che si occupa di generare domande a risposta multipla basate
 * sull'analisi di un documento. Le domande sono di vario tipo: frequenza
 * assoluta, confronto, esclusione, parola più/meno frequente.
 *
 * <p>La classe garantisce i seguenti invarianti:</p>
 * <ul>
 *   <li>ogni domanda ha esattamente 4 opzioni di risposta distinte;</li>
 *   <li>esiste esattamente una sola risposta corretta priva di ambiguità (nessun pareggio per il massimo/minimo);</li>
 *   <li>la raccolta di domande non contiene mai valori nulli.</li>
 * </ul>
 */
public class GeneratoreDomande {

    private final AnalisiDocumenti analisi;
    private final Random rnd = new Random();

    /**
     * Costruttore del generatore di domande.
     *
     * @param analisi Oggetto AnalisiDocumenti già popolato con i dati del documento
     */
    public GeneratoreDomande(AnalisiDocumenti analisi) {
        this.analisi = Objects.requireNonNull(analisi, "L'analisi dei documenti non può essere nulla");
    }

    /**
     * Classe immutabile che rappresenta una domanda a risposta multipla.
     */
    public static class Domanda implements Serializable {
        private static final long serialVersionUID = 1L;

        /** Testo della domanda */
        public final String testo;

        /** Lista immutabile delle 4 opzioni di risposta */
        public final List<String> opzioni;

        /** Indice (0-3) della risposta corretta nella lista delle opzioni */
        public final int rispostaCorretta;

        /**
         * Costruttore della domanda.
         *
         * @param testo Testo della domanda
         * @param opzioni Lista delle opzioni di risposta (deve contenere esattamente 4 elementi)
         * @param rispostaCorretta Indice della risposta corretta (0..3)
         */
        public Domanda(String testo, List<String> opzioni, int rispostaCorretta) {
            this.testo = Objects.requireNonNull(testo, "Il testo non può essere nullo");
            if (opzioni == null || opzioni.size() != 4) {
                throw new IllegalArgumentException("La domanda deve avere esattamente 4 opzioni");
            }
            if (rispostaCorretta < 0 || rispostaCorretta >= opzioni.size()) {
                throw new IndexOutOfBoundsException("Indice risposta corretta non valido: " + rispostaCorretta);
            }
            this.opzioni = Collections.unmodifiableList(new ArrayList<>(opzioni));
            this.rispostaCorretta = rispostaCorretta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Domanda domanda = (Domanda) o;
            return rispostaCorretta == domanda.rispostaCorretta &&
                    Objects.equals(testo, domanda.testo) &&
                    Objects.equals(opzioni, domanda.opzioni);
        }

        @Override
        public int hashCode() {
            return Objects.hash(testo, opzioni, rispostaCorretta);
        }

        @Override
        public String toString() {
            return "Domanda{" +
                    "testo='" + testo + '\'' +
                    ", opzioni=" + opzioni +
                    ", rispostaCorretta=" + rispostaCorretta +
                    '}';
        }
    }

    /**
     * Pulisce il nome del file rimuovendo l'estensione per una visualizzazione gradevole nel testo del quiz.
     */
    private static String stripExtension(String nomeDocumento) {
        if (nomeDocumento == null) return "";
        int dotIdx = nomeDocumento.lastIndexOf('.');
        return dotIdx > 0 ? nomeDocumento.substring(0, dotIdx) : nomeDocumento;
    }

    /**
     * Genera una domanda sulla frequenza assoluta di una parola nel documento.
     * Esempio: "Quante volte compare la parola 'X' nel documento?"
     *
     * @param nomeDocumento Nome del documento
     * @return Domanda valida con 4 opzioni numeriche, oppure null se il documento è vuoto
     */
    private Domanda domandaFrequenzaAssoluta(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        if (parole == null || parole.isEmpty()) {
            return null;
        }

        List<String> paroleDoc = new ArrayList<>(parole.keySet());
        String parola = paroleDoc.get(rnd.nextInt(paroleDoc.size()));
        int freq = parole.get(parola);

        // Genera 3 distrattori numerici distinti e non negativi
        Set<Integer> opzioniSet = new HashSet<>();
        opzioniSet.add(freq);

        int delta = 1;
        while (opzioniSet.size() < 4) {
            int candidata1 = freq + delta;
            int candidata2 = Math.max(0, freq - delta);
            if (candidata1 != freq) opzioniSet.add(candidata1);
            if (opzioniSet.size() < 4 && candidata2 != freq) opzioniSet.add(candidata2);
            delta++;
        }

        List<Integer> opzioniNum = new ArrayList<>(opzioniSet);
        Collections.shuffle(opzioniNum);
        int idx = opzioniNum.indexOf(freq);

        List<String> opzioni = opzioniNum.stream().map(String::valueOf).collect(Collectors.toList());
        String testo = String.format("Quante volte compare la parola \"%s\" nel documento \"%s\"?",
                parola, stripExtension(nomeDocumento));
        return new Domanda(testo, opzioni, idx);
    }

    /**
     * Genera una domanda sulla parola più frequente nel documento.
     * Garantisce che la parola corretta abbia una frequenza strettamente maggiore dei 3 distrattori.
     */
    private Domanda domandaParolaPiuFrequente(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        if (parole == null || parole.size() < 4) {
            return null;
        }

        int maxFreq = Collections.max(parole.values());
        List<String> paroleMax = parole.entrySet().stream()
                .filter(e -> e.getValue() == maxFreq)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Se troppe parole hanno la frequenza massima e non ci sono almeno 3 parole con frequenza inferiore,
        // la domanda risulterebbe ambigua.
        List<String> paroleMinori = parole.entrySet().stream()
                .filter(e -> e.getValue() < maxFreq)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (paroleMinori.size() < 3) {
            return null;
        }

        String parolaScelta = paroleMax.get(rnd.nextInt(paroleMax.size()));
        Collections.shuffle(paroleMinori);

        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaScelta);
        opzioni.add(paroleMinori.get(0));
        opzioni.add(paroleMinori.get(1));
        opzioni.add(paroleMinori.get(2));

        Collections.shuffle(opzioni);
        int idx = opzioni.indexOf(parolaScelta);

        String testo = String.format("Quale parola compare più spesso nel documento \"%s\"?",
                stripExtension(nomeDocumento));
        return new Domanda(testo, opzioni, idx);
    }

    /**
     * Genera una domanda sulla parola meno frequente nel documento.
     * Garantisce che la parola corretta abbia una frequenza strettamente minore dei 3 distrattori.
     */
    private Domanda domandaParolaMenoFrequente(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        if (parole == null || parole.size() < 4) {
            return null;
        }

        int minFreq = Collections.min(parole.values());
        List<String> paroleMin = parole.entrySet().stream()
                .filter(e -> e.getValue() == minFreq)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> paroleMaggiori = parole.entrySet().stream()
                .filter(e -> e.getValue() > minFreq)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (paroleMaggiori.size() < 3) {
            return null;
        }

        String parolaScelta = paroleMin.get(rnd.nextInt(paroleMin.size()));
        Collections.shuffle(paroleMaggiori);

        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaScelta);
        opzioni.add(paroleMaggiori.get(0));
        opzioni.add(paroleMaggiori.get(1));
        opzioni.add(paroleMaggiori.get(2));

        Collections.shuffle(opzioni);
        int idx = opzioni.indexOf(parolaScelta);

        String testo = String.format("Quale parola compare meno spesso nel documento \"%s\"?",
                stripExtension(nomeDocumento));
        return new Domanda(testo, opzioni, idx);
    }

    /**
     * Genera una domanda di confronto tra 4 parole del documento.
     * Garantisce che tra le 4 parole selezionate ci sia una sola vincitrice assoluta
     * con frequenza strettamente maggiore delle altre 3 (nessun pareggio per il primo posto).
     */
    private Domanda domandaConfrontoFrequenze(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        if (parole == null || parole.size() < 4) {
            return null;
        }

        List<String> paroleDoc = new ArrayList<>(parole.keySet());

        // Tenta fino a 10 volte di estrarre una quaterna con un unico massimo non ambiguo
        for (int tentativi = 0; tentativi < 10; tentativi++) {
            Collections.shuffle(paroleDoc);
            List<String> scelte = new ArrayList<>(paroleDoc.subList(0, 4));

            // Trova la frequenza massima nella quaterna
            int maxFreq = scelte.stream().mapToInt(parole::get).max().orElse(-1);
            long countMax = scelte.stream().filter(w -> parole.get(w) == maxFreq).count();

            // Se esattamente una parola ha maxFreq, abbiamo una risposta univoca e priva di ambiguità
            if (countMax == 1) {
                String parolaMax = scelte.stream().filter(w -> parole.get(w) == maxFreq).findFirst().get();
                int idx = scelte.indexOf(parolaMax);
                String testo = String.format("Quale tra queste parole è la più frequente nel documento \"%s\"?",
                        stripExtension(nomeDocumento));
                return new Domanda(testo, scelte, idx);
            }
        }
        return null;
    }

    /** Dizionario multilingua di supporto per le parole assenti */
    private static final Map<Lingua, List<String>> DIZIONARIO_PAROLE = new EnumMap<>(Lingua.class);
    static {
        DIZIONARIO_PAROLE.put(Lingua.ITALIANO, Arrays.asList(
                "CASA", "LIBRO", "TAVOLO", "SEDIA", "FINESTRA", "PORTA", "GIARDINO", "ALBERO",
                "FIORE", "SOLE", "LUNA", "STELLA", "MARE", "MONTAGNA", "FIUME", "LAGO",
                "CANE", "GATTO", "UCCELLO", "PESCE", "CAVALLO", "MUCCA", "PECORA", "MAIALE",
                "AUTO", "TRENO", "AEREO", "BICICLETTA", "MOTO", "NAVE", "AUTOBUS", "TRAM",
                "SCUOLA", "UNIVERSITÀ", "OSPEDALE", "CINEMA", "TEATRO", "MUSEO", "BIBLIOTECA",
                "AMORE", "ODIO", "FELICITÀ", "TRISTEZZA", "PAURA", "CORAGGIO", "SPERANZA", "TEMPO"
        ));
        DIZIONARIO_PAROLE.put(Lingua.INGLESE, Arrays.asList(
                "HOUSE", "BOOK", "COMPUTER", "CHAIR", "WINDOW", "DOOR", "GARDEN", "TREE",
                "FLOWER", "SUN", "MOON", "STAR", "OCEAN", "MOUNTAIN", "RIVER", "LAKE",
                "DOG", "CAT", "BIRD", "FISH", "HORSE", "COW", "SHEEP", "PIG",
                "CAR", "TRAIN", "AIRPLANE", "BICYCLE", "MOTORCYCLE", "SHIP", "BUS", "SUBWAY",
                "SCHOOL", "UNIVERSITY", "HOSPITAL", "CINEMA", "THEATER", "MUSEUM", "LIBRARY",
                "LOVE", "HATE", "HAPPINESS", "SADNESS", "FEAR", "COURAGE", "HOPE", "TIME"
        ));
        DIZIONARIO_PAROLE.put(Lingua.FRANCESE, Arrays.asList(
                "MAISON", "LIVRE", "TABLE", "CHAISE", "FENÊTRE", "PORTE", "JARDIN", "ARBRE",
                "FLEUR", "SOLEIL", "LUNE", "ÉTOILE", "MER", "MONTAGNE", "RIVIÈRE", "LAC",
                "CHIEN", "CHAT", "OISEAU", "POISSON", "CHEVAL", "VACHE", "MOUTON", "COCHON",
                "VOITURE", "TRAIN", "AVION", "VÉLO", "MOTO", "BATEAU", "BUS", "MÉTRO",
                "ÉCOLE", "UNIVERSITÉ", "HÔPITAL", "CINÉMA", "THÉÂTRE", "MUSÉE", "BIBLIOTHÈQUE",
                "AMOUR", "HAINE", "BONHEUR", "TRISTESSE", "PEUR", "COURAGE", "ESPOIR", "TEMPS"
        ));
        DIZIONARIO_PAROLE.put(Lingua.TEDESCO, Arrays.asList(
                "HAUS", "BUCH", "TISCH", "STUHL", "FENSTER", "TÜR", "GARTEN", "BAUM",
                "BLUME", "SONNE", "MOND", "STERN", "MEER", "BERG", "FLUSS", "SEE",
                "HUND", "KATZE", "VOGEL", "FISCH", "PFERD", "KUH", "SCHAF", "SCHWEIN",
                "AUTO", "ZUG", "FLUGZEUG", "FAHRRAD", "MOTORRAD", "SCHIFF", "BUS", "BAHN",
                "SCHULE", "UNIVERSITÄT", "KRANKENHAUS", "KINO", "THEATER", "MUSEUM", "BIBLIOTHEK",
                "LIEBE", "HASS", "GLÜCK", "TRAURIGKEIT", "ANGST", "MUT", "HOFFNUNG", "ZEIT"
        ));
        DIZIONARIO_PAROLE.put(Lingua.SPAGNOLO, Arrays.asList(
                "CASA", "LIBRO", "MESA", "SILLA", "VENTANA", "PUERTA", "JARDÍN", "ÁRBOL",
                "FLOR", "SOL", "LUNA", "ESTRELLA", "MAR", "MONTAÑA", "RÍO", "LAGO",
                "PERRO", "GATO", "PÁJARO", "PEZ", "CABALLO", "VACA", "OVEJA", "CERDO",
                "COCHE", "TREN", "AVIÓN", "BICICLETA", "MOTO", "BARCO", "AUTOBÚS", "TRANVÍA",
                "ESCUELA", "UNIVERSIDAD", "HOSPITAL", "CINE", "TEATRO", "MUSEO", "BIBLIOTECA",
                "AMOR", "ODIO", "FELICIDAD", "TRISTEZA", "MIEDO", "CORAJE", "ESPERANZA", "TIEMPO"
        ));
    }

    /**
     * Genera una domanda su una parola non presente nel documento.
     * 1 opzione assente (corretta) + 3 opzioni presenti (errate).
     */
    private Domanda domandaParolaMaiComparsa(String nomeDocumento, Lingua l) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        if (parole == null || parole.size() < 3) {
            return null;
        }

        Lingua linguaEffettiva = (l != null && DIZIONARIO_PAROLE.containsKey(l)) ? l : Lingua.ITALIANO;
        List<String> dizionario = DIZIONARIO_PAROLE.get(linguaEffettiva);
        if (dizionario == null || dizionario.isEmpty()) {
            return null;
        }

        List<String> paroleAssenti = dizionario.stream()
                .filter(parola -> !parole.containsKey(parola))
                .collect(Collectors.toList());

        if (paroleAssenti.isEmpty()) {
            return null;
        }

        String parolaCorretta = paroleAssenti.get(rnd.nextInt(paroleAssenti.size()));

        List<String> parolePresenti = new ArrayList<>(parole.keySet());
        Collections.shuffle(parolePresenti);

        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaCorretta);
        opzioni.add(parolePresenti.get(0));
        opzioni.add(parolePresenti.get(1));
        opzioni.add(parolePresenti.get(2));

        Collections.shuffle(opzioni);
        int idx = opzioni.indexOf(parolaCorretta);

        String testo = String.format("Quale di queste parole NON è presente nel documento \"%s\"?",
                stripExtension(nomeDocumento));
        return new Domanda(testo, opzioni, idx);
    }

    /**
     * Genera una domanda di fallback sicura quando i requisiti per le domande complesse non sono soddisfatti.
     */
    private Domanda creaDomandaFallback(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        if (parole != null && !parole.isEmpty()) {
            Domanda d = domandaFrequenzaAssoluta(nomeDocumento);
            if (d != null) return d;
        }

        // Caso limite: documento privo di parole dopo il filtraggio
        String nome = stripExtension(nomeDocumento);
        return new Domanda(
                String.format("Quante parole uniche rimangono nel testo \"%s\" dopo il filtro stopwords?", nome),
                Arrays.asList("0", "1", "2", "3"),
                0
        );
    }

    /**
     * Restituisce una raccolta di domande valide generate dal documento specificato.
     * Garantisce che la lista contenga esattamente {@code num} elementi,
     * tutti non nulli e ciascuno con esattamente 4 opzioni.
     *
     * @param num il numero di domande da generare
     * @param nomeDocumento il nome del documento
     * @param l la lingua del documento
     * @return lista di oggetti {@link Domanda} garantiti privi di null
     */
    public List<Domanda> getRaccoltaDiDomande(int num, String nomeDocumento, Lingua l) {
        List<Domanda> lista = new ArrayList<>();
        if (num <= 0 || nomeDocumento == null) {
            return lista;
        }

        for (int i = 0; i < num; i++) {
            Domanda d = null;
            int tipoPartenza = rnd.nextInt(5);

            // Prova fino a 5 strategie diverse prima di usare il fallback
            for (int tentativo = 0; tentativo < 5 && d == null; tentativo++) {
                int strategia = (tipoPartenza + tentativo) % 5;
                d = switch (strategia) {
                    case 0 -> domandaConfrontoFrequenze(nomeDocumento);
                    case 1 -> domandaFrequenzaAssoluta(nomeDocumento);
                    case 2 -> domandaParolaMenoFrequente(nomeDocumento);
                    case 3 -> domandaParolaMaiComparsa(nomeDocumento, l);
                    default -> domandaParolaPiuFrequente(nomeDocumento);
                };
            }

            if (d == null) {
                d = creaDomandaFallback(nomeDocumento);
            }

            lista.add(d);
        }
        return lista;
    }
}
