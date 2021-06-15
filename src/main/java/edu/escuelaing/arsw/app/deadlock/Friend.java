package edu.escuelaing.arsw.app.deadlock;


public class Friend {
    private final String name;

    public Friend (String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public synchronized void bow(Friend bower){
        System.out.format("%s: %S has bowed me! %n", name, bower.getName());
        bower.bowBack(this);
    }

    public synchronized void bowBack (Friend bower){
        System.out.format("%s: %S has bowed back to me! %n", name, bower.getName());
    }

    public static void main (String... args){
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
        //(new Thread(() -> alphonse.bow(gaston))).start();
        //(new Thread(() -> gaston.bow(alphonse))).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                alphonse.bow(gaston);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                gaston.bow(alphonse);
            }
        }).start();
    }
}


