package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import java.io.Serializable;

/**
 * Rappresenta un utente del sistema.
 * La classe Utente incapsula le informazioni di base relative a un utente, quali:
 * - username: il nome visibile dell'utente;
 * - email: l'indirizzo email, immutabile dopo la creazione;
 * - fotoProfilo: un array di byte rappresentante l'immagine del profilo (può essere {@code null});
 * - tipo: il tipo di utente, indicato tramite l'enum {@link TipoUtente} (ad es. amministratore o giocatore).
 */

public class Utente implements Serializable {

/** Nome utente dell'utente */
private String username;

/** Email immutabile dell'utente */
private final String email;

/** Foto profilo dell'utente come array di byte, può essere null */
private byte[] fotoProfilo;

/** Tipo dell'utente (es. amministratore, giocatore) */
private TipoUtente tipo;


    /**
     * Costruisce un nuovo utente con username, email, foto profilo e il tipo di utente.
     *
     * @param username    il nome utente
     * @param email       l'indirizzo email (chiamato anche identificativo univoco)
     * @param fotoProfilo un array di byte che rappresenta la foto del profilo dell'utente
     * @param tipo        il tipo dell'utente, es. {@code TipoUtente.amministratore} o {@code TipoUtente.giocatore}
     */
    public Utente(String username, String email, byte[] fotoProfilo, TipoUtente tipo) {
        this.username = username;
        this.email = email;
        this.fotoProfilo = fotoProfilo;
        this.tipo = tipo;
    }

    /**
     * Costruisce un nuovo utente con username, email e tipo, senza foto profilo.
     *
     * @param username il nome utente
     * @param email    l'indirizzo email
     * @param tipo     il tipo dell'utente
     */
    public Utente(String username, String email, TipoUtente tipo) {
        this.username = username;
        this.email = email;
        this.tipo = tipo;
        this.fotoProfilo = null;
    }

    /**
     * Restituisce il nome utente.
     *
     * @return il nome utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     *
     * @return l'email (valore immutabile)
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce la foto profilo dell'utente.
     *
     * @return un array di byte rappresentante la foto profilo, oppure {@code null} se non impostata
     */
    public byte[] getFotoProfilo() {
        return fotoProfilo;
    }

    /**
     * Restituisce il tipo dell'utente.
     *
     * @return il tipo dell'utente, ad esempio {@code TipoUtente.amministratore} o {@code TipoUtente.giocatore}
     */
    public TipoUtente getTipo() {
        return tipo;
    }

    /**
     * Imposta un nuovo nome utente.
     *
     * @param username il nuovo nome utente da impostare
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Imposta una nuova foto profilo per l'utente.
     *
     * @param fotoProfilo un array di byte rappresentante la nuova foto profilo
     */
    public void setFotoProfilo(byte[] fotoProfilo) {
        this.fotoProfilo = fotoProfilo;
    }

    /**
     * Restituisce una rappresentazione testuale dell'utente.
     *
     * @return una stringa contenente username, email, riferimento alla foto profilo e tipo utente
     */
    @Override
    public String toString() {
        return "Utente{" +
               "username=" + username +
               ", email=" + email +
               ", fotoProfilo=" + fotoProfilo +
               ", tipo=" + tipo +
               '}';
    }
}
