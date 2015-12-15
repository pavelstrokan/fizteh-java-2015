package ru.fizteh.fivt.students.StrokanPavel.Threads;

public class Worker extends Thread {

    private int number;
    private SynchronizedCounter counter;

    public Worker(int n, SynchronizedCounter c) {
        number = n;
        counter = c;
    }

    public void run() {
        while (true) {
            if (counter.value() == number) {
                System.out.println("Thread#" + number);
                counter.increment();
                synchronized (this.getClass()) {
                    this.getClass().notifyAll();
                }
            }
        }
    }
}
