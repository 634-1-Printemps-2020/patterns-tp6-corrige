package metier;

import outils.AccessBdd;

import java.util.concurrent.CountDownLatch;

public class Migrate implements Runnable {
    private CountDownLatch latch;
    private String succ;
    private String master;

    public Migrate(CountDownLatch latch, String succ, String master) {
        this.latch = latch;
        this.succ = succ;
        this.master = master;
    }

    @Override
    public void run() {
        AccessBdd.migrate(succ, master);
        latch.countDown();
    }
}