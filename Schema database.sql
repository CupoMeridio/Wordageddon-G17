PGDMP                      }        	   defaultdb    16.9    17.5     e           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            f           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            g           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            h           1262    16442 	   defaultdb    DATABASE     u   CREATE DATABASE defaultdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.UTF-8';
    DROP DATABASE defaultdb;
                     avnadmin    false            [           1247    17064    lingua_type    TYPE     ]   CREATE TYPE public.lingua_type AS ENUM (
    'it',
    'en',
    'es',
    'fr',
    'de'
);
    DROP TYPE public.lingua_type;
       public               avnadmin    false            �            1255    16635    check_admin()    FUNCTION     -  CREATE FUNCTION public.check_admin() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM utente WHERE id = NEW.id AND tipo = 'amministratore'
    ) THEN
        RAISE EXCEPTION 'L''utente deve essere un amministratore!';
    END IF;
    RETURN NEW;
END;
$$;
 $   DROP FUNCTION public.check_admin();
       public               avnadmin    false            �            1255    16942    check_admin_stopwords()    FUNCTION     I  CREATE FUNCTION public.check_admin_stopwords() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM utente WHERE email = NEW.id_amministratore AND tipo = 'amministratore'
    ) THEN
        RAISE EXCEPTION 'L''utente deve essere un amministratore!';
    END IF;
    RETURN NEW;
END;
$$;
 .   DROP FUNCTION public.check_admin_stopwords();
       public               avnadmin    false            �            1255    16941    check_admin_testo()    FUNCTION     E  CREATE FUNCTION public.check_admin_testo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM utente WHERE email = NEW.id_amministratore AND tipo = 'amministratore'
    ) THEN
        RAISE EXCEPTION 'L''utente deve essere un amministratore!';
    END IF;
    RETURN NEW;
END;
$$;
 *   DROP FUNCTION public.check_admin_testo();
       public               avnadmin    false            �            1255    16633    remove_stopwords()    FUNCTION     �   CREATE FUNCTION public.remove_stopwords() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	DELETE FROM stopwords
	WHERE TRUE; -- DA MODIFICARE IN FUTURO PER GESTIONE MULTILINGUA
	RETURN NEW;
END;
$$;
 )   DROP FUNCTION public.remove_stopwords();
       public               avnadmin    false            �            1259    16957 	   punteggio    TABLE     w  CREATE TABLE public.punteggio (
    email_utente character varying(100) NOT NULL,
    data timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    punti double precision NOT NULL,
    difficolta character(9) NOT NULL,
    CONSTRAINT punteggio_difficolta_check CHECK ((lower((difficolta)::text) = ANY (ARRAY['facile'::text, 'medio'::text, 'difficile'::text])))
);
    DROP TABLE public.punteggio;
       public         heap r       avnadmin    false            �            1259    16981 	   stopwords    TABLE     �   CREATE TABLE public.stopwords (
    nome_file character varying(255) NOT NULL,
    data_ultima_modifica timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    documento bytea NOT NULL,
    id_amministratore character varying(100)
);
    DROP TABLE public.stopwords;
       public         heap r       avnadmin    false            �            1259    16968    testo    TABLE     i  CREATE TABLE public.testo (
    nome_file character varying(255) NOT NULL,
    id_amministratore character varying(100),
    difficolta character(9) NOT NULL,
    documento bytea NOT NULL,
    lingua public.lingua_type,
    CONSTRAINT testo_difficolta_check CHECK ((lower((difficolta)::text) = ANY (ARRAY['facile'::text, 'medio'::text, 'difficile'::text])))
);
    DROP TABLE public.testo;
       public         heap r       avnadmin    false    859            �            1259    16946    utente    TABLE     a  CREATE TABLE public.utente (
    username character varying(30) NOT NULL,
    email character varying(100) NOT NULL,
    password character(60) NOT NULL,
    foto_profilo bytea,
    tipo character(14) DEFAULT 'giocatore'::bpchar,
    CONSTRAINT utente_tipo_check CHECK ((lower((tipo)::text) = ANY (ARRAY['giocatore'::text, 'amministratore'::text])))
);
    DROP TABLE public.utente;
       public         heap r       avnadmin    false            �           2606    16962    punteggio punteggio_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.punteggio
    ADD CONSTRAINT punteggio_pkey PRIMARY KEY (email_utente, data);
 B   ALTER TABLE ONLY public.punteggio DROP CONSTRAINT punteggio_pkey;
       public                 avnadmin    false    216    216            �           2606    16988    stopwords stopwords_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.stopwords
    ADD CONSTRAINT stopwords_pkey PRIMARY KEY (nome_file);
 B   ALTER TABLE ONLY public.stopwords DROP CONSTRAINT stopwords_pkey;
       public                 avnadmin    false    218            �           2606    16975    testo testo_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.testo
    ADD CONSTRAINT testo_pkey PRIMARY KEY (nome_file);
 :   ALTER TABLE ONLY public.testo DROP CONSTRAINT testo_pkey;
       public                 avnadmin    false    217            �           2606    16954    utente utente_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (email);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public                 avnadmin    false    215            �           2606    16956    utente utente_username_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_username_key UNIQUE (username);
 D   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_username_key;
       public                 avnadmin    false    215            �           2620    16995 !   stopwords enforce_admin_stopwords    TRIGGER     �   CREATE TRIGGER enforce_admin_stopwords BEFORE INSERT OR UPDATE ON public.stopwords FOR EACH ROW EXECUTE FUNCTION public.check_admin_stopwords();
 :   DROP TRIGGER enforce_admin_stopwords ON public.stopwords;
       public               avnadmin    false    233    218            �           2620    16994    testo enforce_admin_testo    TRIGGER     �   CREATE TRIGGER enforce_admin_testo BEFORE INSERT OR UPDATE ON public.testo FOR EACH ROW EXECUTE FUNCTION public.check_admin_testo();
 2   DROP TRIGGER enforce_admin_testo ON public.testo;
       public               avnadmin    false    217    232            �           2620    16996 "   stopwords trigger_remove_stopwords    TRIGGER     �   CREATE TRIGGER trigger_remove_stopwords BEFORE INSERT ON public.stopwords FOR EACH ROW EXECUTE FUNCTION public.remove_stopwords();
 ;   DROP TRIGGER trigger_remove_stopwords ON public.stopwords;
       public               avnadmin    false    218    230            �           2606    17013 %   punteggio punteggio_email_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.punteggio
    ADD CONSTRAINT punteggio_email_utente_fkey FOREIGN KEY (email_utente) REFERENCES public.utente(email) ON DELETE CASCADE;
 O   ALTER TABLE ONLY public.punteggio DROP CONSTRAINT punteggio_email_utente_fkey;
       public               avnadmin    false    216    4293    215            �           2606    16989 *   stopwords stopwords_id_amministratore_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stopwords
    ADD CONSTRAINT stopwords_id_amministratore_fkey FOREIGN KEY (id_amministratore) REFERENCES public.utente(email);
 T   ALTER TABLE ONLY public.stopwords DROP CONSTRAINT stopwords_id_amministratore_fkey;
       public               avnadmin    false    4293    215    218            �           2606    16976 "   testo testo_id_amministratore_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.testo
    ADD CONSTRAINT testo_id_amministratore_fkey FOREIGN KEY (id_amministratore) REFERENCES public.utente(email);
 L   ALTER TABLE ONLY public.testo DROP CONSTRAINT testo_id_amministratore_fkey;
       public               avnadmin    false    4293    215    217           