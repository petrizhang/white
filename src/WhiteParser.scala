
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

    def module: Parser[Module] = {
      expr_list ^^ { list => Module(list) }
    }

    def expr_list: Parser[List[Expr]] = {
      rep(expr <~ SEM)
    }

    def func_def: Parser[FunctionDef] = {
      DEF ~ identifier ~ L_BRACKET ~ identifier_list ~ R_BRACKET ~ block ^^ {
        case _ ~ id ~ _ ~ id_list ~ _ ~ body => FunctionDef(id, id_list, body)
      }
    }

    def identifier_list[List[String]] = {
      identifier ~ rep(COMMA ~> identifier) ^^ {
        case id ~ list => List(id) ::: list
      } |
        epsilon ^^ { _ => List("") }
    }

    def arg_list[List[Expr]] = {
      expr ~ rep(COMMA ~> expr) ^^ {
        case e ~ list => List(e) ::: list
      } |
        epsilon ^^ { _ => List(VoidExpr()) }
    }

    def block: Parser[Expr] = {
      L_BRACE ~ expr_list ~ R_BRACE ^^ {
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

    def var_def: Parser[VariableDef] = {
      VAR ~ identifier ~ opt(ASSIGN ~> expr) ^^ {
        case _ ~ id ~ None => VariableDef(id, VoidExpr())
        case _ ~ id ~ Some(e) => VariableDef(id, e)
      }
    }

    def normal_expr: Parser[Expr] = {
      bool_literal ^^ { case boolean => BoolLiteral(boolean) } |
        number_literal ^^ { case num => NumberLiteral(num) } |
        str_literal ^^ { case str => StringLiteral(str) } |
        if_expr |
        while_expr |
        func_def |
        var_def |
        func_call |
        identifier ~ assign_body ^^ {
          case name ~ None => VariableRef(name)
          case name ~ Some(e) => AssignExpr(VariableRef(name), e)
        } |
        L_BRACKET ~> expr <~ R_BRACKET
    }

    def func_call: Parser[Expr] = {
      identifier ~ rep1(L_BRACKET ~> arg_list <~ R_BRACKET) ^^ {
        case id ~ list => {
          val func = VariableRef(id)
          list.foldLeft(func: Expr) {
            (x, y) => FunctionCall(x, y)
          }
        }
      } |
        L_BRACKET ~ expr ~ R_BRACKET ~ rep1(L_BRACKET ~> arg_list <~ R_BRACKET) ^^ {
          case _ ~ e ~ _ ~ list => {
            list.foldLeft(e: Expr) {
              (x, y) => FunctionCall(x, y)
            }
          }
        }
    }


    def assign_body: Parser[Option[Expr]] = {
      ASSIGN ~ expr ^^ { case _ ~ e => Some(e) } |
        epsilon ^^ { _ => None }
    }

    private def identifier: Parser[String] = {
      accept("identifier", { case IDENTIFIER(name) => name })
    }

    private def str_literal: Parser[String] = {
      accept("string literal", { case STR_LITERAL(value) => value })
    }

    private def bool_literal: Parser[Boolean] = {
      accept("bool literal", { case BOOL_LITERAL(value) => value })
    }

    private def number_literal: Parser[Double] = {
      accept("nummber literal", { case NUMBER_LITERAL(value) => value })
    }

    def apply(tokens: Seq[WhiteToken]): Either[WhiteParsrError, WhiteAST] = {
      val reader = new WhiteTokenReader(tokens)
      module(reader) match {
        case NoSuccess(msg, next) => Left(WhiteParsrError(msg))
        case Success(result, next) => Right(result)
        case x => Left(WhiteParsrError(x.toString))
      }
    }
  }

}