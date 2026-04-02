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
- 🌍 Supporto multilingua per i documenti:
  - Prima di iniziare una sessione, l'utente può scegliere la lingua dei documenti con cui giocare
  - Le lingue attualmente supportate sono: italiano, inglese, spagnolo, francese, tedesco
- 👤 **Gestione utenti**:
  - Gli utenti possono registrarsi e accedere tramite login
  - Le credenziali sono memorizzate in modo sicuro nel database (hashing con bcrypt)
  - Sono previsti **account amministratore** (creabili solo esternamente via database)
  - Gli amministratori hanno accesso a funzionalità avanzate:
    - Gestione dei documenti testuali
    - Modifica della lista di *stopwords*
- 🏆 **Classifiche e storico punteggi**
- 💾 Ripresa delle partite interrotte:
  - Il sistema salva automaticamente lo stato corrente della partita
  - Alla successiva autenticazione, l'utente può decidere se riprendere o iniziare una nuova sessione di gioco

---

## ⚙️ Requisiti tecnici

- **JDK 17+**
- **[NetBeans 26](https://netbeans.apache.org/front/main/index.html)**
- **Maven**
- Sistema operativo: multipiattaforma (testato su Windows e Linux)

---

## 🗃️ Database

Il progetto utilizza **[PostgreSQL 17](https://www.postgresql.org)** come sistema di gestione del database relazionale.  
Per l'amministrazione e la gestione dei dati è stato utilizzato **pgAdmin 4**.

Il database è stato **ospitato in cloud** mediante i servizi forniti da [**Aiven**](https://aiven.io/), garantendo accessibilità remota e maggiore affidabilità durante lo sviluppo collaborativo.

Le **credenziali di accesso** sono gestite tramite un file `.env` (non incluso nel repository).

Struttura attesa del file `.env`:

```env
DBNAME=nome_del_database
USER=nome_utente
PASSWORD=password_utente
```

---

## 📦 Tecnologie e dipendenze

Questo progetto utilizza le seguenti librerie e plugin esterni, **gestiti automaticamente tramite Maven** (non è necessario scaricare nulla manualmente):

- [JavaFX](https://openjfx.io/) — UI framework principale per la realizzazione dell'interfaccia grafica.
- [dotenv-java](https://github.com/cdimascio/dotenv-java) — Gestione delle variabili d'ambiente (`.env`) per una configurazione sicura, ad esempio per la connessione al database.
- [jBCrypt](https://github.com/jeremyh/jBCrypt) — Crittografia sicura delle password con algoritmo BCrypt.
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/) — Driver per la connessione al database PostgreSQL.
- [Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/) — Permette la creazione di un JAR eseguibile contenente tutte le dipendenze.

Tutte le dipendenze sono dichiarate nel file [`pom.xml`](./pom.xml).

---

## 📁 Struttura del progetto

Il progetto segue il pattern architetturale **MVC (Model-View-Controller)**, supportato da un livello di **Service** per le operazioni asincrone e un pattern **DAO (Data Access Object)** per la persistenza.

```text
Wordageddon-G17/
├── src/main/java/it/unisa/diem/wordageddong17/
│   ├── controller/      # Gestori degli eventi JavaFX e logica di presentazione
│   ├── model/           # Core business logic e classi di dominio (Utente, Partita, Analisi)
│   ├── service/         # Servizi per operazioni asincrone e task in background
│   ├── database/        # Implementazioni concrete per l'accesso ai dati (PostgreSQL)
│   ├── interfaccia/     # Interfacce DAO per il disaccoppiamento della persistenza
│   ├── App.java         # Classe principale dell'applicazione JavaFX
│   └── RunMain.java     # Launcher ausiliario per l'eseguibile JAR
├── src/main/resources/
│   ├── fxml/            # Definizioni dell'interfaccia utente (Scene Builder)
│   ├── styles/          # Fogli di stile CSS per il look & feel dell'app
│   ├── imgs/            # Asset grafici, loghi e diagrammi UML esportati
│   └── UML/             # Sorgenti dei diagrammi delle classi e casi d'uso
├── .env                 # Variabili d’ambiente (NON incluso nel repository)
├── Documentazione javadoc/  # Cartella contenente la documentazione javadoc completa
├── pom.xml              # File di configurazione Maven e gestione dipendenze
├── Schema database.sql  # Script SQL per l'inizializzazione del database
└── Relazione...pdf      # Documentazione completa dell'analisi e progettazione
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
- Nella sezione **[Releases](https://github.com/CupoMeridio/Wordageddon-G17/releases)** è disponibile il file JAR eseguibile pronto all'uso
- Il JAR può essere generato anche localmente tramite il comando `mvn clean package` o le funzioni di build dell'IDE

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
