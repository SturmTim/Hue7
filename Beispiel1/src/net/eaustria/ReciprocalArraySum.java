/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria;

/**
 *
 * @author bmayr
 */
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Class wrapping methods for implementing reciprocal array sum in parallel.
 */
public final class ReciprocalArraySum {

    /**
     * Default constructor.
     */
    private ReciprocalArraySum() {
    }

    /**
     * Sequentially compute the sum of the reciprocal values for a given array.
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double seqArraySum(final double[] input) {
        double sum = 0;

        for (double number : input) {
            sum += 1 / number;
        }

        return sum;
    }

    private static class ReciprocalArraySumTask extends RecursiveAction {

        private final int startIndexInclusive;

        private final int endIndexExclusive;

        private final double[] input;

        private double value;

        private static int SEQUENTIAL_THRESHOLD = 50000;

        ReciprocalArraySumTask(final int setStartIndexInclusive, final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
        }

        public double getValue() {
            return value;
        }

        @Override
        protected void compute() {
            if (endIndexExclusive - startIndexInclusive < SEQUENTIAL_THRESHOLD) {
                value = seqArraySum(input);
            } else {
                int middle = startIndexInclusive + ((endIndexExclusive - startIndexInclusive) / 2);
                ReciprocalArraySumTask sumActionFirstHalf = new ReciprocalArraySumTask(startIndexInclusive, middle, input);
                ReciprocalArraySumTask sumActionScondHalf = new ReciprocalArraySumTask(middle, endIndexExclusive, input);
                invokeAll(sumActionFirstHalf, sumActionScondHalf);

                sumActionFirstHalf.join();
                sumActionScondHalf.join();

                value = sumActionFirstHalf.getValue() + sumActionScondHalf.getValue();
            }
        }
    }

    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
        double sum = 0;
        ForkJoinPool pool = new ForkJoinPool(numTasks);

        ReciprocalArraySumTask sumAction = new ReciprocalArraySumTask(0, input.length, input);

        pool.invoke(sumAction);

        sum = sumAction.getValue();
        return sum;
    }

}
