package mpd;

import java.sql.SQLOutput;
import java.util.Arrays;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    private int[] something;
    private int halfLength;
    private Result finalResult;

    @Override
    public long minimumPairwiseDistance(int[] values) {
        something = values.clone();
        halfLength = (something.length)/2;
        Arrays.sort(something);
        Result sharedResult = new Result();

        Thread[] thread = new Thread[4];
        thread[0] = new Thread(new LowLeft(sharedResult));
        thread[1] = new Thread(new LowRight(sharedResult));
        thread[2] = new Thread(new TopRight(sharedResult));
        thread[3] = new Thread(new Center(sharedResult));

        for (int i = 0; i < 4; i++) {
            thread[i].start();
        }

        try {
            for (int i = 0; i < 4; i++) {
                thread[i].join();
            }
        } catch(InterruptedException ie){
            System.out.println(ie);
        }


        return sharedResult.getResult();
    }

    class LowLeft implements Runnable{

        private LowLeft(Result r){
            finalResult = r;
        }
        public void run(){
            long result = finalResult.getResult();
            int j = 0;
            for (int i = j + 1; i < halfLength; ++i) {
                for (j = 0; j < i; ++j) {
                    long diff = Math.abs(something[i] - something[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            finalResult.setResult(result);
        }
    }

    class LowRight implements Runnable{

        private LowRight(Result r){
            finalResult = r;
        }

        public void run(){
            long result = finalResult.getResult();

            int j = 0;
            for (int i = j + 1 + halfLength; i < something.length; ++i) {
                for (j = 0; (j + halfLength) < i; ++j) {
                    long diff = Math.abs(something[i] - something[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            finalResult.setResult(result);
        }
    }

    class TopRight implements Runnable{

        private TopRight(Result r){
            finalResult = r;
        }

        public void run(){

            long result = finalResult.getResult();
            int j = halfLength;
            for (int i = j + 1; i < something.length; ++i) {
                for (j = halfLength; j < i; ++j) {
                    long diff = Math.abs(something[i] - something[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            finalResult.setResult(result);
        }
    }

    class Center implements Runnable{

        private Center(Result r){
            finalResult = r;
        }

        public void run(){
            long result = finalResult.getResult();
            int j = 0;
            for (int i = halfLength; i <= j + halfLength - 1; ++i) {
                for (j = 0; (j + halfLength) < something.length; ++j) {
                    long diff = Math.abs(something[i] - something[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            finalResult.setResult(result);
        }
    }

    private class Result {
        long result = Integer.MAX_VALUE;
        long getResult(){
            return result;
        }
        synchronized void setResult(long val){
            result = val;
        }
    }

}
