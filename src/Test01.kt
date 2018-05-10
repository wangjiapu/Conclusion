

fun main(args: Array<String>) {



}

class Test01{
    /**
     * 二叉树节点类
     */

    class BinaryTreeNode {
        internal var value: Int = 0
        internal var left: BinaryTreeNode? = null
        internal var right: BinaryTreeNode? = null
    }

    fun construct(preArray:Array<Int>,endArray:Array<Int>):BinaryTreeNode?{
        return construct(preArray,0,preArray.size,endArray,0,endArray.size)
    }

    fun construct(preArray: Array<Int>, ps:Int, pe:Int, endArray: Array<Int>, es:Int, ee :Int):BinaryTreeNode?{
        if(ps>pe)
            return null

        val value=preArray[ps]
        var index=es

        while (index <= ee && endArray[index] != value ){
            index ++
        }
        if (index>ee)
            RuntimeException("输入数组不合法！")


        val node=BinaryTreeNode()
        node.value=value

        node.left=construct(preArray,ps+1,ps+index-es,endArray,es,index-1)
        node.right=construct(preArray,ps+index-es+1,pe,endArray,index+1,ee)

        return node
    }


}

