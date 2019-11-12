package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    private int[] valueArray;
    private int halfLength;
    private Result finalResult;

    @Override
    public long minimumPairwiseDistance(int[] values) {
        valueArray = values;
        halfLength = (valueArray.length)/2;
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
            long result = Integer.MAX_VALUE;

            for (int i = 0; i < halfLength; ++i) {
                for (int j = 0; j < i; ++j) {
                    long diff = Math.abs(valueArray[i] - valueArray[j]);
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
            long result = Integer.MAX_VALUE;

            for (int i = halfLength; i < valueArray.length; ++i) {
                for (int j = 0; (j + halfLength) < i; ++j) {
                    long diff = Math.abs(valueArray[i] - valueArray[j]);
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
            long result = Integer.MAX_VALUE;

            for (int i = halfLength; i < valueArray.length; ++i) {
                for (int j = halfLength; j < i; ++j) {
                    long diff = Math.abs(valueArray[i] - valueArray[j]);
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
            long result = Integer.MAX_VALUE;

            for (int j = 0; (j+halfLength) < valueArray.length; ++j) {
                for (int i = halfLength; i  < (j+halfLength); ++i) {
                    long diff = Math.abs(valueArray[i] - valueArray[j]);
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
            if(val < result) {
                result = val;
            }
        }
    }

}
