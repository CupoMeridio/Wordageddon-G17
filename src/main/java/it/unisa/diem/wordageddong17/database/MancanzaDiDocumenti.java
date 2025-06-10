/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

/**
 *
 * @author Mattia Sanzari
 */
public class MancanzaDiDocumenti extends Exception {

    /**
     * Creates a new instance of <code>MancanzaDidocumenti</code> without detail
     * message.
     */
    public MancanzaDiDocumenti() {
        super("Non ci sono abbastanza documenti");
    }

    /**
     * Constructs an instance of <code>MancanzaDidocumenti</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    
    public MancanzaDiDocumenti(String msg) {
        super(msg);
    }
}
