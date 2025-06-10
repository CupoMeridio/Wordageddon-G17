package it.unisa.diem.wordageddong17.database;

/**
 * Eccezione personalizzata che viene lanciata quando non sono disponibili documenti sufficienti.
 * <p>
 * Questa eccezione può essere sollevata, ad esempio, durante operazioni di elaborazione o analisi
 * che richiedono la presenza di un certo numero minimo di documenti. È possibile utilizzare il costruttore
 * di default per ottenere un messaggio standard oppure fornire un messaggio personalizzato che spieghi in maniera
 * più precisa la condizione d'errore.
 * </p>
 */
public class MancanzaDiDocumenti extends Exception {

    /**
     * Crea un'eccezione MancanzaDiDocumenti con il messaggio di default:
     * "Non ci sono abbastanza documenti".
     */
    public MancanzaDiDocumenti() {
        super("Non ci sono abbastanza documenti");
    }

    /**
     * Crea un'eccezione MancanzaDiDocumenti con un messaggio personalizzato.
     *
     * @param msg il messaggio che descrive la condizione di errore
     */
    public MancanzaDiDocumenti(String msg) {
        super(msg);
    }
}
