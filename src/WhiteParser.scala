
package white {

  import scala.util.parsing.combinator.Parsers

  import scala.util.parsing.input.{NoPosition, Position, Reader}

  class WhiteTokenReader(tokens: Seq[WhiteToken]) extends Reader[WhiteToken] {
    override def first: WhiteToken = tokens.head

    override def rest: Reader[WhiteToken] = new WhiteTokenReader(tokens.tail)

    override def pos: Position = NoPosition

    override def atEnd: Boolean = tokens.isEmpty
  }

  object WhiteParser extends Parsers {
    override type Elem = WhiteToken

    def epsilon: Parser[Expr] = {
      success(VoidExpr())
    }

    def foldBinaryExpr(x: Expr ~ List[Elem ~ Expr]): Expr = {
      x match {
        case lhs ~ list => {
          list.foldLeft(lhs: Expr) {
            (x, y) => BinaryExpr(y._1, x, y._2)
          }
        }
      }
    }

    def foldBinaryExprList(list: List[Elem ~ Expr]) = {
      val void_expr = VoidExpr()
      list.foldLeft(void_expr: Expr) {
        (x, y) => BinaryExpr(y._1, x, y._2)
      }
    }

    def expr: Parser[Expr] = {
      expr0 ~ expr_help ^^ {
        case e ~ VoidExpr() => e
        case e ~ BinaryExpr(op, lhs, rhs) => BinaryExpr(op, e, rhs)
      }
    }

    def expr_help: Parser[Expr] = {
      rep1(AND ~ expr0 | OR ~ expr0 | XOR ~ expr0) ^^ foldBinaryExprList |
        epsilon
    }

    def expr0: Parser[Expr] = {
      expr1 ~ rep1(E ~ expr1 | //==
        LE ~ expr1 | // <=
        GE ~ expr1 | //>=
        L ~ expr1 | // <
        G ~ expr1 //>
      ) ^^ foldBinaryExpr |
        expr1
    }

    def expr1: Parser[Expr] = {
      expr2 ~ rep1(PLUS ~ expr2 | MINUS ~ expr2) ^^ foldBinaryExpr |
        expr2
    }

    def expr2: Parser[Expr] = {
      normal_expr ~ rep1(MUL ~ normal_expr | DIV ~ normal_expr) ^^ foldBinaryExpr |
        normal_expr
    }

    def normal_expr: Parser[Expr] = {

      L_BRACKET ~> expr <~ R_BRACKET |
        identifier ^^ { case IDENTIFIER(name) => Variable(name) } |
        bool_literal ^^ { case BOOL_LITERAL(value) => BoolLiteral(value) } |
        number_literal ^^ { case NUMBER_LITERAL(value) => NumberLiteral(value) } |
        str_literal ^^ { case STR_LITERAL(value) => StringLiteral(value) } |
        IF ~ L_BRACKET ~ expr ~ R_BRACKET ~ expr ~ opt(ELSE ~ expr) ^^ {
          case _ ~ _ ~ cond ~ _ ~ body ~ else_body =>
            if (else_body.isEmpty) IfExpr(cond, body, None)
            else IfExpr(cond, body, Some(else_body.get._2))
        } | // if语句
        L_BRACE ~> expr <~ R_BRACE |
        L_BRACE ~ rep(expr ~ SEM) ~ R_BRACE ^^ { case _ ~ list ~ _ => Block(list.map(x => x._1)) }
    }

    private def identifier: Parser[IDENTIFIER] = {
      accept("identifier", { case id@IDENTIFIER(name) => id })
    }

    private def str_literal: Parser[STR_LITERAL] = {
      accept("string literal", { case lit@STR_LITERAL(value) => lit })
    }

    private def bool_literal: Parser[BOOL_LITERAL] = {
      accept("bool literal", { case lit@BOOL_LITERAL(value) => lit })
    }

    private def number_literal: Parser[NUMBER_LITERAL] = {
      accept("nummber literal", { case lit@NUMBER_LITERAL(value) => lit })
    }

    def apply(tokens: Seq[WhiteToken]): Either[WhiteParsrError, AST] = {
      val reader = new WhiteTokenReader(tokens)
      expr(reader) match {
        case NoSuccess(msg, next) => Left(WhiteParsrError(msg))
        case Success(result, next) => Right(result)
      }
    }
  }

}