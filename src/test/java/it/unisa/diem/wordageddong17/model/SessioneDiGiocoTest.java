package it.unisa.diem.wordageddong17.model;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessioneDiGiocoTest {

    private Utente utenteTest;
    private final String testEmail = "test_player_unit@wordageddon.it";

    @BeforeEach
    public void setUp() {
        utenteTest = new Utente("testuser", testEmail, null, TipoUtente.giocatore);
        SessioneDiGioco.eliminaSalvataggio(testEmail);
    }

    @AfterEach
    public void tearDown() {
        SessioneDiGioco.eliminaSalvataggio(testEmail);
    }

    @Test
    public void testSalvataggioERipristinoSessione() throws IOException {
        SessioneDiGioco sessione = new SessioneDiGioco(5, 60, utenteTest, 2);
        sessione.setLivello(LivelloPartita.FACILE);

        GeneratoreDomande.Domanda d1 = new GeneratoreDomande.Domanda("Domanda 1", List.of("1", "2", "3", "4"), 0);
        GeneratoreDomande.Domanda d2 = new GeneratoreDomande.Domanda("Domanda 2", List.of("A", "B", "C", "D"), 2);
        sessione.setDomande(List.of(d1, d2));
        sessione.aggiornaRisposte(0, 0); // risposta 0 alla domanda 1 (corretta)

        assertFalse(SessioneDiGioco.esisteSalvataggio(testEmail));

        // Salva la sessione
        sessione.salvaSessioneDiGioco();
        assertTrue(SessioneDiGioco.esisteSalvataggio(testEmail), "Il file di salvataggio deve esistere dopo il salvataggio");

        // Carica in una nuova istanza
        SessioneDiGioco sessioneRipristinata = new SessioneDiGioco();
        sessioneRipristinata.caricaSessioneDiGiocoPerEmail(testEmail);

        assertEquals(utenteTest.getEmail(), sessioneRipristinata.getUtente().getEmail());
        assertEquals(2, sessioneRipristinata.getDomande().size());
        assertEquals(1, sessioneRipristinata.getRisposte().size());
        assertEquals(0, sessioneRipristinata.getRisposte().get(d1));

        // Elimina salvataggio
        assertTrue(SessioneDiGioco.eliminaSalvataggio(testEmail));
        assertFalse(SessioneDiGioco.esisteSalvataggio(testEmail));
    }

    @Test
    public void testCalcoloPuntiFattiConDomandeEquals() {
        SessioneDiGioco sessione = new SessioneDiGioco(2, 60, utenteTest, 1);
        GeneratoreDomande.Domanda d1 = new GeneratoreDomande.Domanda("D1", List.of("A", "B", "C", "D"), 1);
        GeneratoreDomande.Domanda d2 = new GeneratoreDomande.Domanda("D2", List.of("X", "Y", "Z", "W"), 3);
        sessione.setDomande(List.of(d1, d2));

        // Risposte corrette
        sessione.aggiornaRisposte(0, 1);
        sessione.aggiornaRisposte(1, 3);

        sessione.aggiornaPuntiFatti(30);
        assertTrue(sessione.getPunteggioFatto() > 0, "Il punteggio calcolato con 2 risposte corrette deve essere > 0");
    }
}
