package white {

  import scala.collection.mutable.ListBuffer

  class Stack[Elem] {
    private var stack: ListBuffer[Elem] = ListBuffer[Elem]()

    def pop: Elem = {
      stack.remove(0)
    }

    def push(e: Elem): Unit = {
      stack.prepend(e)
    }

    def top: Elem = {
      stack.head
    }

    def print(): Unit = {
      println("stack-top==>")
      stack.foreach {
        x => println(x)
      }
      println("<==stack-bottom")
    }

    def isEmpty: Boolean = {
      stack.isEmpty
    }
  }

  class WhiteInterpreter {

    case class Elem(var _1: W_AST, var _2: Int)

    var stack = new Stack[Elem]()

    def visit(expr: BinaryExpr): Unit = {
      var ast: W_AST = expr
      var visitedCount: Int = 1
      stack.push(Elem(ast, 1))
      while (!stack.isEmpty) {
        // 循环将第一个孩子入栈
        while (hasChild(ast)) {
          val first = firstChild(ast).get
          stack.push(Elem(first, 1))
          ast = first
        }
        // 查看栈顶元素
        ast = stack.top._1
        visitedCount = stack.top._2
        // 查看下一个孩子
        val next = nextChild(ast, visitedCount)
        // 如果没有下一个孩子,访问栈顶元素并出栈
        if (next.isEmpty) {
          stack.pop
          println(ast)
          ast = VoidExpr() // 这一步为了使前面的将孩子入栈的循环失效
        }
        else {
          // 计数并访问下一个孩子
          stack.top._2 += 1
          ast = next.get
          stack.push(Elem(ast,1))
        }
      }

    }

    def hasChild(ast: W_AST): Boolean = {
      ast match {
        case BinaryExpr(_, _, _) => true
        case _ => false
      }
    }

    def childCount(ast: W_AST): Int = {
      ast match {
        case BinaryExpr(_, _, _) => 2
        case _ => 0
      }
    }

    def nextChild(ast: W_AST, visitedCount: Int): Option[W_AST] = {
      ast match {
        case BinaryExpr(_, _, rhs) => {
          if (visitedCount < 2) Some(rhs)
          else None
        }
        case _ => None
      }
    }

    def firstChild(expr: W_AST): Option[W_AST] = {
      expr match {
        case BinaryExpr(_, lhs, _) => Some(lhs)
        case _ => None
      }
    }
  }

}
