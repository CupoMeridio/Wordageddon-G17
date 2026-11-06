# Documentazione di Progetto - Wordageddon G17

Questa cartella raccoglie i documenti formali, i diagrammi architetturali e i riferimenti alla documentazione tecnica di **Wordageddon**.

---

## 📑 Indice dei Documenti

### 1. Documentazione Tecnica e API (Javadoc)
- **Consultazione Online (GitHub Pages)**: [https://cupomeridio.github.io/Wordageddon-G17/](https://cupomeridio.github.io/Wordageddon-G17/)
- **Download Offline**: disponibile nella sezione [Releases](https://github.com/CupoMeridio/Wordageddon-G17/releases/tag/latest) come archivio `Wordageddon-javadoc.jar`.
- **Generazione locale da sorgenti**:
  ```bash
  mvn javadoc:javadoc
  ```

### 2. Relazione e Presentazione Accademica
- **Relazione Formale**: [`Relazione di progetto - Gruppo 17.pdf`](Relazione%20di%20progetto%20-%20Gruppo%2017.pdf)  
  Documento completo che descrive l'analisi dei requisiti, l'architettura MVC, i pattern adottati e le scelte progettuali.
- **Slide della Presentazione**: [`Presentazione progetto.pptx`](Presentazione%20progetto.pptx)  
  Presentazione utilizzata in sede di discussione accademica.

### 3. Diagrammi UML delle Classi
All'interno della cartella [`DiagrammiDelleClassi/`](DiagrammiDelleClassi/) sono disponibili:
- **`EasyUML/`**: esportazioni grafiche suddivise per caso d'uso e pacchetto logico.
- **`PlantUML/`**: sorgenti `.puml` modificabili e versionabili per il rendering automatico dei diagrammi di classe completi e semplificati.
