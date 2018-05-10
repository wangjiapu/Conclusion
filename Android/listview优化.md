

listView的优化方式有好多种：

最常见的就是　
- convertView的复用，这个也是加载布局优化，在加载布局前先判断convertView是不是为空，如果为空就去加载，如果不为空就直接使用.
    但是每次都要重新的findViewByid（）然后赋值，
- Viewhodler的使用是直接复用之前已经全部弄好的View,他是将setTag的方式将viewHolder存储在ｖｉｅｗ中，这个被称为优化加载控件．
- 分段式加载
- 分页加载　个人觉得不算是listView优化，
- 滑动暂停后加载．