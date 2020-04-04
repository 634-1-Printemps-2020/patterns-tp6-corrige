package metier;

import domaine.Personne;
import outils.AccessBdd;

import java.util.concurrent.CountDownLatch;

public class InsertV2 extends Thread {
    private CountDownLatch countDownLatch;
    private String succ;
    private Personne pers;

    public InsertV2(String succ, Personne pers) { this.succ = succ; this.pers = pers; }
    public void setCountDownLatch(CountDownLatch countDownLatch) { this.countDownLatch = countDownLatch; }

    @Override
    public void run() {
        AccessBdd.insert(succ, pers);
        countDownLatch.countDown();
    }
}