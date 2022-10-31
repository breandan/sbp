package edu.berkeley.sbp.scala
import edu.berkeley.sbp.Input.Location
import edu.berkeley.sbp.Input.Region

case class Tree(val label:String, val children:IndexedSeq[Tree], val region:Option[Region[_]]) {

  def childrenFlat : Boolean =
    children.map((t:Tree) => children.length == 0).foldLeft(true)(_ && _)

  def coalesceFlatHeadlessNodes : Tree =
   if (label.equals("") && childrenFlat)
      new Tree(children.map((t:Tree) => t.label).foldLeft("")(_ + _), Tree.emptyChildren, region)
   else
      new Tree(label, (children.map((t:Tree) => t.coalesceFlatHeadlessNodes)), region)
}

object Tree {
  val emptyChildren = IndexedSeq[Tree]()
}

object SBP {

  def mkTree(t:edu.berkeley.sbp.Tree[String]) : Tree = {
    val children = scala.collection.immutable.IndexedSeq[Tree](
      (for(i <- 0 until t.size())
        yield mkTree(t.child(i)))
      : _*
    )
    val region = t.getRegion
    return new Tree(t.getHead.toString, children, if (region==null) None else Some(region))
  }

}
