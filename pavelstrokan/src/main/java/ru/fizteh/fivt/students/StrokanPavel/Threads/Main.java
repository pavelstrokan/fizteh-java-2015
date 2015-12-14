package ru.fizteh.fivt.students.StrokanPavel.Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        counter(args);
    }

    private static void rollcall(String[] args) {
        int numberOfThreads = Integer.valueOf(args[0]);
        ExecutorService service = Executors.newWorkStealingPool();
        List<Future<Boolean>> futures = new ArrayList<>();
        boolean notReady = true;
        while (notReady) {
            System.out.println("Are you ready?");
            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(service.submit( () -> {
                    Random random = new Random();
                    return random.nextInt(10) != 0;
                }));
            }
            boolean ready = true;
            for (Future<Boolean> currentFuture : futures) {
                try {
                    Boolean value = currentFuture.get();
                    ready = ready && value;
                    System.out.println(ready ? "Yes" : "No");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    return;
                }
            }
            notReady = !ready;
            futures.clear();
        }
    }

    private static void counter(String[] args) {
        try {
            int count = Integer.valueOf(args[0]);
            SynchronizedCounter counter = new SynchronizedCounter(count);
            for (int i = 1; i <= count; i++) {
                new Thread(new Worker(i, counter)).start();
            }
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

}
