

## 进程间通信

#### 使用多进程有两种办法：
- 在AndroidMenifest中指定：  android:process 属性
- native层去fork一个新的进程

Android 系统会为一个应用分配一个唯一分ID  被称为  UID，只有UID相等才可以共享数据。

两个应用可以通过分享id来共享数据，不管是不是在同一进程中，当然 同一进程的话就是一个进程中的两个应用罢了；


#### 多进程造成的问题：

- 静态成员和单例模式完全失效。
- 线程同步机制完全失效。
- SharedPreferences的可靠性下降。
- application会多次创建。


###  IPC 基础概念：

#### [对象的序列化](https://github.com/wangjiapu/Conclusion/blob/master/Android/%E5%BA%8F%E5%88%97%E5%8C%96.md)
#### Binder

- 首先从Android Framework角度说：binder 是连接各个Manager之间的桥梁
- 从Android 层面来说，是客户端与服务端通信的媒介。
- 普通的service不涉及进程间通信比较简单， Messenger底层是AIDL ,


