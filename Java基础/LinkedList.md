

### LinkedList 我们都知道他是一个双向链表


具体的实现没有什么，只是他在获取节点的时候是通过将整个链表或分为两半来寻找的


```

Node<E> node(int index) {
// assert isElementIndex(index);
if (index < (size >> 1)) {
Node<E> x = first;
for (int i = 0; i < index; i++)
x = x.next;
return x;
} else {
Node<E> x = last;
for (int i = size - 1; i > index; i--)
x = x.prev;
return x;
}
}
```

时间复杂度将为了n/2,但是既然这样设计了为什么不全部使用二分查找呢？


