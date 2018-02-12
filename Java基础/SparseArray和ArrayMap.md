

##  这个内容应该放到android 的内存优化

- SparseArray比hashMap更省内存，hashmaph中有（key,value,hash,next）
而这个里面事int型的数组，和其他类型的数组，初始值只有10,比较省内存
- 少去了int----Intager的装箱操作，
- 查找使用的事key的二分查找，速度更快

## 缺点：

- 如果key 不是int型  直接使用arrayMap。
- 如果数据量大于千数量级，直接使用hasnMap


ArrayMap基本和SparseArray是一样的，只不过key可以是其他类型。