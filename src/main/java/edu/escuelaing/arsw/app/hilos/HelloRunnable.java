package edu.escuelaing.arsw.app.hilos;

public class HelloRunnable implements Runnable{
    public void run(){
        System.out.println("Hello from runnable!: "+ this);
    }

    public static void main(String ... args){
        int i = 0;
        while (i<20) {
            (new Thread(new HelloRunnable())).start();
            i++;
        }

    }
}
