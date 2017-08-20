
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

    def foldBinaryExpr(x: Expr ~ Option[List[Elem ~ Expr]]) = {
      x match {
        case lhs ~ Some(list) => list.foldLeft(lhs: Expr) {
          (x, y) => BinaryExpr(y._1, x, y._2)
        }
        case lhs ~ None => lhs
      }
    }

    //    def module: Parser[Expr] = {
    ////
    ////    }


    def block: Parser[Expr] = {
      L_BRACE ~ rep(expr <~ SEM) ~ R_BRACE ^^ {
        case _ ~ list ~ _ => Block(list)
      }
    }

    def expr: Parser[Expr] = {
      expr0 ~ expr_help ^^ foldBinaryExpr
    }

    def expr_help: Parser[Option[List[Elem ~ Expr]]] = {
      rep1(AND ~ expr0 |
        OR ~ expr0 |
        XOR ~ expr0) ^^ { list => Some(list) } |
        epsilon ^^ { _ => None }
    }

    def expr0: Parser[Expr] = {
      expr1 ~ expr0_help ^^ foldBinaryExpr
    }

    def expr0_help: Parser[Option[List[Elem ~ Expr]]] = {
      rep1(E ~ expr1 | //==
        LE ~ expr1 | // <=
        GE ~ expr1 | //>=
        L ~ expr1 | // <
        G ~ expr1 //>
      ) ^^ { list => Some(list) } |
        epsilon ^^ { _ => None }
    }

    def expr1: Parser[Expr] = {
      expr2 ~ expr1_help ^^ foldBinaryExpr
    }

    def expr1_help: Parser[Option[List[Elem ~ Expr]]] = {
      rep1(PLUS ~ expr2 | MINUS ~ expr2) ^^ { list => Some(list) } |
        epsilon ^^ { _ => None }
    }

    def expr2: Parser[Expr] = {
      normal_expr ~ expr2_help ^^ foldBinaryExpr
    }

    def expr2_help: Parser[Option[List[Elem ~ Expr]]] = {
      rep1(MUL ~ normal_expr | DIV ~ normal_expr) ^^ { list => Some(list) } |
        epsilon ^^ { _ => None }
    }

    /// if表达式
    /// if (xxx) xxx | {xxx}
    /// if (xxx) xxx|{xxx} else xxx |{xxx}
    def if_expr: Parser[Expr] = {
      IF ~ L_BRACKET ~ expr ~ R_BRACKET ~ (expr | block) ~ ELSE ~ (expr | block) ^^ {
        case _ ~ _ ~ cond ~ _ ~ body ~ _ ~ else_body => IfExpr(cond, body, Some(else_body))
      } | // if (xxx) xxx|{xxx} else {xxx}
        IF ~ L_BRACKET ~ expr ~ R_BRACKET ~ (expr | block) ^^ {
          case _ ~ _ ~ cond ~ _ ~ body => IfExpr(cond, body, None)
        } // if (xxx) xxx; 或 if(xxx) {xxx}
    }

    /// while表达式
    /// while (xxx) xxx{xxx}
    def while_expr: Parser[Expr] = {
      WHILE ~ L_BRACKET ~ expr ~ R_BRACKET ~ (expr | block) ^^ {
        case _ ~ _ ~ cond ~ _ ~ body => WhileExpr(cond, body)
      }
    }

    def normal_expr: Parser[Expr] = {
      L_BRACKET ~> expr <~ R_BRACKET |
        identifier ~ assign_body ^^ {
          case IDENTIFIER(name) ~ None => Variable(name)
          case IDENTIFIER(name) ~ Some(e) => Assign(Variable(name), e)
        } |
        bool_literal ^^ { case BOOL_LITERAL(value) => BoolLiteral(value) } |
        number_literal ^^ { case NUMBER_LITERAL(value) => NumberLiteral(value) } |
        str_literal ^^ { case STR_LITERAL(value) => StringLiteral(value) } |
        if_expr |
        while_expr
    }

    def assign_body: Parser[Option[Expr]] = {
      ASSIGN ~ expr ^^ { case _ ~ e => Some(e) } |
        epsilon ^^ { _ => None }
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

    def apply(tokens: Seq[WhiteToken]): Either[WhiteParsrError, WhiteAST] = {
      val reader = new WhiteTokenReader(tokens)
      block(reader) match {
        case NoSuccess(msg, next) => Left(WhiteParsrError(msg))
        case Success(result, next) => Right(result)
        case x => Left(WhiteParsrError(x.toString))
      }
    }
  }

}