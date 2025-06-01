/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.List;

/**
 *
 * @author Mattia Sanzari
 */
public interface DosSessioneDiGioco {
    
    public byte[] prendiStopwords(String nomeFile);
    public List<byte[]> prendiDocumenti(LivelloPartita livello);
}
