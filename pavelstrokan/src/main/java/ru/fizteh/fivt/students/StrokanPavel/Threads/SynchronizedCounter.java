package ru.fizteh.fivt.students.StrokanPavel.Threads;

public class SynchronizedCounter {
    private volatile int count = 1;
    private int number;

    public SynchronizedCounter(int n) {
        number = n;
    }

    public synchronized void increment() {
        count = (count % number) + 1;
    }
    public synchronized int value() {
        return count;
    }
}

