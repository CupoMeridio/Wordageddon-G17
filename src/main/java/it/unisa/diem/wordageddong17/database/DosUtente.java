/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

/**
 *
 * @author user
 */
public interface DosUtente {
    
    public boolean modificaUsername(String email, String username);
    public boolean modificaFotoProfilo(String email, byte[] foto);
    
}
