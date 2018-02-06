# Activity及Fragemnt 生命周期及源码分析

## 官方定义
- fragment是依赖activity 的，不能单独存在的
- 一个activity 里面可以有多个fragment
- 一个fragement可以被多个activity重用
- fragment 有自己的生命周期
1. 说道fragment的生命周期就不得不说activity的生命周期了


生命周期|Activity  |  Fragment                   
 -------------------|----------------|--------------|
onCreate | 当activity第一次创建的时候调用，通过setContentView去加载布局，当然，也可以通过bundle参数来恢复异常情况下的activity的状态|在onCreate之前会有onAttach方法，是用来将fragment与activity绑定的，不过当我们调用动态加载fragment的时候就会发现，onAttach,oncreate,OnCreateView(创建视图),OnActivityCreate依次被调用，这样才相当与Activity的onCreate方法|
onStart|activity正在启动，acticity 已经开始了不过他还在后台，意味着我们不能与之交互|因为调用了activity的onstart方法，所以也调用了fragment的方法，fragment 中的控件也是无法与用户（因为，fragment是依附与activity的）
|onResume|可见，获取到焦点，处于活动状态|同理|
|onPause|activity正在停止，失去焦点，仍可见正常情况：调用onstop，极端情况：调用onResume,回到Activity，（有一次在onPause中开启一个Activity 然后finsh，导致应用直接挂到后台）|同理|
|onstop|失去焦点，位于后台，可以做一些稍微繁重的回收工作，同样不能太耗时||
|onDestory|最后一个回调，回收工作，资源释放，|onDestroyView,onDestroy,ODetach,|
|OnRestart|一般就是不可见重新变为可见的状态，例如用户使用home键返回后有开启activity|fragment没有onrestart方法，但是我们可以通过返回栈的方式，将fragment移除的时候添加到返回栈中，那个时候fragment就会调用onDestroyView方法，只是讲界面移除了|
|onSaveInstanceState|在onStop之前，但是和onPause 没有时序关系，保存数据到bundle中，同时传给onRestoreInstance和oncareate方法用来恢复状态|当然Fragment也提供了同样的方法便于保存数据|
|onRestoreInstance|恢复数据|同理|
		2.普遍情况：正常的启动 onCreate -> onStart  ->onResume
		3打开一个新的activity onPause -> onStop
		4 再次回到activity 时 onRestart -> onStart -> onResume 
		5 按返回键时  onPause -> onStop -> onDestroy
		6 按home键又返回  onPause ->onStop -> onRestart
- 可以动态的添加或删除
##优点
- 模块化
- 可重用
- 可适配
##Fragment的核心类
- Fragment：Fragment的基类，任何创建的Fragment都需要继承该类。
- FragmentManager：管理和维护Fragment。他是抽象类，具体的实现类是FragmentManagerImpl。
- FragmentTransaction：对Fragment的添加、删除等操作都需要通过事务方式进行。他是抽象类，具体的实现类是BackStackRecord。
- BackStackRecord实现了一个BackStackEntry，所以就可以通过这个来实现fragment回退栈。
## 基本操作
- add 向activity 中添加一个
- remove 从活动中移除一个fragment （如果没有回退栈 那么这个实例将被销毁）
- transaction.replace() 使用另一个Fragment替换当前的,实际上就是remove()
然后add()的合体
- transaction.hide() 隐藏当前的Fragment,仅仅是设为不可见,并不会销毁。不调用任何生命周期方法，调用该方法的前提是要显示的Fragment已经被添加到容器，只是纯粹把Fragment UI的setVisibility为false
- transaction.show() 显示之前隐藏的Fragment
- detach() 会将view从UI中移除,和remove()不同,onPause()->onStop()->onDestroyView()。UI从布局中移除，但是仍然被FragmentManager管理（remove的时候添加到了回退栈）
- 重建view视图,附加到UI上并显示，onCreateView()->onStart()->onResume()

## fragment 回退栈

一般情况下 我们是通过Fragment的添加，删除来依次显示界面的，但是，问什么每次都要移除？既然可以保存数据，那么我们就可以每次都只执行一个ui 的绘制不就可以了，联想到Activity 的任务栈，就要了这个Fragment的回退栈；

- 回退栈是由FragmentManager管理的，Fragment事务是不会加入回退栈的，如果想将Fragment事务加入回退栈，则可以加入addToBackStack("")
- PopBackStack():弹出一个顶层的fragment 没有什么东西就返回false；（异步）

- 
##activity的横竖屏问题：
- android:screenOrientation:是用来设置活动的显示方向的，默认值是：unSpecified　根据系统的屏幕方向选择
landscape:横屏，portrait:竖屏
- android:configChanges:用来设置ａｃｔｉｖｉｔｙ配置改变的，当配置改变时，活动默认情况下会先销毁在创建，但是如果设置了这个属性后，可以避免这种事的发生调用onConfigurationChanged()方法
- 在１３以前：
	1. 不设置android:configChanges，切横屏执行一次，切竖屏执行两次；
	2. 设置oriention，调用生命周期，只是调用一次
	3. 设置oriention | KeyboardHidden　不会调用生命周期，只会执行onConfigurationChanged()方法
- １３以后：
	1. 设置以前那几个都是执行一次，
	2. 只有加上screenSize时，才不会销毁activity　调用onConfigurationChanged()

- android api 13开始，屏幕尺寸会发生变化，如果要避免设备方向改变而导致重新ｏｎＣｒｅａｔｅ除了添加oriention外，还要添加screenSize,


## Activity的启动模式
#### 标准模式（standard）
标准的模式，没有什么可以说的
#### 栈顶复用模式（SingleTop）
- 如果在启动他这个任务栈中有这个activity实例，但是没有位于栈顶，直接创建，如果没有位于栈顶，那么就创建一个新的
- 如果新的活动位于栈顶了 那么这个活动的onCreate onStart都不会调用，只会调用 OnNewIntent去获得参数中的信息。

#### 栈内复用模式(SingleTask)
1. 先找需要压入得栈，如果没有 直接创建栈并创建活动的实力然后压入。
2. 如果栈已经存在，查找此栈中有没有该活动的实例，，有 移除上面的活动，此活动位于栈顶，，没有   直接创建压入；

这个模式下启动一个ａｃｔｉｖｉｔｙ 就先去找这个这个活动存在的那个栈，如果有，直接将这个活动之上的所有活动实例直接移除，并且，这个任务栈切换到前台。
例如：聊天界面（只希望存在一个）


#### 但实例模式(singleInstance)
启动时，无论从哪里启动，都会只给ａ创建一个唯一的任务栈，而且栈中只有他一个活动，之后不会再创建ａ除非ａ被销毁了。
