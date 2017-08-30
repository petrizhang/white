package white {

  sealed trait W_Token

  /// 标识符
  case class IDENTIFIER(str: String) extends W_Token {
    override def toString: String = str
  }

  /// 字符字面量
  case class STR_LITERAL(value: String) extends W_Token {
    override def toString: String = value
  }

  case class NUMBER_LITERAL(value: Double) extends W_Token {
    override def toString: String = value.toString
  }

  case class BOOL_LITERAL(value: Boolean) extends W_Token {
    override def toString: String = value.toString
  }

  case object PLUS extends W_Token {
    override def toString: String = "+"
  }

  case object MINUS extends W_Token {
    override def toString: String = "-"
  }

  case object MUL extends W_Token {
    override def toString: String = "*"
  }

  case object DIV extends W_Token {
    override def toString: String = "/"
  }

  /// 小于 <
  case object L extends W_Token {
    override def toString: String = "<"
  }

  /// 大于 >
  case object G extends W_Token {
    override def toString: String = ">"
  }

  /// 等于 ==
  case object E extends W_Token {
    override def toString: String = "=="
  }

  /// 小于等于 <=
  case object LE extends W_Token {
    override def toString: String = "<="
  }

  /// 大于等于 》=
  case object GE extends W_Token {
    override def toString: String = ">="
  }

  /// 大于等于 》=
  case object ASSIGN extends W_Token {
    override def toString: String = "="
  }

  /// and
  case object AND extends W_Token {
    override def toString: String = "and"
  }

  /// or
  case object OR extends W_Token {
    override def toString: String = "or"
  }

  /// xor
  case object XOR extends W_Token {
    override def toString: String = "xor"
  }

  /// not
  case object NOT extends W_Token {
    override def toString: String = "not"
  }

  /// if
  case object IF extends W_Token {
    override def toString: String = "IF"
  }

  /// if
  case object ELSE extends W_Token {
    override def toString: String = "ELSE"
  }

  /// while
  case object WHILE extends W_Token {
    override def toString: String = "WHILE"
  }

  /// (
  case object L_BRACKET extends W_Token {
    override def toString: String = "("
  }

  /// )
  case object R_BRACKET extends W_Token {
    override def toString: String = ")"
  }

  /// {
  case object L_BRACE extends W_Token {
    override def toString: String = "{"
  }

  /// {
  case object R_BRACE extends W_Token {
    override def toString: String = "}"
  }

  /// ;
  case object SEM extends W_Token {
    override def toString: String = ";"
  }

  case object COMMA extends W_Token {
    override def toString: String = ","
  }

  case object DEF extends W_Token {
    override def toString: String = "def";
  }

  case object VAR extends W_Token {
    override def toString: String = "var";
  }

}