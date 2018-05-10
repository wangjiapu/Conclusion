




- 开启线程来实现异步操作．
- 使用handler来实现异步机制．
- 当然也可以使用AsyncTask来实现异步．
1. 当我们使用AsyncTask的时候，execute()必须要在主线程中调用，当我们去查看源代码的时候就会发现AsyncTask内部使用了两个线程池和一个handler，
两个线程池的作用其一是：维护耗时任务排队执行，另一个是真正执行任务的线程，还有一个handler是用来在ｕｉ线程操作的东西．
    １２８
- RxJava y异步
RxJava 是通过观察着模式来实现异步的．通过subscribeon()方法来指定被观察者线程，然后将那个线程指定某某线程，observeon()