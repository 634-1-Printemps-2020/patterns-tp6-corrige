package metier;

import domaine.Personne;
import outils.AccessBdd;

public class InsertV1 extends Thread {
    private String succ;
    private Personne pers;

    public InsertV1(String succ, Personne pers) { this.succ = succ; this.pers = pers; }

    @Override
    public void run() {
        AccessBdd.insert(succ, pers);
    }
}