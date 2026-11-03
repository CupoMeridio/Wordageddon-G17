package it.unisa.diem.wordageddong17.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnalisiDocumentiTest {

    private AnalisiDocumenti analisi;

    @BeforeEach
    public void setUp() {
        analisi = new AnalisiDocumenti();
    }

    @Test
    public void testParoleAccentuataNonVengonoTroncate() throws IOException {
        String testo = "L'università è fantastica perché offre così tante opportunità. L'università forma gli studenti.";
        byte[] docBytes = testo.getBytes(StandardCharsets.UTF_8);

        analisi.analisiUnDocumento(docBytes, "test_accenti.txt");
        Map<String, Integer> risultato = analisi.restituisciDocumento("test_accenti.txt");

        assertNotNull(risultato);
        // "UNIVERSITÀ" non deve essere troncata in "UNIVERSIT"
        assertTrue(risultato.containsKey("UNIVERSITÀ"), "La parola 'UNIVERSITÀ' deve essere riconosciuta con accento");
        assertEquals(2, risultato.get("UNIVERSITÀ"));

        assertTrue(risultato.containsKey("PERCHÉ"), "La parola 'PERCHÉ' deve essere riconosciuta con accento");
        assertEquals(1, risultato.get("PERCHÉ"));

        assertTrue(risultato.containsKey("COSÌ"), "La parola 'COSÌ' deve essere riconosciuta con accento");
        assertEquals(1, risultato.get("COSÌ"));
    }

    @Test
    public void testFiltraggioStopWords() throws IOException {
        String stopwords = "è per una di con gli le";
        analisi.setStopWords(stopwords.getBytes(StandardCharsets.UTF_8));

        String testo = "Una mela è caduta per terra con grande rumore.";
        analisi.analisiUnDocumento(testo.getBytes(StandardCharsets.UTF_8), "test_sw.txt");
        Map<String, Integer> risultato = analisi.restituisciDocumento("test_sw.txt");

        assertNotNull(risultato);
        assertFalse(risultato.containsKey("UNA"), "La stopword 'UNA' deve essere filtrata");
        assertFalse(risultato.containsKey("È"), "La stopword 'È' deve essere filtrata");
        assertFalse(risultato.containsKey("PER"), "La stopword 'PER' deve essere filtrata");
        assertFalse(risultato.containsKey("CON"), "La stopword 'CON' deve essere filtrata");

        assertTrue(risultato.containsKey("MELA"));
        assertTrue(risultato.containsKey("CADUTA"));
        assertTrue(risultato.containsKey("TERRA"));
    }
}
