
触发minor　gc的条件是当minor　内存不够用了就必须的gc

但是触发FULL　GC的条件是：
- 系统调用Sytem.gc就是去回收老年全面回收
- 老年区的内存不足时．
- 方法区空间不足的时候．
- 通过minor　gc后进入老年区的平均大小大于老年区的可用内存．
- Eden区向Space区复制对象时另个一个Space空间和老年区可用内存小于对象需要内存时　FULL GC