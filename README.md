# Wordageddon-G17

<div align="center">
  <img src="src/main/resources/imgs/Logo_senza_sfondo.png" alt="Wordageddon Logo" width="300"/>
</div>

Un'applicazione desktop ludico-educativa sviluppata in **JavaFX** per testare la memoria e l'intuizione lessicale sulle parole e sulle loro frequenze in documenti testuali multilingua.

---

> [!NOTE]
> ### 📌 Nota sulle versioni del repository
> - **Branch `consegna-esame`**: conserva lo stato esatto del progetto così come consegnato e presentato per l'esame accademico (aprile 2026).
> - **Branch `main`**: accoglie l'evoluzione del progetto, il consolidamento architetturale e l'eliminazione del debito tecnico:
>   - **Fase 0**: Docker Compose, pool di connessioni HikariCP, schema e seed SQL puliti, rimozione di credenziali hardcoded e hardening della build.
>   - **Fase 1**: Risoluzione bug di analisi (tokenizzazione Unicode UTF-8 per parole accentate), garanzia invarianti quiz (4 opzioni distinte, nessun pareggio o null), propagazione eccezioni DB con `DataAccessException`, salvataggio sicuro sessioni in directory applicativa, rimozione leak timer.
>   - **Fase 2**: Decomposizione del monolite `AppViewController` (~2.300 righe) in 9 controller modulari e viste FXML dedicate coordinate dal `NavigationService`, estrazione del layer `util` (immagini, validazione, dialoghi), eliminazione di codice morto in Model e pipeline di Continuous Integration GitHub Actions (JDK 17 & 21).

---

## 🚀 Panoramica del gioco

Wordageddon sfida il giocatore ad analizzare mentalmente testi letterari in diverse lingue (italiano, inglese, spagnolo, francese, tedesco) e a rispondere a domande a scelta multipla nel minor tempo possibile.

### Tipologie di quiz implementate:
1. **Parola più frequente**: individuare la parola con il maggior numero di occorrenze nel documento.
2. **Parola meno frequente**: riconoscere la parola presente con la frequenza minima.
3. **Parola mai comparsa**: distinguere la parola che non appare mai nel testo analizzato.
4. **Confronto tra due parole**: confrontare le occorrenze di due parole e scegliere quella che appare più spesso.
5. **Frequenza assoluta di una parola**: indicare l'esatto conteggio di una determinata parola.

### Livelli di difficoltà:
- **Facile**: testi brevi e quiz basilari.
- **Medio**: testi di media lunghezza e vocabolario più articolato.
- **Difficile**: testi complessi e distrometri lessicali più serrati.

### Ruoli utente:
- **Giocatore**: registrazione, login con credenziali protette da BCrypt, scelta della partita, salvataggio/ripresa, visualizzazione della classifica globale e dello storico personale.
- **Amministratore**: oltre al gameplay, gestione e caricamento di nuovi testi (.txt) nel database e aggiornamento della lista di *stopwords*.

---

## 📥 Download e Release Eseguibili (Continuous Build)

I pacchetti eseguibili e pronti all'uso vengono compilati e aggiornati automaticamente ad ogni commit sul ramo principale tramite GitHub Actions nella sezione **[Latest Release](https://github.com/CupoMeridio/Wordageddon-G17/releases/tag/latest)**:

| Pacchetto | Descrizione | Requisiti |
|---|---|---|
| **`Wordageddon-*.msi`** | Installer guidato per Windows con icona e collegamenti su Desktop e Menu Start | Nessuno (Java Runtime integrato) |
| **`Wordageddon-Windows-Portable.zip`** | Versione portabile per Windows (estrai ed esegui `Wordageddon.exe`) | Nessuno (Java Runtime integrato) |
| **`Wordageddon.jar`** | Fat JAR eseguibile multipiattaforma (Linux, macOS, Windows) | Java 17 o superiore |

---

## ⚡ Avvio rapido con Docker Compose

Il modo più rapido per avviare il database locale completo di schema e dati dimostrativi:

```bash
# 1. Avvia PostgreSQL 17 locale pre-popolato (schema + seed + utenti dimostrativi)
docker compose up -d

# 2. Compila ed esegui l'applicazione JavaFX
mvn javafx:run
```

Se preferisci creare un fat JAR eseguibile con tutte le dipendenze:
```bash
mvn clean package
java -jar target/WordageddonG17-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 🗄️ Database e Configurazione

Il database utilizzato è **PostgreSQL 17**.

La classe di connessione (`Database.java`) adotta un connection pool ad alte prestazioni **HikariCP** e determina la configurazione secondo questa priorità:
1. **Variabili d'ambiente di sistema**: `DB_URL`, `DB_USER`, `DB_PASSWORD`
2. **File `.env`** nella directory corrente (può essere creato a partire da `.env.example`)
3. **Default per lo sviluppo locale** (allineati a `docker-compose.yml`):
   - `DB_URL`: `jdbc:postgresql://localhost:5432/wordageddon`
   - `DB_USER`: `wordageddon`
   - `DB_PASSWORD`: `wordageddon`

### File `.env` (opzionale):
```bash
cp .env.example .env
```
Contenuto tipo:
```env
DB_URL=jdbc:postgresql://localhost:5432/wordageddon
DB_USER=wordageddon
DB_PASSWORD=wordageddon
```

### Utenti preconfigurati nel seed:
All'avvio tramite `docker compose` sono già presenti due utenti di test:
| Ruolo | Email | Password | Descrizione |
|---|---|---|---|
| **Amministratore** | `admin@wordageddon.it` | `admin123` | Può caricare testi e aggiornare stopwords |
| **Giocatore** | `player@wordageddon.it` | `player123` | Utente con punteggi di test in classifica |

---

## 📦 Tecnologie e Dipendenze

- **Java 17+** (supporta Java 21)
- **JavaFX 17** (Controls, FXML)
- **HikariCP 5.1.0** — Connection pooling thread-safe e performante
- **PostgreSQL JDBC Driver 42.7.5**
- **jBCrypt 0.4** — Hashing sicuro delle password (salt a 12 round)
- **dotenv-java 3.1.2** — Caricamento variabili d'ambiente opzionali
- **SLF4J / JDK14** — Logging unificato

---

## 📁 Struttura del Progetto

```text
Wordageddon-G17/
├── .github/
│   └── workflows/
│       ├── ci.yml                # GitHub Actions CI (build e test su JDK 17 e 21)
│       └── release.yml           # Rolling Release automatica con jpackage (MSI, Portable ZIP, JAR)
├── docker/
│   └── init/
│       ├── 01_schema.sql         # DDL con tabelle, constraint, enum e trigger PostgreSQL
│       └── 02_seed.sql           # Utenti seed, stopwords e testi dimostrativi
├── docker-compose.yml            # PostgreSQL 17 pronto all'uso con volumi persistenti
├── docs/                         # Documentazione, relazione accademica e diagrammi UML
│   ├── Relazione di progetto - Gruppo 17.pdf
│   ├── Presentazione progetto.pptx
│   └── DiagrammiDelleClassi/     # Diagrammi UML divisi per modulo (EasyUML e PlantUML)
├── src/main/java/it/unisa/diem/wordageddong17/
│   ├── controller/               # Controller JavaFX modulari (Login, Home, Leaderboard, Setup, Profile, ecc.)
│   ├── model/                    # Logica di dominio, sessione di gioco, analisi testi e quiz generator
│   ├── service/                  # Servizi asincroni JavaFX e NavigationService centralizzato
│   ├── database/                 # Implementazioni DAO PostgreSQL con pool HikariCP
│   ├── interfaccia/              # Interfacce contrattuali DAO
│   ├── util/                     # Helper riutilizzabili (ImageUtils, ValidationUtils, AlertUtils)
│   ├── App.java                  # Classe di avvio JavaFX (Stage, Scene e caricamento FXML)
│   └── RunMain.java              # Entry point privo di estensioni per fat JAR
├── src/main/resources/
│   ├── fxml/                     # Viste FXML modulari (LoginView, HomeView, ProfileView, ecc.)
│   ├── styles/                   # Fogli di stile CSS
│   └── imgs/                     # Asset grafici di runtime (loghi, icone, avatar)
├── src/test/java/                # Suite di test unitari JUnit 5
├── .env.example                  # Template per variabili di configurazione
├── pom.xml                       # Descrittore di build Maven
└── README.md
```

---

## 📖 Documentazione e Javadoc

Per generare la documentazione Javadoc aggiornata senza sporcare il repository git:
```bash
mvn javadoc:javadoc
```
I file HTML saranno generati in `target/site/apidocs/index.html`.

La relazione formale e la presentazione sono archiviate nella cartella [`docs/`](./docs/).

---

## 👨‍💻 Membri del Team originario
- [Vittorio Postiglione](https://github.com/CupoMeridio)
- [Mattia Sanzari](https://github.com/Mattia-Sanzari)
- [Alessandro Scandone](https://github.com/alescand1)
- [Sharon Schiavano](https://github.com/sharon-schiavano)

## 🎓 Contesto Accademico
Progetto d'esame sviluppato per il corso di **Java Avanzato** presso l'**Università degli Studi di Salerno**.
