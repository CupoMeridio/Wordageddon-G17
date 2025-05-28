/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

/**
 *
 * @author Mattia Sanzari
 */
public interface DosLogin {
    
    public String prendiUsername(String email);
    public boolean verificaPassword(String email, String password);
    
}
