package ru.fizteh.fivt.students.StrokanPavel.Threads;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {

    private Queue<T> myBlockingQueue;
    private int elementsBound;
    private static Lock offerLock;
    private static Lock takeLock;
    private static Lock stepLock;
    private static Condition nonEmpty;
    private static Condition nonFull;

    public List<T> getData() {
        return new ArrayList<>(myBlockingQueue);
    }

    public BlockingQueue(int elementsBound) {
        this.elementsBound = elementsBound;
        this.myBlockingQueue = new LinkedList<T>();
        offerLock = new ReentrantLock(true);
        takeLock = new ReentrantLock(true);
        stepLock = new ReentrantLock(true);
        nonEmpty = stepLock.newCondition();
        nonFull = stepLock.newCondition();
    }

    public void offer(List<T> toAdd) {
        try {
            offerLock.lock();
            for (int i = 0; i < toAdd.size(); ++i) {
                try {
                    stepLock.lock();
                    while (myBlockingQueue.size() == elementsBound) {
                        nonFull.await();
                    }
                    myBlockingQueue.add(toAdd.get(i));
                    nonEmpty.signalAll();
                } catch (InterruptedException e) {
                    int helloCheckStyle;
                } finally {
                    stepLock.unlock();
                }
            }
        } finally {
            offerLock.unlock();
        }
    }

    public List<T> take(int numberOfElements) {
        List<T> takenElements = new ArrayList<T>();
        try {
            takeLock.lock();
            for (int i = 0; i < numberOfElements; ++i) {
                try {
                    stepLock.lock();
                    while (myBlockingQueue.size() == 0) {
                        nonEmpty.await();
                    }
                    takenElements.add(myBlockingQueue.poll());
                    nonFull.signalAll();
                } catch (InterruptedException e) {
                    int helloCheckStyle2;
                } finally {
                    stepLock.unlock();
                }
            }
        } finally {
            takeLock.unlock();
            return takenElements;
        }
    }
}
