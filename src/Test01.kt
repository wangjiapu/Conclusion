import java.util.*

fun main(args: Array<String>) {

}

class Test01{
    /**
     * 二叉树节点类
     */

    class BinaryTreeNode {
        internal var node: Int = 0
        internal var left: BinaryTreeNode? = null
        internal var right: BinaryTreeNode? = null
    }




    fun construct(pre:Arrays, ps:Int,pis:Int, end:Arrays,es:Int,ie:Int):BinaryTreeNode?{
        if(ps>pis)
            return null

        val node=pre[ps]
        val index=pis

        while (index <= ie)
    }


}

private operator fun Arrays.get(ps: Int): Int {
    this[ps]
}
