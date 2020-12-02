package it.unibo.oop.lab.reactivegui03;
import it.unibo.oop.lab.reactivegui02.ConcurrentGUI;

public class AnotherConcurrentGUI extends ConcurrentGUI {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int WAIT_TIME = 10_000;

    public AnotherConcurrentGUI() {
        super();
        final Agent agent = new Agent();
        new Thread(agent).start();
    }

    private class Agent implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(AnotherConcurrentGUI.WAIT_TIME);
                AnotherConcurrentGUI.super.getAgent().stopCounting();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread ha aspettato 10 secondi");
        }
    }
}
