package white {

  import scala.util.parsing.combinator.JavaTokenParsers

  object WhiteScanner extends JavaTokenParsers {

    def e = "==" ^^ { _ => E }

    def le = "<=" ^^ { _ => LE }

    def ge = ">=" ^^ { _ => GE }

    def and = "and" ^^ { _ => AND }

    def or = "or" ^^ { _ => OR }

    def xor = "xor" ^^ { _ => XOR }

    def not = "not" ^^ { _ => NOT }

    def lBracket = "(" ^^ { _ => L_BRACKET }

    def rBracket = ")" ^^ { _ => R_BRACKET }

    def lBrace = "{" ^^ { _ => L_BRACE }

    def rBrace = "}" ^^ { _ => R_BRACE }

    def sem = ";" ^^ { _ => SEM }

    def l = "<" ^^ { _ => L }

    def g = ">" ^^ { _ => G }

    def assign = "=" ^^ { _ => ASSIGN }

    def plus = "+" ^^ { _ => PLUS }

    def minus = "-" ^^ { _ => MINUS }

    def mul = "*" ^^ { _ => MUL }

    def div = "/" ^^ { _ => DIV }

    def comma = "," ^^ { _ => COMMA }

    def if_ = "if" ^^ { _ => IF }

    def else_ = "else" ^^ { _ => ELSE }

    def while_ = "while" ^^ { _ => WHILE }

    def def_ = "def" ^^ { _ => DEF }

    def var_ = "var" ^^ { _ => VAR }

    def boolLiteral: Parser[BOOL_LITERAL] = {
      "true" ^^ { _ => BOOL_LITERAL(true) } |
        "false" ^^ { _ => BOOL_LITERAL(false) }
    }

    def numberLiteral: Parser[NUMBER_LITERAL] = {
      floatingPointNumber ^^ { str => NUMBER_LITERAL(str.toFloat) }
    }

    def strLiteral: Parser[STR_LITERAL] = {
      super.stringLiteral ^^ { str => STR_LITERAL(str) }
    }

    def identifier: Parser[IDENTIFIER] = {
      "[a-zA-Z_][a-zA-Z0-9]*".r ^^ { str => IDENTIFIER(str) }
    }

    def tokens: Parser[List[WhiteToken]] = {
      phrase(rep1(
        e |
          le |
          ge |
          lBracket |
          rBracket |
          lBrace |
          rBrace |
          sem |
          l |
          g |
          and |
          or |
          xor |
          not |
          assign |
          plus |
          minus |
          mul |
          div |
          comma |
          if_ |
          else_ |
          while_ |
          def_ |
          var_ |
          boolLiteral |
          numberLiteral |
          strLiteral |
          identifier
      ))
    }

    def apply(code: String): Either[WhiteScannerError, List[WhiteToken]] = {
      parse(tokens, code) match {
        case NoSuccess(msg, next) => Left(WhiteScannerError(msg))
        case Success(result, next) => Right(result)
      }
    }
  }

}