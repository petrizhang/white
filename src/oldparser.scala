//package white {
//
//  import scala.util.parsing.combinator._
//
//  object oldparser extends JavaTokenParsers {
//    def foldBinaryExpr(x: Expr ~ List[String ~ Expr]): Expr = {
//      x match {
//        case lhs ~ list => {
//          list.foldLeft(lhs: Expr) {
//            (x, y) => BinaryExpr(y._1, x, y._2)
//          }
//        }
//      }
//    }
//
//    def expr: Parser[Expr] = {
//      expr0 ~ rep1("and" ~ expr0 | "or" ~ expr0 | "xor" ~ expr0) ^^ foldBinaryExpr |
//      expr0
//    }
//
//    def expr0: Parser[Expr] = {
//      expr1 ~ rep1(">" ~ expr1 | "<" ~ expr1 | "<=" ~ expr1 | ">=" ~ expr1 | "==" ~ expr1) ^^ foldBinaryExpr |
//        expr1
//    }
//
//    def expr1: Parser[Expr] = {
//      expr2 ~ rep1("+" ~ expr2 | "-" ~ expr2) ^^ foldBinaryExpr |
//      expr2
//    }
//
//    def expr2: Parser[Expr] = {
//      normal_expr ~ rep1("*" ~ normal_expr | "/" ~ normal_expr) ^^ foldBinaryExpr |
//      normal_expr
//    }
//
//    def normal_expr: Parser[Expr] = {
//      block |
//        floatingPointNumber ^^ { x => NumberLiteral(x.toFloat) } | // 数字字面量
//        "not" ~ expr ^^ { case _ ~ e => UnaryExpr("not", e) } | // 逻辑否
//        "if" ~ "(" ~ expr ~ ")" ~ expr ~ opt("else" ~ expr) ^^ {
//          case "if" ~ "(" ~ cond ~ ")" ~ body ~ else_body =>
//            if (else_body.isEmpty) IfExpr(cond, body, None)
//            else IfExpr(cond, body, Some(else_body.get._2))
//        } | // if语句
//        "true" ^^ { x => BoolLiteral(true) } | // 布尔字面量true
//        "false" ^^ { x => BoolLiteral(false) } | // 布尔字面量fasle
//        "(" ~> expr <~ ")" | // 括号表达式
//        stringLiteral ^^ { x => StringLiteral(x) } | // 字符串字面量
//        //identifier ~ "=" ~ expr ^^ { case v ~ _ ~ e => Assign(v.asInstanceOf[Variable], e) } | //赋值语句
//        identifier ^^ { x => x.asInstanceOf[Variable] } //变量引用
//    }
//
//    def block: Parser[Expr] = {
//      "{" ~> expr <~ "}" //|
//      // "{" ~ rep(expr ~ ";") ~ "}" ^^ { case _ ~ list ~ _ => Block(list.map(x => x._1)) } // 代码块
//    }
//
//    def identifier: Parser[Expr] = {
//      "[a-zA-Z][a-zA-Z0-9_]*".r ^^ { str => Variable(str) }
//    }
//
//    def parse(text: String): Unit = {
//      val s = parseAll(expr, text)
//      println(s)
//    }
//
//  }
//
//}
