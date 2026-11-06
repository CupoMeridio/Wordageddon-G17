/**
 * Layer di accesso ai dati e persistenza relazionale PostgreSQL.
 * Gestisce la configurazione del connection pool HikariCP, l'interazione
 * thread-safe con il database e la propagazione controllata delle eccezioni tramite
 * {@link it.unisa.diem.wordageddong17.database.DataAccessException}.
 */
package it.unisa.diem.wordageddong17.database;
