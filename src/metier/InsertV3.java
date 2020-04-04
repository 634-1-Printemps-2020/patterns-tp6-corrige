package metier;

import domaine.Personne;
import outils.AccessBdd;

import java.util.concurrent.Callable;

public class InsertV3 implements Callable<Void> {
    private String succ;
    private Personne pers;

    public InsertV3(String succ, Personne pers) { this.succ = succ; this.pers = pers; }

    @Override
    public Void call() {
        AccessBdd.insert(succ, pers);
        return null;
    }
}