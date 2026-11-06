# Wordageddon-G17

<div align="center">
  <img src="src/main/resources/imgs/Logo_senza_sfondo.png" alt="Wordageddon Logo" width="300"/>
</div>

Un'applicazione desktop ludico-educativa sviluppata in **JavaFX** per testare la memoria e l'intuizione lessicale sulle parole e sulle loro frequenze in documenti testuali multilingua.

---

> [!NOTE]
> ### Versioni del repository
> - **Branch `consegna-esame`**: conserva lo stato originale del progetto così come consegnato e presentato per l'esame accademico (aprile 2026).
> - **Branch `main`**: versione consolidata ed evoluta del progetto, con refactoring dell'architettura frontend, eliminazione del debito tecnico, supporto per database locale via Docker e pipeline di Continuous Integration.

---

## Panoramica del gioco

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
- **Amministratore**: oltre al gameplay, gestione e caricamento di nuovi testi (.txt) nel database e aggiornamento della lista di stopwords.

---

## Download e Release Eseguibili (Continuous Build)

I pacchetti compilati e pronti all'uso vengono generati e aggiornati automaticamente ad ogni commit sul ramo `main` tramite GitHub Actions nella sezione **[Latest Release](https://github.com/CupoMeridio/Wordageddon-G17/releases/tag/latest)**:

| Pacchetto | Descrizione | Requisiti |
|---|---|---|
| **`Wordageddon-*.msi`** | Installer guidato per Windows con icona e collegamenti su Desktop e Menu Start | Nessuno (Java Runtime integrato) |
| **`Wordageddon-Windows-Portable.zip`** | Versione portabile per Windows (estrai ed esegui `Wordageddon.exe`) | Nessuno (Java Runtime integrato) |
| **`Wordageddon.jar`** | Fat JAR eseguibile multipiattaforma (Linux, macOS, Windows) | Java 17 o superiore |

---

## Come Avviare il Sistema Completo

Per eseguire l'applicazione completa è necessario disporre di un database PostgreSQL (locale tramite Docker, locale nativo o remoto su cloud). Di seguito sono illustrate le modalità supportate.

### Opzione 1: Database locale con Docker (Consigliata)

È il metodo più rapido per eseguire l'applicazione in locale senza configurazioni manuali del database.

#### Prerequisito: Installazione di Docker (se non già presente)

- **Windows**:
  - Scaricare e installare [Docker Desktop](https://www.docker.com/products/docker-desktop/). Durante l'installazione verificare che sia selezionata l'opzione "Use WSL 2 instead of Hyper-V".
  - *Nota*: se WSL risulta disabilitato sul sistema, aprire PowerShell come Amministratore ed eseguire:
    ```powershell
    wsl --install --no-distribution
    ```
    quindi riavviare il computer prima di avviare Docker Desktop.
- **Linux (Debian, Ubuntu e derivate)**:
  - Installare Docker Engine e il plugin Compose da terminale:
    ```bash
    sudo apt update
    sudo apt install -y docker.io docker-compose-v2
    sudo usermod -aG docker $USER
    ```
    (effettuare il logout e login dalla sessione utente per applicare il gruppo `docker`).

Verificare la corretta installazione con:
```bash
docker compose version
```

#### Procedura di avvio

1. **Avviare il database PostgreSQL 17**:
   Dalla cartella principale del progetto:
   ```bash
   docker compose up -d
   ```
   Il container si avvia in background con lo schema applicato (`01_schema.sql`) e i dati dimostrativi precaricati (`02_seed.sql`). Per arrestare il database al termine dell'utilizzo: `docker compose down`.

2. **Avviare l'applicazione**:
   - **Su Windows**: avviare l'eseguibile `Wordageddon.exe` (dalla versione portabile o installata via `.msi`).
   - **Su Linux / macOS**: eseguire il Fat JAR:
     ```bash
     java -jar target/WordageddonG17-1.0-SNAPSHOT-jar-with-dependencies.jar
     ```
   - **Da codice sorgente**:
     ```bash
     mvn javafx:run
     ```
   L'applicazione si collegherà automaticamente al database locale su `localhost:5432`.

### Opzione 2: PostgreSQL nativo locale (Senza Docker né WSL)

Per chi non desidera o non può abilitare WSL/Docker sul proprio sistema:

1. **Installare PostgreSQL nativo**:
   - **Su Windows**: scaricare ed eseguire l'installer ufficiale `.exe` da [postgresql.org/download/windows](https://www.postgresql.org/download/windows/) (installa PostgreSQL come normale servizio Windows, senza bisogno di virtualizzazione o WSL).
   - **Su Linux**: installare PostgreSQL con il package manager:
     ```bash
     sudo apt install -y postgresql postgresql-contrib
     ```
2. **Inizializzare il database**:
   - Creare un database denominato `wordageddon`.
   - Tramite **pgAdmin** (incluso nell'installer Windows) oppure da terminale con `psql`, aprire ed eseguire in sequenza:
     1. `docker/init/01_schema.sql` (tabelle, enum, indici e vincoli)
     2. `docker/init/02_seed.sql` (testi multilingua, stopwords e utenti)
3. **Configurazione credenziali (se diverse dal default)**:
   Se durante l'installazione di PostgreSQL è stata impostata una password diversa da `wordageddon`, creare un file `.env` accanto all'applicazione:
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/wordageddon
   DB_USER=postgres
   DB_PASSWORD=la_tua_password
   ```
4. **Avviare l'applicazione** (`Wordageddon.exe`, `.jar` o `mvn javafx:run`).

### Opzione 3: Database Remoto Cloud (Zero installazioni locali)

Per eseguire l'applicazione senza installare alcun servizio database sul computer:

1. Creare un'istanza PostgreSQL gratuita su un provider cloud (es. Neon.tech, Supabase, Aiven).
2. Eseguire sulla console SQL del provider gli script:
   - `docker/init/01_schema.sql`
   - `docker/init/02_seed.sql`
3. Creare il file `.env` nella directory di esecuzione dell'applicazione:
   ```env
   DB_URL=jdbc:postgresql://host-remoto:5432/nomedb?sslmode=require
   DB_USER=tuo_username
   DB_PASSWORD=tua_password
   ```
4. Avviare l'applicazione (`Wordageddon.exe`, `.jar` o `mvn javafx:run`).

---

## Credenziali di Accesso Dimostrative

Nel database preconfigurato sono disponibili due account di prova:

| Ruolo | Email | Password | Funzionalità disponibili |
|---|---|---|---|
| **Giocatore** | `player@wordageddon.it` | `player123` | Sessioni di gioco, salvataggi, classifiche e cronologia |
| **Amministratore** | `admin@wordageddon.it` | `admin123` | Gameplay completo + gestione documenti (.txt) e modifica stopwords |

In alternativa, è sempre possibile registrare un nuovo account direttamente dalla schermata di registrazione.

---

## Database e Configurazione

La classe `Database.java` adotta un connection pool **HikariCP** e determina la configurazione secondo questa priorità:
1. **Variabili d'ambiente di sistema**: `DB_URL`, `DB_USER`, `DB_PASSWORD`
2. **File `.env`** nella directory corrente (template disponibile in `.env.example`)
3. **Default per lo sviluppo locale**:
   - `DB_URL`: `jdbc:postgresql://localhost:5432/wordageddon`
   - `DB_USER`: `wordageddon`
   - `DB_PASSWORD`: `wordageddon`

---

## Tecnologie e Dipendenze

- **Java 17+** (supporta Java 21)
- **JavaFX 17** (Controls, FXML)
- **HikariCP 5.1.0** — Connection pooling thread-safe
- **PostgreSQL JDBC Driver 42.7.5**
- **jBCrypt 0.4** — Hashing sicuro delle password (salt a 12 round)
- **dotenv-java 3.2.0** — Gestione variabili d'ambiente opzionali
- **SLF4J / JDK14** — Logging unificato

---

## Struttura del Progetto

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

## Documentazione e Javadoc

La documentazione delle API e i riferimenti formali di progetto sono fruibili in diverse modalità:

- **Consultazione Online (GitHub Pages)**: [cupomeridio.github.io/Wordageddon-G17](https://cupomeridio.github.io/Wordageddon-G17/)  
  *(Compilata e aggiornata automaticamente ad ogni push sul branch `main`)*
- **Pacchetto Offline**: allegato come `Wordageddon-javadoc.jar` nella sezione **[Latest Release](https://github.com/CupoMeridio/Wordageddon-G17/releases/tag/latest)**.
- **Generazione manuale da sorgenti**:
  ```bash
  mvn javadoc:javadoc
  ```
  I file HTML generati saranno disponibili localmente in `target/reports/apidocs/index.html`.

La relazione accademica formale, le slide e i diagrammi UML delle classi sono archiviati nella cartella [`docs/`](./docs/).

---

## Membri del Team originario
- [Vittorio Postiglione](https://github.com/CupoMeridio)
- [Mattia Sanzari](https://github.com/Mattia-Sanzari)
- [Alessandro Scandone](https://github.com/alescand1)
- [Sharon Schiavano](https://github.com/sharon-schiavano)

## Contesto Accademico
Progetto d'esame sviluppato per il corso di **Java Avanzato** presso l'**Università degli Studi di Salerno**.
