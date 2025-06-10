/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
public class PrendiStopWordsService extends Service<byte[]> {
    private final DAOListaStopWords dbSW;
    private String nomeFile;
    
    public PrendiStopWordsService(String nomeFile) {
        this.dbSW = DatabaseStopWords.getInstance();
        this.nomeFile=nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }
    
    @Override
    protected Task<byte[]> createTask() {
     
            return new Task<byte[]>(){
                @Override
                protected byte[] call() throws Exception {
                  return dbSW.PrendiStopwords(nomeFile);
                }
            };
    }   
}
