package c.mars;

import java.util.concurrent.*;

/**
 * Created by Constantine Mars on 4/1/15.
 *
 * can be called and can return future
 */
public class CallableFuture<T> {
    public CallableFuture(Callable<T> callable) {
        this.callable = callable;
    }

    private Callable<T> callable;
    private RunnableFuture<T> runnableFuture;

    public void call() {
        runnableFuture = getRunnableFuture();
        new Thread(runnableFuture).start();
    }

    public T get() throws ExecutionException, InterruptedException {
        return getRunnableFuture().get();
    }

    private RunnableFuture<T> getRunnableFuture() {
        if (runnableFuture == null) {
            runnableFuture = new RunnableFutureImpl();
        }
        return runnableFuture;
    }

    private class RunnableFutureImpl implements RunnableFuture<T> {

        private T result;
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
        public T get() throws InterruptedException, ExecutionException {
            synchronized (this) {
                System.out.println("wait");
                wait();
                System.out.println("wait finished");
            }
            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
        {
            synchronized (this) {
                System.out.println("wait");
                wait();
                System.out.println("wait finished");
            }
            return result;
        }

        @Override
        public void run() {
            done = false;
            try {
                result = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;

            synchronized (this) {
                notifyAll();
            }
        }
    }
}
