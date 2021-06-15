package edu.escuelaing.arsw.app.hilos;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SynchronizedCounters {

    private long c1 = 0;
    private long c2 = 0;
    Object lock1 = new Object();
    Object lock2 = new Object();

    public long incC1 (){
        synchronized (lock1){
            c1++;
            try{
                Thread.sleep(4000);
            }catch (InterruptedException ex){
                Logger.getLogger(HelloRunableWithJoin.class.getName()).log(Level.SEVERE, null, ex);
            }
            return c1;
        }

    }

    public synchronized long incC2(){
        synchronized (lock2) {
            c2++;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HelloRunableWithJoin.class.getName()).log(Level.SEVERE, null, ex);
            }
            return c2;
        }
    }

    public static void main(String... args){
        SynchronizedCounters sc = new SynchronizedCounters();
        Runnable r1 = () -> System.out.println("Counter 1: " + sc.incC1());
        Runnable r2= () -> System.out.println("Counter 2: " + sc.incC2());
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }
}
