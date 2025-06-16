package it.unisa.diem.wordageddong17.model;

/**
 * Enum che rappresenta i tipi di utente.
 * I possibili tipi sono:
 * - amministratore: utente con privilegi di amministrazione
 * - giocatore: utente che partecipa alle partite
 */
public enum TipoUtente {

    /** Utente con privilegi di amministrazione */
    amministratore,

    /** Utente che partecipa alle partite */
    giocatore;
}
