

object Ptest {

  import scala.util.parsing.combinator._

  abstract class Expr

  case class ElifExpr(cond: Expr, body: Expr) extends Expr

  case class IfExpr(cond: Expr, body: Expr, else_body: Option[Expr]) extends Expr

  case class Number(value: String) extends Expr

  object ArithParser extends JavaTokenParsers {
    def expr: Parser[Expr] = {
      "if" ~ "(" ~ expr ~ ")" ~ expr ~ opt("else" ~ expr) ^^ {
        case "if" ~ "(" ~ cond ~ ")" ~ body ~ else_body => {
          if (else_body.isEmpty) IfExpr(cond, body, None)
          else IfExpr(cond, body, Some(else_body.get._2))
        }
      } |
        floatingPointNumber ^^ { x => Number(x) } |
        "a" ~ "==" ~ floatingPointNumber ^^ { x => Number("a==x") } |
        "{" ~> expr <~ "}"
    }

    def parse(text: String) {
      val s = parseAll(expr, text)
      println(s)
    }
  }

}
