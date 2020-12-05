package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int numThreads;

    public MultiThreadedSumMatrix(final int n) {
        this.numThreads = n;
    }

    @Override
    public double sum(final double[][] matrix) {
        final int nElem = matrix.length / numThreads + matrix.length % numThreads; 
        int startIndex = 0; //punto di partenza da dove contare gli elementi

        final List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) { //per ogni thread
            workers.add(new Worker(matrix, nElem, startIndex)); //aggiungo un worker alla lista dei worker
            startIndex += nElem;
        }
        //faccio partire i workers tutti insieme
        for (final Worker w: workers) {
            w.start();
        }
        double sum = 0;

        //aspetto che ogni thread finisca il proprio lavoro
        for (final Worker w: workers) {
            try {
                w.join();
                //aggiungo alla somma totale quello che ha calcolato il thread
                sum += w.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }
        return sum;
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int nElem; //numero di elementi da sommare
        private final int startIndex;
        private double result;
        /**
         * 
         * @param matrix matrice di riferimento
         * @param nElem numero di elementi da contare
         * @param startIndex indice 
         */
        Worker(final double[][] matrix, final int nElem, final int startIndex) {
            this.matrix = Arrays.copyOf(matrix, matrix.length);
            this.nElem = nElem;
            this.startIndex = startIndex;
        }

        @Override
        public void run() {
            for (int i = this.startIndex; i < this.startIndex + this.nElem && i < matrix.length; i++) {
                for (final double d: matrix[i]) { //per ogni elemento della riga
                    result += d;
                }
            }
        }

        private double getResult() {
            return this.result;
        }
    }

}
