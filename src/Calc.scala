import scala.util.parsing.combinator.JavaTokenParsers

package parsec {

  object Calc {

    import scala.util.parsing.combinator._

    abstract class Expr

    case class Variable(name: String)

    case class BinaryExpr(operator: String, lhs: Expr, rhs: Expr) extends Expr

    case class UnaryExpr(operator: String) extends Expr

    case class Number(value: Double) extends Expr

    object ArithParser extends JavaTokenParsers {
      def expr: Parser[Expr] =
        (term ~ "+" ~ term) ^^ { case lhs ~ "+" ~ rhs => BinaryExpr("+", lhs, rhs) } |
          (term ~ "-" ~ term) ^^ { case lhs ~ "-" ~ rhs => BinaryExpr("-", lhs, rhs) } |
          term

      def term: Parser[Expr] =
        (factor ~ "*" ~ factor) ^^ { case lhs ~ "*" ~ rhs => BinaryExpr("*", lhs, rhs) } |
          (factor ~ "/" ~ factor) ^^ { case lhs ~ "/" ~ rhs => BinaryExpr("/", lhs, rhs) } |
          factor

      def factor: Parser[Expr] =
        "(" ~> expr <~ ")" |
          floatingPointNumber ^^ { x => Number(x.toFloat) }

      def parse(text: String) = {
        parseAll(expr, text)
      }
    }

    def parse(text: String): Option[Expr] = {
      val results = ArithParser.parse(text)
      println(results)
      if (results.successful) {
        val ast = results.get
        println(ast)
        return Some(ast)
      }
      return None
    }

    def eval(expr: Expr): Double = {
      expr match {
        case BinaryExpr("+", lhs, rhs) => eval(lhs) + eval(rhs)
        case BinaryExpr("-", lhs, rhs) => eval(lhs) - eval(rhs)
        case BinaryExpr("*", lhs, rhs) => eval(lhs) * eval(rhs)
        case BinaryExpr("/", lhs, rhs) => eval(lhs) / eval(rhs)
        case Number(x) => x
      }
    }

  }

}
