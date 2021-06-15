package edu.escuelaing.arsw.app.hilos;

public class HelloThread extends Thread{

    public void run (){
        System.out.println("Hello from thread: " + this);
    }
    public static void main(String ... args){
        int i = 0;
        while (i < 20){
            (new HelloThread()).start();
            i++;
        }

    }
}
