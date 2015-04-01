# future.sample
CallableFuture is implementation of Future that returns value in .get() only when .call(U arg) method is called with argument.
This makes CallableFuture useful to notify code in separate threads about results of work of another threads.

