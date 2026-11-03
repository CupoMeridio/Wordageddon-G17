package it.unisa.diem.wordageddong17.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratoreDomandeTest {

    private AnalisiDocumenti analisi;
    private GeneratoreDomande generatore;

    @BeforeEach
    public void setUp() throws IOException {
        analisi = new AnalisiDocumenti();
        String testo = """
                Nel mezzo del cammin di nostra vita mi ritrovai per una selva oscura,
                ché la diritta via era smarrita. Ahi quanto a dir qual era è cosa dura
                esta selva selvaggia e aspra e forte che nel pensier rinova la paura!
                Tant è amara che poco è più morte; ma per trattar del ben ch i vi trovai,
                dirò de l altre cose ch i v ho scorte.
                """;
        analisi.analisiUnDocumento(testo.getBytes(StandardCharsets.UTF_8), "inferno.txt");
        generatore = new GeneratoreDomande(analisi);
    }

    @Test
    public void testInvariantiDomandeGenerate() {
        int numDomande = 20;
        List<GeneratoreDomande.Domanda> domande = generatore.getRaccoltaDiDomande(numDomande, "inferno.txt", Lingua.ITALIANO);

        assertEquals(numDomande, domande.size(), "Il numero di domande richieste deve corrispondere esattamente");

        for (GeneratoreDomande.Domanda d : domande) {
            assertNotNull(d, "Nessuna domanda deve essere null");
            assertNotNull(d.testo, "Il testo della domanda non deve essere null");
            assertFalse(d.testo.isBlank(), "Il testo della domanda non deve essere vuoto");

            assertEquals(4, d.opzioni.size(), "Ogni domanda deve avere esattamente 4 opzioni");
            assertEquals(4, new HashSet<>(d.opzioni).size(), "Tutte le 4 opzioni devono essere distinte tra loro: " + d.opzioni);

            assertTrue(d.rispostaCorretta >= 0 && d.rispostaCorretta < 4,
                    "L'indice della risposta corretta deve essere compreso tra 0 e 3");
            assertNotNull(d.opzioni.get(d.rispostaCorretta), "La risposta corretta non deve essere null");
        }
    }

    @Test
    public void testEqualsAndHashCodeDomanda() {
        List<String> opzioni = List.of("A", "B", "C", "D");
        GeneratoreDomande.Domanda d1 = new GeneratoreDomande.Domanda("Test domanda", opzioni, 1);
        GeneratoreDomande.Domanda d2 = new GeneratoreDomande.Domanda("Test domanda", opzioni, 1);
        GeneratoreDomande.Domanda d3 = new GeneratoreDomande.Domanda("Altra domanda", opzioni, 1);

        assertEquals(d1, d2, "Due domande con stessi campi devono essere uguali");
        assertEquals(d1.hashCode(), d2.hashCode(), "Due domande uguali devono avere lo stesso hashCode");
        assertNotEquals(d1, d3);
    }

    @Test
    public void testDocumentoConPochissimeParoleNonLanciaEccezioni() throws IOException {
        AnalisiDocumenti piccolaAnalisi = new AnalisiDocumenti();
        piccolaAnalisi.analisiUnDocumento("Solo una parola.".getBytes(StandardCharsets.UTF_8), "mini.txt");
        GeneratoreDomande genMini = new GeneratoreDomande(piccolaAnalisi);

        List<GeneratoreDomande.Domanda> domande = genMini.getRaccoltaDiDomande(5, "mini.txt", Lingua.ITALIANO);
        assertEquals(5, domande.size());
        for (GeneratoreDomande.Domanda d : domande) {
            assertNotNull(d);
            assertEquals(4, d.opzioni.size());
            assertEquals(4, new HashSet<>(d.opzioni).size());
        }
    }
}
