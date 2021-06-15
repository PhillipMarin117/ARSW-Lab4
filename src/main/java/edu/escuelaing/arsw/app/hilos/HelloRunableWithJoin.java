package edu.escuelaing.arsw.app.hilos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloRunableWithJoin implements Runnable{

    public void run(){
        try{
            System.out.println("Time from thread: " + LocalDateTime.now());
            Thread.sleep(4000);
        }catch (InterruptedException ex){
            Logger.getLogger(HelloRunableWithJoin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main (String... args){
        int i = 0;
        Thread t = null;
        List<Thread> myThreads = new ArrayList<Thread>();
        while (i<4) {
            t = new Thread(new HelloRunableWithJoin());
            t.start();
            System.out.println("Time from main thread before join: " + LocalDateTime.now());
            i++;
            myThreads.add(t);
        }
        for(Thread currentThread : myThreads) {
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HelloRunableWithJoin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Time from main thread after join: " + LocalDateTime.now());
    }
}
