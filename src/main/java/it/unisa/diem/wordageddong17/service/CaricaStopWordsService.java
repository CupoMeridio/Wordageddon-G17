/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.Database;
import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.ListaStopWords;
import it.unisa.diem.wordageddong17.model.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
public class CaricaStopWordsService extends Service<Void> {
    private final DAOListaStopWords dbSW;
    private String nomeFile;
    private String email;
    private byte[] documento;

    public CaricaStopWordsService(String nomeFile, String email, byte[] documento) {
        this.dbSW = DatabaseStopWords.getInstance();
        this.nomeFile = nomeFile;
        this.email = email;
        this.documento = documento;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }
    
    

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }
    
    @Override
    protected Task<Void> createTask() {
     return new Task<Void>(){
         @Override
         protected Void call() throws Exception {
             System.out.println("email: "+ email +"documento: "+documento + "nomeFile"+ nomeFile);
            dbSW.CaricareStopwords(email, documento, nomeFile);
            return null;
         }
     
     };
    }   
}
