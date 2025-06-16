# Wordageddon-G17
<div align="center">
  <img src="src/main/resources/imgs/Logo_senza_sfondo.png" alt="Wordageddon Logo" width="300"/>
</div>

Un'applicazione desktop ludico-educativa sviluppata in JavaFX per testare la memoria sulle parole più frequenti in documenti testuali.

## 🚀 Funzionalità principali

- 🎮 **Sistema di gioco multi-livello**: Facile, Medio, Difficile  
- ❓ **4 tipologie di quiz**:
  - Selezione della parola con **frequenza assoluta** maggiore in un documento
  - Riconoscimento della parola con **frequenza relativa** più alta
  - Identificazione della **parola non presente** nel documento
  - Scelta della **parola più ricorrente** in uno specifico documento
- 👤 **Gestione utenti**:
  - Gli utenti possono registrarsi e accedere tramite login
  - Le credenziali sono memorizzate in modo sicuro nel database (hashing con bcrypt)
  - Sono previsti **account amministratore** (creabili solo esternamente via database)
  - Gli amministratori hanno accesso a funzionalità avanzate:
    - Gestione dei documenti testuali
    - Modifica della lista di *stopwords*
- 🏆 **Classifiche e storico punteggi**

---

## ⚙️ Requisiti tecnici

- **JDK 17+**
- **NetBeans 26**
- **Maven**
- Sistema operativo: multipiattaforma (testato su Windows e Linux)

---

## 🗃️ Database

Il progetto utilizza **PostgreSQL 17** come sistema di gestione del database relazionale.  
Per l'amministrazione e la gestione dei dati è stato utilizzato **pgAdmin 4**.

Le credenziali di accesso sono gestite tramite un file `.env` (non incluso nel repository).

Struttura attesa del file `.env`:

```env
DBNAME=nome_del_database
USER=nome_utente
PASSWORD=password_utente
```

---

## 📦 Tecnologie e dipendenze
Il progetto utilizza le seguenti librerie e plugin esterni:

- JavaFX — UI framework principale

- dotenv-java — Gestione variabili d'ambiente (.env) per la connessione sicura al database

- jbcrypt — Crittografia sicura delle password

- maven-shade-plugin — Creazione del JAR eseguibile con tutte le dipendenze incluse

---

## 📁 Struttura del progetto
```
Wordageddon-G17/
├── src/
│   └── main/
│       ├── java/                           # Codice sorgente Java
│       ├── resources/                      # Risorse (FXML, immagini, file di configurazione)
├── .env                                    # Variabili d’ambiente (NON incluso nel repository)
├── Documentazione javadoc/                 # Cartella contenente la documentazione javadoc
├── Home documentazione javadoc             # Collegamento epr accedere rapidamente alla home della documentazione javadoc
├── pom.xml                                 # Configurazione Maven
├── Schema database.sql                     # Backup del database realizzato contenente la sola struttura
├── Relazione di progetto - Gruppo 17.pdf   # Relazione del progetto contenente analisi dei requisiti, casi d'uso, diagrammi delle classi, diagrammi delle sequenze, progettazione database con diagramma ER, mockup
```

---

## 📚 Documentazione e Release
- La **documentazione Javadoc** completa è disponibile nel repository e può essere generata tramite NetBeans
- È presente un **documento PDF** con la relazione di progetto contenente:
  - Analisi dei requisiti
  - Casi d'uso
  - Diagrammi delle classi
  - Diagrammi delle sequenze
  - Mockup dell'interfaccia
- Nella sezione **Releases** è disponibile il file JAR eseguibile pronto all'uso
- Il JAR può essere generato anche localmente tramite le funzioni di build di NetBeans

---

## 👨‍💻 Membri del team
- [Vittorio Postiglione](https://github.com/CupoMeridio)
- [Mattia Sanzari](https://github.com/Mattia-Sanzari)
- [Alessandro Scandone](https://github.com/alescand1)
- [Sharon Schiavano](https://github.com/sharon-schiavano)

## 🎓 Contesto accademico
Questo progetto è stato realizzato come parte dell’esame di Java Avanzato presso l’Università degli Studi di Salerno.
<div align="center">
  <img src="https://www.opisalerno.it/wp-content/uploads/2016/11/logo-unisa-png-768x432.png" width="400" alt="Unisa Logo"/>
</div>
