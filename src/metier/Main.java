package metier;

import domaine.Personne;
import outils.AccessBdd;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<String> lstSucc = AccessBdd.listeSuccursales();

        // =========================================================================================
        // Version 1: on attend la fin de chaque thread (join) ==> il faut donc conserver(stocker) ces threads
        List<InsertV1> lstExecV1 = new ArrayList<>();
        for (String succ : lstSucc) {
            List<Personne> lstPers = AccessBdd.listePersonnes(succ);
            for (Personne pers : lstPers) {
                InsertV1 insertV1 = new InsertV1(succ, pers);
                lstExecV1.add(insertV1);
                insertV1.start();
            }
        }

        for (InsertV1 insertV1 : lstExecV1) {
            insertV1.join();
        }
        System.out.println("Insertions terminées");

        // =========================================================================================
        // Version 2: utiliser un CountDownLatch ==> il faut donc d'abord compter le nombre d'insert ==> et stocker les insert
        List<InsertV2> lstExecV2 = new ArrayList<>();
        for (String succ : lstSucc) {
            List<Personne> lstPers = AccessBdd.listePersonnes(succ);
            for (Personne pers : lstPers) {
                lstExecV2.add(new InsertV2(succ, pers));
            }
        }

        CountDownLatch latch = new CountDownLatch(lstExecV2.size());
        for (InsertV2 insertV2 : lstExecV2) {
            insertV2.setCountDownLatch(latch);
            insertV2.start();
        }
        latch.await();
        System.out.println("Insertions terminées");

        // =========================================================================================
        // Version 3: utiliser un ExecutorService ==> invokeAll() attend la fin de tous les threads
        List<InsertV3> lstExecV3 = new ArrayList<>();
        ExecutorService exec = Executors.newCachedThreadPool();
        for (String succ : lstSucc) {
            List<Personne> lstPers = AccessBdd.listePersonnes(succ);
            for (Personne pers : lstPers) {
                lstExecV3.add(new InsertV3(succ, pers));
            }
        }
        exec.invokeAll(lstExecV3);
        exec.shutdown();
        System.out.println("Insertions terminées");

        // =========================================================================================

        System.out.println("Début migrations...");
        String master = AccessBdd.master();

        latch = new CountDownLatch(lstSucc.size());
        for (String succ : lstSucc) {
            new Thread(new Migrate(latch, succ, master)).start();
        }
        latch.await();
        System.out.println("Migrations terminées");
    }
}