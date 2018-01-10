# handler
## 我们的项目

想到ｈａｎｌｅｒ是因为我们的在处理消息的时候使用到了一个消息队列，因为handler的核心应该就算是消息队列的处理，起初我们也打算使用基本的队列，按照时间发生顺序添加，但是最后通过应为涉及到了多线程的问题，只能通过枷锁来控制队列的添加消息出现数据混乱，但是我们都知道ｈａｎｄｌｅｒ中的消息队列不是通过枷锁来时实现同步的，所以就去看了一下handler的消息队列是怎么实现的，

## handler
android 的消息机制的上层接口，
有MessageQueue, Handler, Looper,Message

#### Message:
消息包装类，将需要传递的消息封装在里面
#### MessageQueue:
消息队列，其实是一个单向链表，
MessageQueue的创建：

    MessageQueue(boolean quitAllowed) {
        mQuitAllowed = quitAllowed;
        mPtr = nativeInit();
    }
这里会创建一个native层的MessageQueue，并且将引用地址返回给Java层保存在mPtr变量中，通过这种方式将Java层的对象与Native层的对象关联在了一起
在Native层初始化的时候会创建一个ｌｏｏｐｅｒ（这个looper和java中的没有多大关系）
Ｑｕｅｕｅ的ｎｅｘｔ方法

循环体内首先调用nativePollOnce(ptr, nextPollTimeoutMillis)，这是一个native方法，实际作用就是通过Native层的MessageQueue阻塞nextPollTimeoutMillis毫秒的时间：
nativePollOnce方法返回后，就代表next方法就可以从mMessages中获取一个消息，也就是说如果消息队列中没有消息存在nativePollOnce就不会返回。

1.如果nextPollTimeoutMillis=-1，一直阻塞不会超时。

2.如果nextPollTimeoutMillis=0，不会阻塞，立即返回。

3.如果nextPollTimeoutMillis>0，最长阻塞nextPollTimeoutMillis毫秒(超时)，如果期间有程序唤醒会立即返回。

异步消息和同步消息

#### Handler：
所有的消息都是调用sendMessageAtTime去添加到队列中的

消息辅助类，主要功能就是向消息池中发送消息事件，

#### Looper：
不断的循坏执行，从ＭｅｓｓａｇｅＱｕｅｕｅ中读取消息，按分发机制将消息发送给目标这处理；主线程中默认实现了一个messageQueue，
1. 创建与线程绑定的Looper，同时会创建一个与之关联的MessageQueue用于存放消息
2. 开启消息循环，从MessageQueue中获取待处理消息，若无消息会阻塞线程
3. 通过Handler发送消息，此时会将Message入队列到MessageQueue中，并且唤醒等待的Looper
4. Looper获取的消息会投递给对应的Handler处理
### epoll:
1. int epoll_create(int size);创建一个epoll句柄，当这个句柄创建完成之后，在/proc/进程id/fd中可以看到这个fd。
2. int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event);注册事件函数。
3. int epoll_wait(int epfd, struct epoll_event * events, int maxevents, int timeout);等待事件的发生，参数timeout是超时时间毫秒值，0会立即返回，-1将不确定，也就是说有可能永久阻塞。该函数返回需要处理的事件数目，如返回0表示已超时。


