-- =============================================================================
-- Wordageddon Seed Data
-- =============================================================================

-- Utenti di default:
-- 1. admin@wordageddon.it  / admin123  (Ruolo: amministratore)
-- 2. player@wordageddon.it / player123 (Ruolo: giocatore)
INSERT INTO public.utente (username, email, password, tipo)
VALUES
    ('admin', 'admin@wordageddon.it', '$2a$12$qhHL2vJtWbX5PvDbnUh7o.SnTblarYRctdr43mUMSeK2bHc/gSJgi', 'amministratore'),
    ('player1', 'player@wordageddon.it', '$2a$12$EtxZKKnh3qmFI.uFxbej8O1jYdQMXzMdf0zst4KqznG9iqUq2s5QG', 'giocatore')
ON CONFLICT (email) DO NOTHING;

-- Lista Stopwords italiana di default
INSERT INTO public.stopwords (nome_file, id_amministratore, documento)
VALUES (
    'stopwords_it.txt',
    'admin@wordageddon.it',
    convert_to(
        'a ad adesso ai al alla alle allo allora altre altri altro anche ancora avere aveva avevano ben che chi ciascuno con contro cui da dal dalla dalle dallo del dell della delle dello di dopo dove e ed era erano essa esse essi esso fare fino fra già gli i il in invece io la le lei li lo loro lui ma me mentre meno mi mia mie miei mio molte molti molto nei nel nella nelle nello noi non nostro ormai oltre per però più poco prima quale quali quando quasi quello questi questo qui quindi se senza sia solo sono su sua sue suoi suo tra tramite tu tutti tutto un una uno verso vi voi',
        'UTF8'
    )
)
ON CONFLICT (nome_file) DO NOTHING;

-- Documenti di Testo (Italiano - Facile)
INSERT INTO public.testo (nome_file, id_amministratore, difficolta, lingua, documento)
VALUES
(
    'divina_commedia_inferno_canto1.txt',
    'admin@wordageddon.it',
    'facile',
    'it',
    convert_to(
        'Nel mezzo del cammin di nostra vita mi ritrovai per una selva oscura ché la diritta via era smarrita. Ahi quanto a dir qual era è cosa dura esta selva selvaggia e aspra e forte che nel pensier rinova la paura! Tant è amara che poco è più morte; ma per trattar del ben ch i vi trovai, dirò de l altre cose ch i v ho scorte. Io non so ben ridir com i v entrai, tant era pien di sonno a quel punto che la verace via abbandonai.',
        'UTF8'
    )
),
(
    'promessi_sposi_incipit.txt',
    'admin@wordageddon.it',
    'facile',
    'it',
    convert_to(
        'Quel ramo del lago di Como, che volge a mezzogiorno, tra due catene non interrotte di monti, tutto a seni e a golfi, a seconda dello sporgere e del rientrare di quelli, vien, quasi a un tratto, a ristringersi, e a prender corso e figura di fiume, tra un promontorio a destra, e un ampia costiera dall altra parte; e il ponte, che ivi congiunge le due rive, par che renda ancor più sensibile all occhio questa trasformazione.',
        'UTF8'
    )
),
(
    'il_fu_mattia_pascal_cap1.txt',
    'admin@wordageddon.it',
    'facile',
    'it',
    convert_to(
        'Una delle poche cose, anzi forse la sola ch io sapessi di certo era questa: che mi chiamavo Mattia Pascal. E me ne prevalevo. Ogni qual volta qualcheduno de miei conoscenti o amici mostrava d aver perduto il senno fino al punto di venire a chiedermi qualche consiglio, io mi stringevo nelle spalle, socchiudevo gli occhi e gli rispondevo: Io mi chiamo Mattia Pascal.',
        'UTF8'
    )
)
ON CONFLICT (nome_file) DO NOTHING;

-- Documenti di Testo (Italiano - Medio)
INSERT INTO public.testo (nome_file, id_amministratore, difficolta, lingua, documento)
VALUES
(
    'canti_leopardi_infinito.txt',
    'admin@wordageddon.it',
    'medio',
    'it',
    convert_to(
        'Sempre caro mi fu quest ermo colle, e questa siepe, che da tanta parte dell ultimo orizzonte il guardo esclude. Ma sedendo e mirando, interminati spazi di là da quella, e sovrumani silenzi, e profondissima quiete io nel pensier mi fingo; ove per poco il cor non si spaura. E come il vento odo stormir tra queste piante, io quello infinito silenzio a questa voce vo comparando: e mi sovvien l eterno, e le morte stagioni, e la presente e viva, e il suon di lei. Così tra questa immensità s annega il pensier mio: e il naufragar m è dolce in questo mare.',
        'UTF8'
    )
),
(
    'decameron_novella_prima.txt',
    'admin@wordageddon.it',
    'medio',
    'it',
    convert_to(
        'Convenevole cosa è, carissime donne, che ciascuna cosa la quale l uomo fa, dallo ammirabile e santo nome di Colui il quale di tutte fu facitore le dea principio. Per che, dovendo io al nostro ragionare, prima che alcun altro, dare inizio, intendo da una delle sue maravigliose cose incominciare, acciò che, quella udita, la nostra speranza in Lui, sì come in cosa immutabile, si fermi e sempre sia il suo nome da noi lodato.',
        'UTF8'
    )
),
(
    'galileo_dialogo.txt',
    'admin@wordageddon.it',
    'medio',
    'it',
    convert_to(
        'La filosofia naturale è scritta in questo grandissimo libro che continuamente ci sta aperto innanzi a gli occhi, io dico l universo, ma non si può intendere se prima non s impara a intender la lingua e conoscer i caratteri ne quali è scritto. Egli è scritto in lingua matematica, e i caratteri son triangoli, cerchi ed altre figure geometriche, senza i quali mezzi è impossibile a intenderne umanamente parola; senza questi è un aggirarsi vanamente per un oscuro laberinto.',
        'UTF8'
    )
)
ON CONFLICT (nome_file) DO NOTHING;

-- Documenti di Testo (Italiano - Difficile)
INSERT INTO public.testo (nome_file, id_amministratore, difficolta, lingua, documento)
VALUES
(
    'machiavelli_il_principe.txt',
    'admin@wordageddon.it',
    'difficile',
    'it',
    convert_to(
        'Tutti gli stati, tutti e dominii che hanno avuto e hanno imperio sopra gli uomini, sono stati e sono o repubbliche o principati. I principati sono o ereditari, de quali el sangue del loro signore sia suto lungo tempo principe, o e sono nuovi. I nuovi, o e sono nuovi tutti, come fu Milano a Francesco Sforza, o sono come membri aggiunti allo stato ereditario del principe che gli acquista, come è el regno di Napoli al re di Spagna.',
        'UTF8'
    )
),
(
    'dante_paradiso_canto33.txt',
    'admin@wordageddon.it',
    'difficile',
    'it',
    convert_to(
        'Vergine Madre, figlia del tuo figlio, umile e alta più che creatura, termine fisso d etterno consiglio, tu se colei che l umana natura nobilitasti sì, che l suo fattore non disdegnò di farsi sua fattura. Nel ventre tuo si raccese l amore, per lo cui caldo ne l etterna pace così è germinato questo fiore. Qui se a noi meridiana face di caritate, e giuso, intra i mortali, se di speranza fontana vivace.',
        'UTF8'
    )
),
(
    'manzoni_adelchi_coro.txt',
    'admin@wordageddon.it',
    'difficile',
    'it',
    convert_to(
        'Dagli atri muscosi, dai fòri cadenti, dai boschi, dai forni squallenti ridesti, i volti dispersi, le membra sfiniti, dai solchi bagnati di servo sudor, un popolo timido, ignavo, s affretta, con l ansia nel viso, con tremito al cor. Ascolta il fragor de la pugna lontana, de brandi il percosso, del timpano il rombo, e spia de nemici la dubbia fortuna, sperando la pace da novo signor.',
        'UTF8'
    )
)
ON CONFLICT (nome_file) DO NOTHING;

-- Documenti di Testo (Inglese - Facile, Medio, Difficile)
INSERT INTO public.testo (nome_file, id_amministratore, difficolta, lingua, documento)
VALUES
(
    'alice_in_wonderland_ch1.txt',
    'admin@wordageddon.it',
    'facile',
    'en',
    convert_to(
        'Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, and what is the use of a book, thought Alice, without pictures or conversations? So she was considering in her own mind whether the pleasure of making a daisy chain would be worth the trouble.',
        'UTF8'
    )
),
(
    'pride_and_prejudice_ch1.txt',
    'admin@wordageddon.it',
    'facile',
    'en',
    convert_to(
        'It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife. However little known the feelings or views of such a man may be on his first entering a neighbourhood, this truth is so well fixed in the minds of the surrounding families, that he is considered the rightful property of some one or other of their daughters.',
        'UTF8'
    )
),
(
    'sherlock_holmes_scandal.txt',
    'admin@wordageddon.it',
    'facile',
    'en',
    convert_to(
        'To Sherlock Holmes she is always THE woman. I have seldom heard him mention her under any other name. In his eyes she eclipses and predominates the whole of her sex. It was not that he felt any emotion akin to love for Irene Adler. All emotions, and that one particularly, were abhorrent to his cold, precise but admirably balanced mind.',
        'UTF8'
    )
),
(
    'moby_dick_ch1.txt',
    'admin@wordageddon.it',
    'medio',
    'en',
    convert_to(
        'Call me Ishmael. Some years ago never mind how long precisely having little or no money in my purse, and nothing particular to interest me on shore, I thought I would sail about a little and see the watery part of the world. It is a way I have of driving off the spleen and regulating the circulation. Whenever I find myself growing grim about the mouth; whenever it is a damp, drizzly November in my soul.',
        'UTF8'
    )
),
(
    'tale_of_two_cities_ch1.txt',
    'admin@wordageddon.it',
    'medio',
    'en',
    convert_to(
        'It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of light, it was the season of darkness, it was the spring of hope, it was the winter of despair. We had everything before us, we had nothing before us, we were all going direct to Heaven, we were all going direct the other way.',
        'UTF8'
    )
),
(
    'frankenstein_ch1.txt',
    'admin@wordageddon.it',
    'medio',
    'en',
    convert_to(
        'I am by birth a Genevese, and my family is one of the most distinguished of that republic. My ancestors had been for many years counsellors and syndics, and my father had filled several public situations with honour and reputation. He was respected by all who knew him for his integrity and indefatigable attention to public business.',
        'UTF8'
    )
),
(
    'hamlet_soliloquy.txt',
    'admin@wordageddon.it',
    'difficile',
    'en',
    convert_to(
        'To be, or not to be, that is the question: whether tis nobler in the mind to suffer the slings and arrows of outrageous fortune, or to take arms against a sea of troubles and by opposing end them. To die, to sleep no more, and by a sleep to say we end the heart ache and the thousand natural shocks that flesh is heir to tis a consummation devoutly to be wished.',
        'UTF8'
    )
),
(
    'edgar_allan_poe_raven.txt',
    'admin@wordageddon.it',
    'difficile',
    'en',
    convert_to(
        'Once upon a midnight dreary, while I pondered, weak and weary, over many a quaint and curious volume of forgotten lore while I nodded, nearly napping, suddenly there came a tapping, as of some one gently rapping, rapping at my chamber door. Tis some visitor, I muttered, tapping at my chamber door only this and nothing more.',
        'UTF8'
    )
),
(
    'heart_of_darkness.txt',
    'admin@wordageddon.it',
    'difficile',
    'en',
    convert_to(
        'The Nellie, a cruising yawl, swung to her anchor without a flutter of the sails, and was at rest. The flood had made, the wind was nearly calm, and being bound down the river, the only thing for it was to come to and wait for the turn of the tide. The sea reach of the Thames stretched before us like the beginning of an interminable waterway.',
        'UTF8'
    )
)
ON CONFLICT (nome_file) DO NOTHING;

-- Punteggi dimostrativi per popolare la classifica
INSERT INTO public.punteggio (email_utente, punti, difficolta, data)
VALUES
    ('player@wordageddon.it', 250.0, 'facile',    CURRENT_TIMESTAMP - INTERVAL '2 days'),
    ('player@wordageddon.it', 380.0, 'facile',    CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ('player@wordageddon.it', 420.0, 'medio',     CURRENT_TIMESTAMP - INTERVAL '12 hours'),
    ('player@wordageddon.it', 510.0, 'difficile', CURRENT_TIMESTAMP - INTERVAL '2 hours')
ON CONFLICT DO NOTHING;
