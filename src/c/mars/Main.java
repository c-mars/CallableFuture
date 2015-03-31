package c.mars;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

//        using CallableFuture
        final CallableFuture<String> callableFuture = new CallableFuture<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
//                this should be returned after outer callback done
                return "get some!";
            }
        });

//        this thread emulates outer callback that can be performed at anytime from outside
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("outer thread...");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("outer callback - callableFuture.call()");
//                callableFuture can be called from anywhere - and it should trigger unblocking of .get()
                callableFuture.call();
                System.out.println("callableFuture.called");
            }
        }).start();

        try {
//            .get() acts as usual Future - it blocks until any outer thread or callback trigger futureCallable.call()
            System.out.println("result: "+callableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static String doJob() {
        System.out.println("doing job");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("job completed");
        return "super product";
    }
}
