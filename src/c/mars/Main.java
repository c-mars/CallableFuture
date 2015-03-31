package c.mars;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
//        this is framework-based work - cool, but sometimes can't be applied
//        FutureTask<String> futureTask = new FutureTask<String>(new PromiseCallable());
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Future<String> future = executorService.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                System.out.println("call");
//                TimeUnit.SECONDS.sleep(3);
//                System.out.println("call finished");
//                return "spaceX";
//            }
//        });
//        executorService.shutdown();

//        this is manual creation of the future - almost universal
        RunnableFuture<String> future = new RunnableFuture<String>() {
            private String r;
            private boolean done;

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return done;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                synchronized (this) {
                    System.out.println("wait");
                    wait();
                    System.out.println("wait finished");
                }
                return r;
            }

            @Override
            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                synchronized (this) {
                    System.out.println("wait");
                    wait();
                    System.out.println("wait finished");
                }
                return r;
            }

            @Override
            public void run() {
                done = false;
                r = doJob();
                done = true;

                synchronized (this) {
                    notifyAll();
                }
            }
        };
        new Thread(future).start();
        System.out.println("thread started");

        try {
            System.out.println("result: "+future.get());
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
