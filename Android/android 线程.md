

## AsyncTask

###  简单使用：

- onPreExecute：在主线程中执行，在异步线程执行前执行，一般用于准备一些参数之类的工作

- onInBackgroud(params...):在线陈池中执行，真正的异步任务执行题。

- onProgressUpdate(Progress.. )在主线程中执行 ，当后台人会执行进度发生改变的时候此方法回被调用。

- onPostExecute(Result result)  在主线程中执行，是异步线程执行完了后的结果返回。


- onCancelled()方法是异步线程被取消的时候调用。

#  总结：


asyncTask  的对象必须在主线程中创建：通过源码的AsyncTask对象的构造方法 可以知道 不是主线程是不行的

因为：他会使用到handler用来更行ui,会调用looper.getMainLooper(）创建，所以必须是主线程。

execute方法必须在ui线程中被调用：new **Task().execute()

因为：通过源码可以发现 他是会调用 onPreExecute()方法 ，由于这个方法是在UI线程中执行，所以必须在ui线程中

不能在程序中直接调用 线程中提供的方法：

因为：


AsyncTask 对象只能执行一次：

因为：
