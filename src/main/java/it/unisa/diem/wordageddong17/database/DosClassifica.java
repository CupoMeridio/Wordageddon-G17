package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.List;

public interface DosClassifica {
    public List<Classifica> prendiClassifica(LivelloPartita difficolta);
}
