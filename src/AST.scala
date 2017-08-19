
package white {

  abstract class AST

  // 表达式
  class Expr() extends AST {
    protected val typename: String = "<void>"
  }

  case class VoidExpr() extends Expr

  // 变量
  case class Variable(name: String) extends Expr {
    override def toString: String = s"$name@v"
  }

  // 赋值
  case class Assign(variable: Variable, value: Expr) extends Expr {
    override def toString: String = s"$variable@v=value"
  }

  // 代码块
  case class Block(block: List[Expr]) extends Expr

  // 数字字面量
  case class NumberLiteral(value: Double) extends Expr {
    override def toString: String = s"$value@n"
  }

  // 字符串字面量
  case class StringLiteral(value: String) extends Expr {
    override def toString: String = s""""$value""""
  }

  // 布尔字面量
  case class BoolLiteral(value: Boolean) extends Expr {
    override def toString: String = s"$value@b"
  }

  // 二元运算
  case class BinaryExpr(operator: WhiteToken, lhs: Expr, rhs: Expr) extends Expr {
    override def toString: String = s"($operator,$lhs,$rhs)"
  }

  // 单目运算
  case class UnaryExpr(operator: WhiteToken, hs: Expr) extends Expr {
    override def toString: String = s"($operator hs)"
  }

  // 完整if表达式
  case class IfExpr(cond: Expr, body: Expr, else_body: Option[Expr]) extends Expr {
    override def toString: String = s"if ($cond){\n $body \n} else {\n $else_body\n}"
  }

  // while表达式
  case class WhileExpr(condition: Expr, body: Expr) extends Expr

  // 函数定义
  case class FunctionDef(name: String, params: List[String], body: Expr)

  // 函数调用
  case class FunctionCall(name: String, args: List[Expr])

}
