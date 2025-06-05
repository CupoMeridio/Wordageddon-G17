/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import java.util.ArrayList;

/**
 *
 * @author Mattia Sanzari
 */
public interface DosAmministratore {
    
    public boolean CaricareStopwords(String email, byte[] documentoStopwords, String nomefile);
    public boolean CaricareTesto(String email,String nomeFile, String difficolta, byte[] file, Lingua lingua);
    public byte[] PrendiStopwords(String nomeFile);
    public byte[] PrendiTesto(String nomeDocumento);
    public boolean CancellaTesto(String nomeDocumento);
    public ArrayList<DocumentoDiTesto> prendiTuttiIDocumenti();
}
