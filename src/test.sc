val s = """class A(xc: Int) {
  val x = xc
  def print = println("X")
}
class B(override val x: Int, val yc: Int)
  extends A(x) {
  val y = yc
  override def print = println("B")
}
object TT {
  def apply(x: String): A = {
    new A(x.toInt)
  }
  def unapply(arg: A): Option[String] = {
    Some(arg.x.toString)
  }
}
object Main {
  def main(args: Array[String]) {
    val a = new A(1)
    val any:Any = a
    any match {
      case TT("1") => println("TT(\"1\")")
      case x:A => println("x:A")
      case _ => println("none")
    }
  }
}
"""

