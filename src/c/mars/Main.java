package c.mars;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

        System.out.println(">> callableFuture created");
        final CallableFuture<String, Integer> callableFuture = new CallableFuture<String, Integer>(new CallableFuture.CallableWithArg<String, Integer>() {
            @Override
            public String call(Integer integer) {
                return "\"resulting status is "+integer+"\"";
            }
        });

//        this thread emulates outer callback that can be performed at anytime from outside
        System.out.println(">> callableFuture submitted to new thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("outer thread is running...");
                try {
                    int steps = 5;
                    for (int i=0; i<steps; i++) {
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("running... ["+(i+1)*100/steps+"%]");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(">> calling callableFuture.call() from a thread with argument 66");
//                callableFuture can be called from anywhere - and it should trigger unblocking of .get()
                callableFuture.call(66);
                System.out.println("callableFuture was called");
            }
        }).start();

        try {
//            .get() acts as usual Future - it blocks until any outer thread or callback trigger futureCallable.call()
            System.out.println(">> waiting for result in main()...");
            System.out.println(">> result obtained: "+callableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
