
package white {

  abstract class W_AST

  // 表达式
  class Expr() extends W_AST {
    protected val typename: String = "<void>"
  }

  case class VoidExpr() extends Expr

  // 变量引用
  case class VariableRef(name: String) extends Expr {
    override def toString: String = s"$name"
  }

  // 变量声明
  case class VariableDef(name: String, initValue: Expr) extends Expr

  // 赋值
  case class AssignExpr(variable: VariableRef, value: Expr) extends Expr {
    override def toString: String = s"$variable=$value"
  }

  // 代码块
  case class Block(block: List[Expr], var scope: Scope) extends Expr {

    override def toString: String = {
      val str = block.foldLeft("") {
        (x, y) => {
          x match {
            case "" => y.toString
            case _ => s"$x;\n$y"
          }
        }
      }
      s"\n{\n $str; \n}\n"
    }
  }

  // 数字字面量
  case class NumberLiteral(value: Double) extends Expr {
    override def toString: String = s"$value"
  }

  // 字符串字面量
  case class StringLiteral(value: String) extends Expr {
    override def toString: String = s""""$value""""
  }

  // 布尔字面量
  case class BoolLiteral(value: Boolean) extends Expr {
    override def toString: String = s"$value"
  }

  // 二元运算
  case class BinaryExpr(operator: W_Token, lhs: Expr, rhs: Expr) extends Expr {
    override def toString: String = s"($operator,$lhs,$rhs)"
  }

  // 单目运算
  case class UnaryExpr(operator: W_Token, hs: Expr) extends Expr {
    override def toString: String = s"($operator hs)"
  }

  // 完整if表达式
  case class IfExpr(cond: Expr, body: Expr, else_body: Option[Expr]) extends Expr {


    override def toString: String = {
      val else_str = else_body match {
        case Some(e) => "else " + e.toString
        case None => ""
      }
      s"if $cond $body $else_str"
    }
  }

  // while表达式
  case class WhileExpr(condition: Expr, body: Expr) extends Expr {
    override def toString: String = s"while $condition $body"
  }

  // 函数定义
  case class FunctionDef(name: String, params: List[String], body: Block) extends Expr

  // 函数调用
  case class FunctionCall(func: Expr, args: List[Expr]) extends Expr

  // 模块
  case class Module(body: List[Expr],var scope: Scope) extends W_AST

}
