一直对View 的几个触摸事件的几个方法调用时机不是很清除，背了好几次都没有记住，今天就来看看源码到底是怎么实现的．


## View的事件分发：
其实View是没有事件分发的只有对一个事件的处理与不处理，但是我们所说的事件分发的那几个方法确实从在的，我们从View的dispatchTouchEvent方法看起：


**View.dispatchTouchEvent(motionEvent event):**



```
 public boolean dispatchTouchEvent(MotionEvent event) {

      ...

        if (onFilterTouchEventForSecurity(event)) {
            if ((mViewFlags & ENABLED_MASK) == ENABLED && handleScrollBarDragging(event)) {
                result = true;
            }
          /**

          我们可以清楚的看到如果我们设置了OnTouchListener，那么下面的方法肯定会调用
          但是：OnTouchListener的ontouch方法是有返回值的，（默认返回false）

          返回：true  ->  result = true;那么onTouchEvent(event)就不会被调用了．
                false ->  result = false,那么就会调用onTouchEvent(event)．

            从这段代码就只能看出来这些信息．


          */
            ListenerInfo li = mListenerInfo;
            if (li != null && li.mOnTouchListener != null
                    && (mViewFlags & ENABLED_MASK) == ENABLED
                    && li.mOnTouchListener.onTouch(this, event)) {
                result = true;
            }

            if (!result && onTouchEvent(event)) {
                result = true;
            }
        }

    ...

        return result;
    }
```

接下来我们看一下View的onTouchEvent(event)方法：


**View.onTouchEvent(event):**


```
  public boolean onTouchEvent(MotionEvent event) {
      ...
       if (clickable || (viewFlags & TOOLTIP) == TOOLTIP) {
            switch (action) {

                 case MotionEvent.ACTION_UP:

                  if ((mPrivateFlags & PFLAG_PRESSED) != 0 || prepressed) {

                       ...

                        if (!mHasPerformedLongPress && !mIgnoreNextUpEvent) {
                            removeLongPressCallback();

                            if (!focusTaken) {
                                // Use a Runnable and post this rather than calling
                                // performClick directly. This lets other visual state
                                // of the view update before click actions start.
                                if (mPerformClick == null) {
                                    mPerformClick = new PerformClick();
                                }
                                if (!post(mPerformClick)) {
                                /**
                                这个方法就是处理点击事件的回调接口

                                这个方法是在我们手指抬起的时候调用的

                                */

                                    performClick();
                                }
                            }
                        }

                        ...

                     }
                    break;

                 case MotionEvent.ACTION_DOWN:
                    break;
                  case MotionEvent.ACTION_CANCEL:
                    break;
                 case MotionEvent.ACTION_MOVE:
                    break;

            }

              return true;
        }


        return false;
  }
```
逻辑很简单，只有我们手指按下或移动的时候，没有返回false，那么手指抬起的事件才会有可能响应点击事件，如果在down事件中返回了false那么意味着这个view 不处理这个事件，以后事件也就不会交给它来处理了．



## ViewGroup的事件分发：


上面说了View的事件处理机制，那么接下来看看ViewGroup是怎么处理事件分发的：



**ViewGroup.dispatchTouchEvent(MotionEvent ev):**


```
 @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//这个事件分发是从父容器中调用过来的

         if (onFilterTouchEventForSecurity(ev)) {

           ．．．

            if (actionMasked == MotionEvent.ACTION_DOWN
                    || mFirstTouchTarget != null) {
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                if (!disallowIntercept) {
                    /**
                    当手指down或者是mFirstTouchTarget不为空的时候，
                    也就是说一次新的按下事件或者是之前处理过的事件有需要下发的时候
                    调用onInterceptTouchEvent方法．这个方法中也就是做了一些判断，

                    默认是返回fasle的

                    如果我们重写了这个方法那就不一定了．

                    */

                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
                    intercepted = false;
                }
            } else {
                // There are no touch targets and this action is not an initial down
                // so this view group continues to intercept touches.
                intercepted = true;
            }

           ．．．

            if (!canceled && !intercepted) {

              //刚才默认onInterceptTouchEvent返回false，那么就会进入这个方法

                if (actionMasked == MotionEvent.ACTION_DOWN
                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {

                    if (newTouchTarget == null && childrenCount != 0) {
                       //循坏遍历同一个父容器中相同位置的子控件，为了方便理解，
                       //我们可以想象为relativeLayout控件叠加时候的情况．

                        for (int i = childrenCount - 1; i >= 0; i--) {

                           /**

                           这个事件分发的一个重要的方法

                           主要是用来处理传入的孩子是不是需要继续分发，


                          如果孩子为空，那么就会将事件返回给父容器或者活动来处理，
                          要是可以处理那么就来处理


                           */
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                               //如果处理成功，意思就是有下发事件有子View处理了
                               //那么就将这个子View添加到链表中，用来下次事件的处理．

                                newTouchTarget = addTouchTarget(child, idBitsToAssign);

                                break;
                            }


                        }

                    }

                    if (newTouchTarget == null && mFirstTouchTarget != null) {

                        newTouchTarget = mFirstTouchTarget;
                        while (newTouchTarget.next != null) {
                            newTouchTarget = newTouchTarget.next;
                        }
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                    }
                }
            }

            // Dispatch to touch targets.
            if (mFirstTouchTarget == null) {
                // No touch targets so treat this as an ordinary view.
                handled = dispatchTransformedTouchEvent(ev, canceled, null,
                        TouchTarget.ALL_POINTER_IDS);
            } else {

               //循环在链表中取得之前处理过的事件target，用于处理
            }


        }

          return handled;
    }
```

通过我们查看了这个事件分发源码我们现在就可以秦楚的知道：

## 总结：

- 当ViewGroup的onInterceptTouchEvent方法如果返回了true那么就要调用onTouchEvent方法了．（当然，View 不存在这个方法）
- 当我们给一个View设置OnTouchListenter方法，那么如果这个方法会最先被调用，但是如果返回了ture那么就没有后面的方法什么事了，如果返回false那么就会调用OnTouchEvent方法，最后会调用onClick方法
- 如果一个View的onTouchEvent事件返回了false那么就意味这这个View不处理这个事件，父容器的OnTouchEvnet方法会调用．这就是典型的责任链模式
- 一个事件序列是说　当手指down->move->move....->up．同一个事件序列只能被同一个View消耗．

