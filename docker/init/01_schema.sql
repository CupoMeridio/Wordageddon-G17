-- =============================================================================
-- Wordageddon Database Schema (PostgreSQL 17 compatible)
-- Gruppo 17 - Università degli Studi di Salerno
-- =============================================================================

-- Tipo enumerato per le lingue supportate dai documenti di testo
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'lingua_type') THEN
        CREATE TYPE public.lingua_type AS ENUM ('it', 'en', 'es', 'fr', 'de');
    END IF;
END $$;

-- -----------------------------------------------------------------------------
-- Tabella: utente
-- Memorizza gli utenti registrati (giocatori o amministratori)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.utente (
    username VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password CHAR(60) NOT NULL,
    foto_profilo BYTEA,
    tipo CHAR(14) DEFAULT 'giocatore'::bpchar NOT NULL,
    CONSTRAINT utente_pkey PRIMARY KEY (email),
    CONSTRAINT utente_username_key UNIQUE (username),
    CONSTRAINT utente_tipo_check CHECK (lower(tipo::text) = ANY (ARRAY['giocatore'::text, 'amministratore'::text]))
);

-- -----------------------------------------------------------------------------
-- Tabella: testo
-- Memorizza i testi di gioco caricati dagli amministratori
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.testo (
    nome_file VARCHAR(255) NOT NULL,
    id_amministratore VARCHAR(100),
    difficolta CHAR(9) NOT NULL,
    documento BYTEA NOT NULL,
    lingua public.lingua_type,
    CONSTRAINT testo_pkey PRIMARY KEY (nome_file),
    CONSTRAINT testo_id_amministratore_fkey FOREIGN KEY (id_amministratore)
        REFERENCES public.utente(email) ON DELETE SET NULL,
    CONSTRAINT testo_difficolta_check CHECK (lower(difficolta::text) = ANY (ARRAY['facile'::text, 'medio'::text, 'difficile'::text]))
);

-- -----------------------------------------------------------------------------
-- Tabella: stopwords
-- Memorizza la lista di stop words caricata dagli amministratori
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.stopwords (
    nome_file VARCHAR(255) NOT NULL,
    data_ultima_modifica TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    documento BYTEA NOT NULL,
    id_amministratore VARCHAR(100),
    CONSTRAINT stopwords_pkey PRIMARY KEY (nome_file),
    CONSTRAINT stopwords_id_amministratore_fkey FOREIGN KEY (id_amministratore)
        REFERENCES public.utente(email) ON DELETE SET NULL
);

-- -----------------------------------------------------------------------------
-- Tabella: punteggio
-- Memorizza la cronologia dei punteggi ottenuti nelle sessioni di gioco
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.punteggio (
    email_utente VARCHAR(100) NOT NULL,
    data TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    punti DOUBLE PRECISION NOT NULL,
    difficolta CHAR(9) NOT NULL,
    CONSTRAINT punteggio_pkey PRIMARY KEY (email_utente, data),
    CONSTRAINT punteggio_email_utente_fkey FOREIGN KEY (email_utente)
        REFERENCES public.utente(email) ON DELETE CASCADE,
    CONSTRAINT punteggio_difficolta_check CHECK (lower(difficolta::text) = ANY (ARRAY['facile'::text, 'medio'::text, 'difficile'::text]))
);

-- -----------------------------------------------------------------------------
-- Funzioni e Trigger
-- -----------------------------------------------------------------------------

-- Verifica che solo un amministratore possa associare stopwords
CREATE OR REPLACE FUNCTION public.check_admin_stopwords()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.id_amministratore IS NOT NULL AND NOT EXISTS (
        SELECT 1 FROM public.utente WHERE email = NEW.id_amministratore AND tipo = 'amministratore'
    ) THEN
        RAISE EXCEPTION 'L''utente associato alle stopwords (%) deve avere ruolo amministratore', NEW.id_amministratore;
    END IF;
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS enforce_admin_stopwords ON public.stopwords;
CREATE TRIGGER enforce_admin_stopwords
    BEFORE INSERT OR UPDATE ON public.stopwords
    FOR EACH ROW EXECUTE FUNCTION public.check_admin_stopwords();

-- Verifica che solo un amministratore possa caricare documenti di testo
CREATE OR REPLACE FUNCTION public.check_admin_testo()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.id_amministratore IS NOT NULL AND NOT EXISTS (
        SELECT 1 FROM public.utente WHERE email = NEW.id_amministratore AND tipo = 'amministratore'
    ) THEN
        RAISE EXCEPTION 'L''utente caricatore del documento (%) deve avere ruolo amministratore', NEW.id_amministratore;
    END IF;
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS enforce_admin_testo ON public.testo;
CREATE TRIGGER enforce_admin_testo
    BEFORE INSERT OR UPDATE ON public.testo
    FOR EACH ROW EXECUTE FUNCTION public.check_admin_testo();

-- All'inserimento di una nuova lista stopwords, rimuove le precedenti se con nome diverso
CREATE OR REPLACE FUNCTION public.remove_stopwords()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM public.stopwords WHERE nome_file <> NEW.nome_file;
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trigger_remove_stopwords ON public.stopwords;
CREATE TRIGGER trigger_remove_stopwords
    BEFORE INSERT ON public.stopwords
    FOR EACH ROW EXECUTE FUNCTION public.remove_stopwords();

-- -----------------------------------------------------------------------------
-- Indici prestazionali per query frequenti
-- -----------------------------------------------------------------------------
CREATE INDEX IF NOT EXISTS idx_testo_difficolta_lingua ON public.testo (difficolta, lingua);
CREATE INDEX IF NOT EXISTS idx_punteggio_difficolta_punti ON public.punteggio (difficolta, punti DESC);
CREATE INDEX IF NOT EXISTS idx_punteggio_utente_data ON public.punteggio (email_utente, data DESC);
